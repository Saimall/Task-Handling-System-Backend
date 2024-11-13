package com.example.user_service.service;

import com.example.user_service.dto.*;
import com.example.user_service.exceptions.EmployeeAlreadyExistException;
import com.example.user_service.exceptions.EmployeeNotFoundException;
import com.example.user_service.exceptions.ManagerAlreadyExistException;
import com.example.user_service.exceptions.ManagerNotFoundException;
import com.example.user_service.model.Employee;
import com.example.user_service.model.Manager;
import com.example.user_service.model.Usermain;
import com.example.user_service.model.UserRole;
import com.example.user_service.repository.EmployeeRepo;
import com.example.user_service.repository.ManagerRepo;
import com.example.user_service.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.Console;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ManagerRepo managerRepo;

    @Autowired
    private UserRepo userRepo;
    
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);



    @Transactional
    public Manager registerManager(ManagerDto managerDto) {

        if(managerRepo.existsByEmail(managerDto.getEmail())) {
            throw new ManagerAlreadyExistException("Manager already exists with this email");
        }

        Manager manager = Manager.builder()
                .name(managerDto.getName())
                .email(managerDto.getEmail())
//                .password(managerDto.getPassword())
                .contact(managerDto.getContact())
                .build();
        Usermain user = Usermain.builder()
                .email(managerDto.getEmail())
                .password(encoder.encode(managerDto.getPassword()))
                .role(UserRole.MANAGER)
                .build();

        userRepo.save(user);
        managerRepo.save(manager);
        return manager;

    }

    public Employee registerEmployee(EmployeeDto employeedto, Long managerId,String token) {
        Manager manager = managerRepo.findById(managerId).orElseThrow(() -> new ManagerNotFoundException("Manager not found"));
//        if(employeeRepo.existsByEmail(employeedto.getEmail())) {
//            throw new EmployeeAlreadyExistException("Employee already exists with this email");
//        }
        Employee employee = Employee.builder()
                .name(employeedto.getName())
                .email(employeedto.getEmail())
//                .password(employeedto.getPassword())
                .contact(employeedto.getContact())
                .designation(employeedto.getDesignation())
                .manager(manager)
                .build();
        Usermain user = Usermain.builder()
                .email(employeedto.getEmail())
                .password(encoder.encode(employeedto.getPassword()))
                .role(UserRole.EMPLOYEE)
                .build();

        userRepo.save(user);
        employeeRepo.save(employee);
        EmailRequestDTO emailRequestDto = new EmailRequestDTO();       
        emailRequestDto.setSubject("Welcome to the Team! Please Update Your Password");
        emailRequestDto.setBody(
        	    "Hi " + employeedto.getName() + ",\n\n" +
        	    "Congratulations on joining the team! We’re excited to have you with us and look forward to working together.\n\n" +
        	    "To get started, please log in using the default password: 12345678. For your security, make sure to change this password as soon as possible after logging in.\n\n" +
        	    "If you need any help or have any questions, feel free to reach out!\n\n" +
        	    "Welcome aboard once again, and we're thrilled to have you with us."
        	);


        emailRequestDto.setToEmail(employeedto.getEmail());
        System.out.println("Preparing to send email notification...");
        
        
        String notificationUrl = "http://localhost:9093/notifications/sendEmail";
        System.out.println("Emaillbody"+emailRequestDto);
        WebClient webClient = WebClient.create();
                   webClient.post()
                       .uri(notificationUrl)
                       .header(HttpHeaders.AUTHORIZATION,token)
                       .bodyValue(emailRequestDto)
                       .retrieve()
                       .toBodilessEntity()
                       .subscribe(
                           response -> System.out.println("Email sent successfully"),
                           error -> System.err.println("Failed to send email: " + error.getMessage())
                       );
        
        return employee;
    }

    public AuthenticationResponse verify(Usermain user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(user.getEmail());
            Optional<Usermain> existingUser = userRepo.findByEmail(user.getEmail());
            if (existingUser.isPresent() && existingUser.get().getRole() == UserRole.MANAGER) {
                Manager existingManager = managerRepo.findByEmail(existingUser.get().getEmail());
                return new AuthenticationResponse(token, existingManager.getManagerId(), existingUser.get().getRole() );
            } else if (existingUser.isPresent() && existingUser.get().getRole() == UserRole.EMPLOYEE) {
                Employee existingEmployee = employeeRepo.findByEmail(existingUser.get().getEmail());
                return new AuthenticationResponse(token, existingEmployee.getEmpId(),existingUser.get().getRole());
            }
            else{
                throw new EmployeeNotFoundException("User not found.");
            }

        }
        return new AuthenticationResponse("Failed", null,null);

    }


    public List<Employee> viewEmployees(Long managerId) {
        Manager manager = managerRepo.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found with id: " + managerId));
        return manager.getEmployees();
    }




    @Transactional
    public String deleteEmployee(Long employeeId) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        // Remove the employee from the manager's list of employees
        Manager manager = employee.getManager();
        if (manager != null) {
            manager.getEmployees().remove(employee);
            System.out.println(manager);
            managerRepo.save(manager);
        }

       
        userRepo.deleteByEmail(employee.getEmail());

        
        employeeRepo.deleteById(employeeId);
        return "Employee deleted successfully";
    }


    public Employee viewEmployeeById(Long employeeId) {
       
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
    }

    public ManagerDashboardDto viewManagerDetails(Long managerId) {
        
        Manager manager = managerRepo.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found with id: " + managerId));

      
        ManagerDashboardDto managerDashboard = new ManagerDashboardDto();
        managerDashboard.setName(manager.getName());
        managerDashboard.setEmail(manager.getEmail());
        managerDashboard.setContact(manager.getContact());

        return managerDashboard;
    }

    public EmployeeDashboardDto viewEmployeeDetails(Long employeeId) {
        
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        
        EmployeeDashboardDto employeeDashboard = new EmployeeDashboardDto();
        employeeDashboard.setName(employee.getName());
        employeeDashboard.setEmail(employee.getEmail());
        employeeDashboard.setContact(employee.getContact());
        employeeDashboard.setDesignation(employee.getDesignation());
        System.out.println("Employee Details from dahsboard"+employeeDashboard);
        return employeeDashboard;
    }

    public String getEmployeeName(Long employeeId) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
        return employee.getName();
    }

   
    public Employee updateEmployee(Long employeeId, EmployeeDto employeeDto) {
    	
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
     
        employee.setName(employeeDto.getName());
        employee.setEmail(employee.getEmail());
        employee.setContact(employeeDto.getContact());
        employee.setDesignation(employeeDto.getDesignation());
       Employee employee2= employeeRepo.save(employee);
        return employee2;
    }

	public void updatePassword(String mail, String oldPassword, String newPassword) {
		 Usermain usermain=userRepo.findByEmail(mail)
	                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

	       System.out.println("User details"+usermain);
	       
	       String hasholdpasswordString=encoder.encode(oldPassword);
	       System.out.println(hasholdpasswordString);
	       

	        
	        if (oldPassword.equals(newPassword)) {
	            throw new IllegalArgumentException("New password cannot be the same as the old password.");
	        }

	        String hashedNewPassword = encoder.encode(newPassword);
	        usermain.setPassword(hashedNewPassword);
	        System.out.println(usermain.getPassword());
	      
	        userRepo.save(usermain);
	    }
		
	
}
