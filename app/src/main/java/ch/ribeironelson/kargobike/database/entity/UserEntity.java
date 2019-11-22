package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserEntity {
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String idUser;
    private String idZone;
    private String idSchedule;

    public UserEntity() { }
    public UserEntity(String firstname, String lastname, String email, String phoneNumber,String idUser, String idZone, String idSchedule) {
        this.firstname=firstname;
        this.lastname=lastname;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.idUser=idUser;
        this.idZone=idZone;
        this.idSchedule=idSchedule;
    }
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdUser() {return idUser; }

    public void setIdUser(String idUser) {this.idUser = idUser; }

    public String getIdZone() {
        return idZone;
    }

    public void setIdZone(String idZone) {
        this.idZone = idZone;
    }

    public String getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(String idSchedule) {
        this.idSchedule = idSchedule;
    }

    @Override
    public String toString() {
        return firstname + lastname;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstname", firstname);
        result.put("lastname", lastname);
        result.put("email", email);
        result.put("phoneNumber", phoneNumber);

        return result;
    }
}
