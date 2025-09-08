package com.showcase.api.controller;


import com.showcase.api.model.Project;
import com.showcase.api.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Project createProject(@RequestBody Project project){
        return projectRepository.save(project);
    }

}
