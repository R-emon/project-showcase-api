package com.showcase.api.controller;


import com.showcase.api.exception.ResourceNotFoundException;
import com.showcase.api.exception.UnauthorizedAccessException;
import com.showcase.api.model.Project;
import com.showcase.api.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {


    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public List<Project> getAllProject(){
        return projectRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Project> createProject(@RequestBody Project project, Principal principal){
        String ownerUsername = principal.getName();
        project.setOwnerUsername(ownerUsername);
        Project saveProject = projectRepository.save(project);

        return new ResponseEntity<>(saveProject, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable String id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable String id, @RequestBody Project projectDetails, Principal principal){
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        if(!existingProject.getOwnerUsername().equals(principal.getName())){
            throw new UnauthorizedAccessException("You do not have permission to update this project.");
        }

        existingProject.setTitle(projectDetails.getTitle());
        existingProject.setDescription(projectDetails.getDescription());
        existingProject.setImageUrl(projectDetails.getImageUrl());
        existingProject.setLiveUrl(projectDetails.getLiveUrl());
        existingProject.setRepoUrl(projectDetails.getRepoUrl());
        existingProject.setTags(projectDetails.getTags());

        return projectRepository.save(existingProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id, Principal principal){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if(!project.getOwnerUsername().equals(principal.getName())){
            throw new UnauthorizedAccessException("You do not have permission to delete this project.");
        }

        projectRepository.delete(project);

        return ResponseEntity.noContent().build();
    }

}
