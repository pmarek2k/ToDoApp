package efs.task.todoapp.repository;

import com.fasterxml.jackson.annotation.JsonGetter;
import efs.task.todoapp.utils.Base64Utils;

public class UserEntity {
    private String username;
    private String password;

    public UserEntity(String username, String password){
        this.username = username;
        this.password = password;
    }

    public UserEntity(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuth() {
        return new StringBuilder(Base64Utils.encode(username)).append(":").append(Base64Utils.encode(password)).toString();
    }

    public void update(String username, String password){
        setUsername(username);
        setPassword(password);
    }
}
