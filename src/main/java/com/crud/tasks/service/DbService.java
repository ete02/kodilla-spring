package com.crud.tasks.service;

import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DbService {
    private final TaskRepository repository;

    /* this constructor will be generated by @RequiredArgsConstructor
    public DbService (TaskRepository repository) {
        this.repository = repository;
    }*/

    public List<Task> getAllTasks(){
        return repository.findAll();
    }
    public Task getTaskById(final Long id) {
      return repository.findById(id).orElse(null);
   }

}

