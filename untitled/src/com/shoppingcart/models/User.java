package com.shoppingcart.models;

public class User {
    private static int counter = 1;
    private final int id;
    private final String username;
    private final String userpasswrod;
    private final Role role;

    public User(String username, String userpasswrod, Role role){
        this.id = counter++;
        this.username = username;
        this.userpasswrod = userpasswrod;
        this.role = role;
    }

    public int getId(){
        return id;
    }

    // getter v setter
    public String getUsername(){
        return username;
    }

    public String getUserpasswrod(){
        return  userpasswrod;
    }

    public Role getRole(){
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}

