package com.app.frimline.models;



public class UserListModel {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String emailId;
    private String firebaseToken;
    private boolean isSelected=false;

    public UserListModel() {
    }

    public UserListModel(String id, String firstName,String lastName,String emailId,String phoneNo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.phoneNo = phoneNo;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public UserListModel setSelected(boolean selected) {
        isSelected = selected;
        return this;
    }

    public String getId() {
        return id;
    }

    public UserListModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserListModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserListModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public UserListModel setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
        return this;
    }

    public String getEmailId() {
        return emailId;
    }

    public UserListModel setEmailId(String emailId) {
        this.emailId = emailId;
        return this;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public UserListModel setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
        return this;
    }
}
