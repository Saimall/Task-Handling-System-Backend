package com.example.user_service.controller;

import com.example.user_service.dto.EmployeeDto;
import com.example.user_service.dto.ManagerDashboardDto;
import com.example.user_service.dto.ManagerDto;
import com.example.user_service.exceptions.EmployeeNotFoundException;
import com.example.user_service.exceptions.ManagerAlreadyExistException;
import com.example.user_service.exceptions.ManagerNotFoundException;
import com.example.user_service.model.Employee;
import com.example.user_service.model.Manager;
import com.example.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager")
public class ManagerController {
    @Autowired
    private UserService userService;

 
    @PostMapping("/register")
    public ResponseEntity<Manager> registerManager(@RequestBody ManagerDto managerDto){
        try{
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userService.registerManager(managerDto));
        }
        catch (ManagerAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                   .body(null);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(null);
        }

    }


    @PostMapping("/registerEmployee/{managerId}")
    public ResponseEntity<Employee> registerEmployee(@RequestBody EmployeeDto employeedto, @PathVariable Long managerId){
        try{
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userService.registerEmployee(employeedto,managerId));
        }
        catch(ManagerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body(null);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(null);
        }
    }


   
    @GetMapping("/{managerId}/viewEmployees")
    public ResponseEntity<List<Employee>> viewEmployees(@PathVariable Long managerId){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.viewEmployees(managerId));
        }
        catch(ManagerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body(null);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(null);
        }
    }

  
    @GetMapping("/viewEmployeeById/{employeeId}")
    public ResponseEntity<Employee> viewEmployeeById(@PathVariable Long employeeId){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.viewEmployeeById(employeeId));
        }
        catch(ManagerNotFoundException | EmployeeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }



    

    //To Delete a Employee
    @DeleteMapping("/deleteEmployee/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.deleteEmployee(employeeId));
        }
        catch(EmployeeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/viewManagerDetails/{managerId}")
    public ResponseEntity<ManagerDashboardDto> viewManagerDetails(@PathVariable Long managerId){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.viewManagerDetails(managerId));
        }
        catch (ManagerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}