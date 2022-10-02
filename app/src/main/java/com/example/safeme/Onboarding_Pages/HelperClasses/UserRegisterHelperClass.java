package com.example.safeme.Onboarding_Pages.HelperClasses;

public class UserRegisterHelperClass {
    String NIC;
    String Name;
    String Mobile;
    String Email;
    String City;

    public UserRegisterHelperClass(String nic, String name, String mobile, String email, String city) {
        NIC = nic;
        Name = name;
        Mobile = mobile;
        Email = email;
        City = city;
    }

    public UserRegisterHelperClass() {
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
