package com.example.user_service.controller;

import com.example.user_service.dto.EmployeeDashboardDto;
import com.example.user_service.dto.EmployeeDto;
import com.example.user_service.exceptions.EmployeeNotFoundException;
import com.example.user_service.exceptions.ManagerNotFoundException;
import com.example.user_service.model.Employee;
import com.example.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    @Autowired
    private UserService userService;

    
    @GetMapping("/viewEmployeeDetails/{employeeId}")
    public ResponseEntity<EmployeeDashboardDto> viewEmployeeDetails(@PathVariable Long employeeId){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.viewEmployeeDetails(employeeId));
        }
        catch (EmployeeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body(null);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(null);
        }
    }

    @GetMapping("/getEmployeeName/{employeeId}")
    public ResponseEntity<String> getEmployeeName(@PathVariable Long employeeId){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.getEmployeeName(employeeId));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @PutMapping("/updateEmployee/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeeDto employeeDto){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.updateEmployee(employeeId, employeeDto));
        }
        catch(ManagerNotFoundException | EmployeeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

}
