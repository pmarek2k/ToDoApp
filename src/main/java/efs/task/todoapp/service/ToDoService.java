package efs.task.todoapp.service;

import efs.task.todoapp.repository.TaskEntity;
import efs.task.todoapp.repository.TaskRepository;
import efs.task.todoapp.repository.UserEntity;
import efs.task.todoapp.repository.UserRepository;
import efs.task.todoapp.utils.BadRequestException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ToDoService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public ToDoService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public void addUser(UserEntity user) throws BadRequestException{
        if(userRepository.query(u -> u.getUsername().equals(user.getUsername())).isEmpty()){
            userRepository.save(user);
        }
        else {
            throw new BadRequestException("Username already exists", 409);
        }
    }

    public void addTask(TaskEntity task) throws BadRequestException{
        if(userRepository.query(u -> u.getAuth().equals(task.getAuth())).isEmpty()){
            throw new BadRequestException("Header auth is incorrect", 401);
        }
        else{
            taskRepository.save(task);
        }
    }

//    public boolean userExists(UserEntity user){
//        if(userRepository.query(u -> u.getUsername().equals(user.getUsername())).isEmpty()){
//            return false;
//        }
//        return true;
//    }

    public boolean isAuthCorrect(String auth){
        if(userRepository.query(t -> t.getAuth().equals(auth)).isEmpty()){
            return false;
        }
        return true;
    }

    public List<TaskEntity> getAllTasks(String auth) throws BadRequestException{
        if(userRepository.query(u -> u.getAuth().equals(auth)).isEmpty()){
            throw new BadRequestException("No user or incorrect password", 401);
        }
        return taskRepository.query(t -> t.getAuth().equals(auth));
    }

    public TaskEntity getTask(String auth, String id) throws BadRequestException, IllegalArgumentException{
        TaskEntity task = taskRepository.query(UUID.fromString(id));
        if(task == null){
            throw new BadRequestException("No task found", 404);
        }
        if(!(task.getAuth().equals(auth))){
            throw new BadRequestException("Task belongs to another user", 403);
        }
        if(userRepository.query(u -> u.getAuth().equals(auth)).isEmpty()){
            throw new BadRequestException("No user or incorrect password", 401);
        }
        return task;


    }

    public void modifyTask(String auth, String id, TaskEntity newTask) throws BadRequestException{
        TaskEntity task = getTask(auth, id);
        taskRepository.update(UUID.fromString(id), newTask);
    }

    public void deleteTask(String auth, String id) throws BadRequestException{
        TaskEntity task = taskRepository.query(UUID.fromString(id));
        if(task == null){
            throw new BadRequestException("No task found", 404);
        }
        if(!(task.getAuth().equals(auth))){
            throw new BadRequestException("Task belongs to another user", 403);
        }
        if(userRepository.query(u -> u.getAuth().equals(auth)).isEmpty()){
            throw new BadRequestException("No user or incorrect password", 401);
        }

        taskRepository.delete(UUID.fromString(id));
    }
}
