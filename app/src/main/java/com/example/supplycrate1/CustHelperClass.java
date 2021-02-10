package com.example.supplycrate1;

public class CustHelperClass {
    String Username,Pass,Email,Phoneno;

    public CustHelperClass() {
    }

    public CustHelperClass(String username, String email, String pass, String phoneno) {
        Username = username;
        Pass = pass;
        Email = email;
        Phoneno = phoneno;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneno() {
        return Phoneno;
    }

    public void setPhoneno(String phoneno) {
        Phoneno = phoneno;
    }
}
