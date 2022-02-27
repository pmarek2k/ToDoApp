package efs.task.todoapp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskRepository implements Repository<UUID, TaskEntity> {

    private ArrayList<TaskEntity> taskList;

    public TaskRepository(){
        this.taskList = new ArrayList<>();
    }

    @Override
    public UUID save(TaskEntity taskEntity) {
        taskList.add(taskEntity);
        return taskEntity.getUUID();
    }

    @Override
    public TaskEntity query(UUID uuid) {
        return taskList.stream().filter( t -> t.getUUID().equals(uuid)).findAny().orElse(null);
    }

    @Override
    public List<TaskEntity> query(Predicate<TaskEntity> condition) {
        return taskList.stream().filter(condition).collect(Collectors.toList());
    }

    @Override
    public TaskEntity update(UUID uuid, TaskEntity taskEntity) {
        for (TaskEntity t:
             taskList) {
            if(t.getUUID().equals(uuid)){
                t.update(taskEntity);
                return t;
            }
        }
        return null;
    }

    @Override
    public boolean delete(UUID uuid) {
        TaskEntity task = taskList.stream().filter(t -> t.getUUID().equals(uuid)).findAny().orElse(null);
        if(task.equals(null)){
            return false;
        }
        else{
            taskList.remove(task);
            return true;
        }

    }
}
