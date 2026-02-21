package com.stream.bean;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "SubscriptionTBL")
@SequenceGenerator(name = "sub_seq_gen", sequenceName = "SUBSCRIPTIONSEQ", allocationSize = 1)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sub_seq_gen")
    @Column(name = "SUBSCRIPTIONID")
    private int subscriptionID;

    @Column(name = "USERID", nullable = false)
    private String userID;

    @Column(name = "PLANCODE", nullable = false)
    private String planCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STARTDATE", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENDDATE", nullable = false)
    private Date endDate;

    @Column(name = "STATUS", nullable = false)
    private String status;

    public int getSubscriptionID() { 
    	return subscriptionID;
    	}
    public void setSubscriptionID(int subscriptionID) {
    	this.subscriptionID = subscriptionID; 
    	}

    public String getUserID() { 
    	return userID; 
    	}
    public void setUserID(String userID) {
    	this.userID = userID;
    	}

    public String getPlanCode() {
    	return planCode; 
    	}
    public void setPlanCode(String planCode) { 
    	this.planCode = planCode; 
    	}

    public Date getStartDate() {
    	return startDate; 
    	}
    public void setStartDate(Date startDate) {
    	this.startDate = startDate; 
    	}

    public Date getEndDate() { 
    	return endDate;
    	}
    public void setEndDate(Date endDate) {
    	this.endDate = endDate; 
    	}

    public String getStatus() {
    	return status; 
    	}
    public void setStatus(String status) { 
    	this.status = status; 
    	}

    @Override
    public String toString() {
        return "Subscription [subscriptionID=" + subscriptionID + ", userID=" + userID + ", planCode=" + planCode
                + ", startDate=" + startDate + ", endDate=" + endDate + ", status=" + status + "]";
    }
}