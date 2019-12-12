package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WorkingZoneEntity implements Serializable {
    String workingZoneId;
    String location;

    public WorkingZoneEntity(){}

    public WorkingZoneEntity(String workingZoneId, String location) {
        this.workingZoneId = workingZoneId;
        this.location = location;
    }

    public WorkingZoneEntity(String location){
        this.location = location;
    }

    public String getWorkingZoneId() {
        return workingZoneId;
    }

    public void setWorkingZoneId(String workingZoneId) {
        this.workingZoneId = workingZoneId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return workingZoneId + " - " +location;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("location", location);
        result.put("workingZoneId", workingZoneId);
        return result;
    }
}
