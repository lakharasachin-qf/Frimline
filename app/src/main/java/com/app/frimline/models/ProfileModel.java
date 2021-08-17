package com.app.frimline.models;

public class ProfileModel {
    private String token;
    private String email;
    private String displayName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    private String userId;
    private String firstName;
    private String lastName;
    private String role;
    private String userName;
    private String avatar;
    private Billing billingAddress;
    private Billing shippingAddress;
    private String phoneNo;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Billing getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Billing billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Billing getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Billing shippingAddress) {
        this.shippingAddress = shippingAddress;
    }



    /*"id": 66,
    "date_created": "2021-06-30T22:45:19",
    "date_created_gmt": "2021-06-30T17:15:19",
    "date_modified": "2021-08-11T23:19:07",
    "date_modified_gmt": "2021-08-11T17:49:07",
    "email": "sunnypatel477@gmail.com",
    "first_name": "Sunny",
    "last_name": "Patel",
    "role": "customer",
    "username": "Sunny Patel",
    "billing": {
        "first_name": "Sunny",
        "last_name": "Patel",
        "company": "Sunny patel",
        "address_1": "173",
        "address_2": "Vastral",
        "city": "Ahmedabad",
        "postcode": "382418",
        "country": "India",
        "state": "GJ",
        "email": "sunnypatel477@gmail.com",
        "phone": "1234567890"
    },
    "shipping": {
        "first_name": "Sunny",
        "last_name": "Patel",
        "company": "Sunny patel",
        "address_1": "173",
        "address_2": "Vastral",
        "city": "Ahmedabad",
        "postcode": "382418",
        "country": "India",
        "state": "GJ"
    },
    "is_paying_customer": false,
    "avatar_url": "https://secure.gravatar.com/avatar/ac244aac2d8b09340e8306fba1b7eade?s=96&d=mm&r=g",*/
}