package com.cloud.ali.cloudhotel.models;

public class User {

    String first_name;
    String last_name;
    String u_id;
    String email;
    String password;
    int type;

    public User(){}

    public User(String first_name, String last_name, String u_id, String email, String password, int type){
        this.first_name = first_name;
        this.last_name = last_name;
        this.u_id = u_id;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
