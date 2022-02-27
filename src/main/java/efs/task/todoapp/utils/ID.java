package efs.task.todoapp.utils;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.UUID;

public class ID {
    private UUID id;

    public ID(){
        this.id = UUID.randomUUID();
    }

    @JsonGetter("id")
    public UUID getID(){
        return this.id;
    }
}
