package com.example.market.classes;

public class User {

    private String Name;
    private String Email;
    private String Type;

    public User()
    {

    }
    public User(String name, String email, String type) {
        Name = name;
        Email = email;
        Type = type;
    }
    public User(User user)
    {
        this.Name=user.getName();
        this.Email=user.getEmail();
        this.Type=user.getType();
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

}
