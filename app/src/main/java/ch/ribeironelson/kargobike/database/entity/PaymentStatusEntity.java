package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class PaymentStatusEntity {
    private String idStatus;
    private String statusName;

    public PaymentStatusEntity(){}

    public PaymentStatusEntity(String statusName) {
        this.statusName = statusName;
    }

    public String getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(String idStatus) {
        this.idStatus = idStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("statusName", statusName);
        return result;
    }
}
