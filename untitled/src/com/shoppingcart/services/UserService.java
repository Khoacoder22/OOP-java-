package com.shoppingcart.services;

import com.shoppingcart.models.User;

import java.util.*;

public class UserService extends BaseServiceImp<User>{
    @Override
    public User getById(int id){
        for(User user : items){
            if(user.getId() == id){
                return user;
            }
        }
        return null;
    }
    @Override
    public void update(int id, String newName, double newPassword){
    }

    @Override
    public void delete(int id){
        User user = getById(id);
        if(user != null){
            items.remove(user);
            System.out.println("User with ID " + id + " has been removed.");
        }
        else {
            System.out.println("User with ID " + id + " does not exist.");
        }
    }


    // lấy danh sách user
    public  List<User> getUsers(){
        return items;
    }

    public User findByUsername(String Username){
        for(User user : getAll()){
            if(user.getUsername().equals(Username)){
                return user;
            }
        }
        return null;
    }

    public User login(String username, String password){
        for(User user : items){
            if(user.getUsername().equals(username) && user.getUserpasswrod().equals(password)){
                System.out.println("User " + username + " logged in.");
                return user;
            }
        }
        return null;
    }

    public boolean addUser(User user){
        for(User u : items){
            if(u.getUsername().equals(user.getUsername())){
                System.out.println("Username " + user.getUsername() + " already exists.");
                return false;
            }
        }
        items.add(user);
        System.out.println("User" + user.getUsername() + "Registered");
        return true;
    }
}
