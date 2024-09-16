package io.swagger.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Entity
@Table(name = "USERS")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = null;

    @Column(nullable = false, updatable = false)
    private String userId = null;

    @Column(name = "FIRST_NAME")
    private String firstName = null;

    @Column(name = "LAST_NAME")
    private String lastName = null;

    @Column(name = "EMAIL_ID",unique = true)
    private String emailId = null;

    @Column(name = "PASSWORD")
    private String password = null;


    public String getLoginStatus() {
        return LoginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        LoginStatus = loginStatus;
    }

    @Column(name = "LoginStatus")
    private String LoginStatus = null;

    @Column(name = "MSISDN",unique = true)
    private String msisdn = null;

    @Column(name = "DATE_OF_BIRTH")
    @DateTimeFormat
    private Date dob = null;

    @Column(name = "STATUS")
    private String status = "INACTIVE";

    @Column(name = "USER_NAME")
    private String userName = null;

    @Column(name = "WORKSPACE")
    private String workspace = null;

    public Float getWalBalnce() {
        return walBalnce;
    }

    public void setWalBalnce(Float walBalnce) {
        this.walBalnce = walBalnce;
    }

    @Column(name = "WALLETBAL")
    private Float walBalnce = null;



    public UserEntity(String userId, String firstName, String lastName, String emailId, String password, String msisdn, Date dob, String status, String userName, String workspace) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.password = password;
        this.msisdn = msisdn;
        this.dob = dob;
        this.status = status;
        this.userName = userName;
        this.workspace = workspace;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }
}
