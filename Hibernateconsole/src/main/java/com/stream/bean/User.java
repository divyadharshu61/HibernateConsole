package com.stream.bean;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "UserTBL")
public class User {
	@Id
	@Column(name = "USERID", nullable = false)
	private String userID;
	
    @Column(name = "FULLNAME", nullable = false)
    private String fullName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "STATUS", nullable = false)
    private String accountStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDDATE", nullable = false)
    private Date createdDate;

    public String getUserID() {
    	return userID; 
    	}
    public void setUserID(String userID) {
    	this.userID = userID; 
    	}

    public String getFullName() {
    	return fullName; 
    	}
    public void setFullName(String fullName) {
    	this.fullName = fullName; 
    	}

    public String getEmail() { 
    	return email;
    	}
    public void setEmail(String email) {
    	this.email = email;
    	}

    public String getPhone() { 
    	return phone;
    	}
    public void setPhone(String phone) {
    	this.phone = phone; 
    	}

    public String getAccountStatus() { 
    	return accountStatus; 
    	}
    public void setAccountStatus(String accountStatus) { 
    	this.accountStatus = accountStatus; 
    	}

    public Date getCreatedDate() { 
    	return createdDate; 
    	}
    public void setCreatedDate(Date createdDate) {
    	this.createdDate = createdDate; 
    	}

    @Override
    public String toString() {
        return "User [userID=" + userID + ", fullName=" + fullName + ", email=" + email
                + ", phone=" + phone + ", accountStatus=" + accountStatus + ", createdDate=" + createdDate + "]";
    }
}