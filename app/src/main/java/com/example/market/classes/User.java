package com.example.market.classes;

public class User {

    private String Name;
    private String Email;
    private String Type;
    private String Phone="";

    public User()
    {

    }

    public User(String name, String email, String type) {
        Name = name;
        Email = email;
        Type = type;
        Phone="";
    }

    public User(String name, String email, String type, String phone) {
        Name = name;
        Email = email;
        Type = type;
        Phone=phone;
    }
    public User(User user)
    {
        this.Name=user.getName();
        this.Email=user.getEmail();
        this.Type=user.getType();
        this.Phone=user.getPhone();
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
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
