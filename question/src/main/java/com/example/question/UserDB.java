package com.example.question;
import java.util.ArrayList;
import java.util.List;
import com.example.question.entity.User;

public class UserDB {
    private static List<User> users = new ArrayList<>();
    private static Integer ID = 1;
    public static boolean addUser(User user){
        for(User u : users){
            if(u.getUsername().equals(user.getUsername())){
                return false;
            }
        }
        ++ID;
        users.add(user);
        return true;
    }
    public static Integer getID(){
        return ID;
    }
    public static boolean findUser(User user){
        for(User u : users){
            if(u.getUsername()!=null&&u.getUsername().equals(user.getUsername())&&
                    u.getPassword()!=null&&u.getPassword().equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }
}