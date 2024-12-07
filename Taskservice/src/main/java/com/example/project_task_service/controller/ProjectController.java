package com.example.project_task_service.controller;

import com.example.project_task_service.dto.ProjectDto;
import com.example.project_task_service.dto.ProjectRequestDto;
import com.example.project_task_service.dto.ProjectResponseDto;
import com.example.project_task_service.exceptions.ProjectNotFoundException;
import com.example.project_task_service.model.Project;
import com.example.project_task_service.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v2/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/addProject/{managerId}")
    public ResponseEntity<Project> addProject(@RequestBody ProjectDto projectDto, @PathVariable Long managerId) {
            Project newProject = projectService.addProject(projectDto, managerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProject);

    }

    @GetMapping("/getProjects/{managerId}")
    public ResponseEntity<?> getProjectsByManager(@PathVariable Long managerId) {
        try {
            List<ProjectDto> projects = projectService.getProjectsByManager(managerId);
            if (projects.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No projects found under this manager");
            }
            return ResponseEntity.status(HttpStatus.OK).body(projects);

        }
        catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No projects found under this manager");
        }
    }
    
    @GetMapping("/getProject/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable Long projectId) {
        try {
        	System.out.println("ID is coming:"+projectId);
            Project project = projectService.getProjectById(projectId);
            System.out.println("project data"+project);
            if (project==null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No projects found under this ID");
            }
            return ResponseEntity.status(HttpStatus.OK).body(project);

        }
        catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No projects found under this manager");
        }
    }

    //Each Project has unique project ID, So when we delete  by project id it is associated with particular manager only
    @DeleteMapping("/deleteProjects/{projectId}")
    public void deleteProjectsByManager(@PathVariable Long projectId){
        try {
            ResponseEntity.status(HttpStatus.OK)
                    .body(projectService.deleteProjectsByManager(projectId));
        }
        catch (ProjectNotFoundException e){
           ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body(null);
        }
        catch (Exception e) {
             ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @DeleteMapping("/deleteProjectsForTest/{projectId}")
    public ResponseEntity<?> deleteProjectsByManagerForTest(@PathVariable Long projectId){
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(projectService.deleteProjectsByManager(projectId));
        }
        catch (ProjectNotFoundException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body(null);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @PutMapping("/updateProjects/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable Long projectId,
            @RequestBody ProjectRequestDto projectRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(projectService.updateProject(projectId, projectRequestDto));
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
