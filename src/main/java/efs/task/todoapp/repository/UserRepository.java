package efs.task.todoapp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserRepository implements Repository<String, UserEntity> {

    private ArrayList<UserEntity> userList;

    public UserRepository(){
        this.userList = new ArrayList<>();
    }

    @Override
    public String save(UserEntity userEntity) {
        userList.add(userEntity);
        return userEntity.getAuth();
    }

    @Override
    public UserEntity query(String s) {
        return userList.stream().filter( t -> t.getAuth().equals(s)).findAny().orElse(null);
    }

    @Override
    public List<UserEntity> query(Predicate<UserEntity> condition) {
        return userList.stream().filter(condition).collect(Collectors.toList());
    }

    @Override
    public UserEntity update(String s, UserEntity userEntity) {
        for (UserEntity u:
                userList) {
            if(u.getAuth() == s){
                u.update(userEntity.getUsername(), userEntity.getPassword());
                return u;
            }
        }
        return null;
    }

    @Override
    public boolean delete(String s) {
        UserEntity user = userList.stream().filter(t -> t.getAuth().equals(s)).findAny().orElse(null);
        if(user.equals(null)){
            return false;
        }
        else{
            userList.remove(user);
            return true;
        }
    }
}
