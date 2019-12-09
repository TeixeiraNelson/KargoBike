package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CheckpointEntity implements Serializable {
    String idCheckpoint;
    String name;

    public CheckpointEntity(){}

    public CheckpointEntity(String idCheckpoint, String name) {
        this.idCheckpoint = idCheckpoint;
        this.name = name;
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
        result.put("name", name);

        return result;
    }
}
