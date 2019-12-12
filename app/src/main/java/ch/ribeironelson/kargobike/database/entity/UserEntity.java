package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserEntity {
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String idUser;
    private String idZone;
    private List<SchedulesEntity> schedules;
    private String idRole;

    public UserEntity() {
        this.firstname = "";
        this.lastname = "";
        this.email = "";
        this.phoneNumber ="";
        this.idUser="";
        this.idZone="0";
        this.schedules = new ArrayList<>();
        this.idRole="0";
    }

    public UserEntity(String firstname, String lastname, String email, String phoneNumber,String idRole, String idUser, String idZone, List<SchedulesEntity> schedulesEntities) {
        this.firstname=firstname;
        this.lastname=lastname;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.idRole=idRole;
        this.idUser=idUser;
        this.idZone=idZone;
        this.schedules=schedulesEntities;
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

    public String getIdRole() { return idRole; }

    public void setIdRole(String idRole) { this.idRole = idRole; }

    public String getIdUser() {return idUser; }

    public void setIdUser(String idUser) {this.idUser = idUser; }

    public String getIdZone() {
        return idZone;
    }

    public void setIdZone(String idZone) {
        this.idZone = idZone;
    }

    public List<SchedulesEntity> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<SchedulesEntity> schedules) {
        this.schedules = schedules;
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
        result.put("idUser",idUser);
        result.put("idZone",idZone);
        result.put("schedules",schedules);
        result.put("idRole",idRole);

        return result;
    }


}
