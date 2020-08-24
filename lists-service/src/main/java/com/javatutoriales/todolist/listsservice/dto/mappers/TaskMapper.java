package com.javatutoriales.todolist.listsservice.dto.mappers;

import com.javatutoriales.todolist.listsservice.dto.TaskDto;
import com.javatutoriales.todolist.listsservice.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface TaskMapper {
    TaskDto taskToTaskDto(Task task);

    Task taskDtoToTask(TaskDto taskDto);
}
