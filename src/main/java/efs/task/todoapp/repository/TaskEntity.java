package efs.task.todoapp.repository;

import com.fasterxml.jackson.annotation.*;
import efs.task.todoapp.utils.ID;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@JsonPropertyOrder({ "id", "description", "due" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskEntity {
    private ID id;
    @JsonIgnore
    private String auth;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd"
    )
    private Date due;

    private String description;

    public TaskEntity(String date, String description, String auth) throws ParseException {
        this.id = new ID();

        if(date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            this.due = df.parse(date);
        }
        else{
            this.due = null;
        }
        if(description != null) {
            this.description = description;
        }
        else{
            this.description = null;
        }
        this.auth = auth;
    }

    public TaskEntity(){
        this.id = new ID();
    }

    @JsonGetter("id")
    public UUID getUUID() {
        return this.id.getID();
    }

    public ID getID(){
        return this.id;
    };

    @JsonGetter("due")
    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    @JsonGetter("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void update(TaskEntity newTask) {
        if (newTask.getDue() != null) {
            this.due = newTask.getDue();
        }
        else{
            this.due = null;
        }
        if (newTask.getDescription() != null) {
            this.description = newTask.getDescription();
        }
        else{
            this.description = null;
        }
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth){
        this.auth = auth;
    }

}
