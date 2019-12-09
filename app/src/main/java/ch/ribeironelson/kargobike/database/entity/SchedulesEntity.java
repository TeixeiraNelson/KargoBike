package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class SchedulesEntity {
    String scheduledId;
    String beginningDateTime;
    String endingDateTime;
    Boolean safetyCheck;
    Long nbDeliveries;

    public SchedulesEntity(){}

    public SchedulesEntity(String scheduledId, String beginningDateTime, String endingDateTime, Boolean safetyCheck, Long nbDeliveries) {
        this.scheduledId = scheduledId ;
        this.beginningDateTime = beginningDateTime;
        this.endingDateTime = endingDateTime;
        this.safetyCheck = safetyCheck;
        this.nbDeliveries = nbDeliveries;

    }

    public String getScheduledId() {
        return scheduledId;
    }

    public void setScheduledId(String scheduledId) {
        this.scheduledId = scheduledId;
    }

    public String getBeginningDateTime() {
        return beginningDateTime;
    }

    public void setBeginningDateTime(String beginningDateTime) {
        this.beginningDateTime = beginningDateTime;
    }

    public String getEndingDateTime() {
        return endingDateTime;
    }

    public void setEndingDateTime(String endingDateTime) {
        this.endingDateTime = endingDateTime;
    }

    public Boolean getSafetyCheck() {
        return safetyCheck;
    }

    public void setSafetyCheck(Boolean safetyCheck) {
        this.safetyCheck = safetyCheck;
    }

    public Long getNbDeliveries() {
        return nbDeliveries;
    }

    public void setNbDeliveries(Long nbDeliveries) {
        this.nbDeliveries = nbDeliveries;
    }

    @Override
    public String toString() {
        return beginningDateTime + endingDateTime;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("scheduledId", scheduledId);
        result.put("beginningDateTime", beginningDateTime);
        result.put("endingDateTime", endingDateTime);
        result.put("safetyCheck", safetyCheck);
        result.put("nbDeliveries", nbDeliveries);

        return result;
    }
}
