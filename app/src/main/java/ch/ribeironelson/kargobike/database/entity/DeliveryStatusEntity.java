package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class DeliveryStatusEntity {
    private String idDeliveryStatus;
    private String statusName;

    public DeliveryStatusEntity(){}

    public DeliveryStatusEntity(String statusName){
        this.statusName = statusName;
    }

    public String getIdDeliveryStatus() {
        return idDeliveryStatus;
    }

    public void setIdDeliveryStatus(String idDeliveryStatus) {
        this.idDeliveryStatus = idDeliveryStatus;
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
