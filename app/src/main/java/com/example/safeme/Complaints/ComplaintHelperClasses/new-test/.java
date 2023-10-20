package com.example.safeme.Complaints.ComplaintHelperClasses;

public class ComplaintHelperClass {
    String NIC;
    String Name;
    String Mobile;
    String City;
    String CID;
    String Date;
    String Description;
    String CType;
    String ImageLink;
    String Status;
    String check;
    String ASERfwrgStatus;
    String touytucheck;

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public ComplaintHelperClass(String check) {
        this.check = check;
    }

    public ComplaintHelperClass() {
    }

    public ComplaintHelperClass(String nic, String name, String mobile, String city, String cid, String date, String description, String cType, String status) {
        NIC = nic;
        Name = name;
        Mobile = mobile;
        City = city;
        CID = cid;
        Date = date;
        Description = description;
        CType = cType;
        Status = status;
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

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCType() {
        return CType;
    }

    public void setCType(String CType) {
        this.CType = CType;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
    public void setCType(String CType) {
        this.CType = CType;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
    
}
