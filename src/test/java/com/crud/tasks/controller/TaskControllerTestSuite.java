package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTestSuite {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DbService dbService;
    @MockBean
    private TaskMapper taskMapper;

    Task task;
    TaskDto taskDto;
    String jsonContent;

    @Before
    public void setUp() {
        Gson gson = new Gson();
        task = new Task(1L, "Title", "Content");
        taskDto = new TaskDto(1L, "Title", "Content");
        jsonContent = gson.toJson(taskDto);
    }

    @Test
    public void shouldGetTasks() throws Exception {
        //Given
        List<Task> tasks = new ArrayList<>();
        List<TaskDto> tasksDto = new ArrayList<>();
        LongStream.range(1, 3).forEach(i -> tasks.add(new Task(i, "Title " + i, "Content " + i)));
        LongStream.range(1, 3).forEach(i -> tasksDto.add(new TaskDto(i, "Title " + i, "Content " + i)));
        given(taskMapper.mapToTaskDtoList(tasks)).willReturn(tasksDto);
        given(dbService.getAllTasks()).willReturn(tasks);
        //When && Then
        mockMvc.perform(get("/v1/task/getTasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Title 1")))
                .andExpect(jsonPath("$[0].content", is("Content 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Title 2")))
                .andExpect(jsonPath("$[1].content", is("Content 2")));
    }

    @Test
    public void shouldGetTask() throws Exception {
        //Given
        given(taskMapper.mapToTaskDto(task)).willReturn(taskDto);
        given(dbService.getTask(1L)).willReturn(Optional.of(task));
        //When && Then
        mockMvc.perform(get("/v1/task/getTask/{taskId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Title")))
                .andExpect(jsonPath("$.content", is("Content")));
    }

    @Test
    public void shouldDeleteTask() throws Exception {
        //Given
        doNothing().when(dbService).deleteTask(1L);
        //When && Then
        mockMvc.perform(delete("/v1/task/deleteTask/{taskId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateTask() throws Exception {
        //Given
        taskDto = new TaskDto(1L, "Title 2", "Content 2");
        given(taskMapper.mapToTaskDto(ArgumentMatchers.any(Task.class))).willReturn(taskDto);
        given(taskMapper.mapToTask(ArgumentMatchers.any(TaskDto.class))).willReturn(task);
        given(dbService.saveTask(ArgumentMatchers.any(Task.class))).willReturn(task);
        //When && Then
        mockMvc.perform(put("/v1/task/updateTask")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Title 2")))
                .andExpect(jsonPath("$.content", is("Content 2")));
    }

    @Test
    public void shouldCreateTask() throws Exception {
        //Given
        given(taskMapper.mapToTaskDto(ArgumentMatchers.any(Task.class))).willReturn(taskDto);
        given(taskMapper.mapToTask(ArgumentMatchers.any(TaskDto.class))).willReturn(task);
        given(dbService.saveTask(ArgumentMatchers.any(Task.class))).willReturn(task);
        //When && Then
        mockMvc.perform(post("/v1/task/createTask")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk());
    }
}