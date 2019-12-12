package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CheckpointEntity implements Serializable {
    String idCheckpoint;
    String name;
    WorkingZoneEntity location;

    public CheckpointEntity(){}

    public CheckpointEntity(String name) {
        this.name = name;
    }

    public CheckpointEntity(String id, String name, WorkingZoneEntity workingZoneEntity) {
        this.idCheckpoint = id;
        this.name = name;
        this.location = workingZoneEntity;
    }
    public CheckpointEntity(String id, String name) {
        this.idCheckpoint = id;
        this.name = name;
    }

    public WorkingZoneEntity getLocation() {
        return location;
    }

    public void setLocation(WorkingZoneEntity workingZoneEntity) {
        this.location = workingZoneEntity;
    }

    public String getIdCheckpoint() {
        return idCheckpoint;
    }

    public void setIdCheckpoint(String idCheckpoint) {
        this.idCheckpoint = idCheckpoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idCheckpoint", idCheckpoint);
        result.put("location",location);
        result.put("name", name);

        return result;
    }
}
