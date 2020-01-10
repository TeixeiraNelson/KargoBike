package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TripEntity implements Serializable {
    CheckpointEntity checkpoint;
    String type;
    String idUser;
    String gpsCoordinates;
    String comment;
    String timestamp;

    public TripEntity(){}

    public TripEntity(CheckpointEntity checkpoint, String type, String idUser, String gpsCoordinates, String timestamp, String comment) {
        this.checkpoint = checkpoint;
        this.type = type;
        this.idUser = idUser;
        this.gpsCoordinates = gpsCoordinates;
        this.timestamp = timestamp;
        this.comment = comment;
    }

    public CheckpointEntity getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(CheckpointEntity checkpoint) {
        this.checkpoint = checkpoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getGpsCoordinates() {
        return gpsCoordinates;
    }

    public void setGpsCoordinates(String gpsCoordinates) {
        this.gpsCoordinates = gpsCoordinates;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idUser", idUser);
        result.put("comment",comment);
        result.put("checkpoint", checkpoint);
        result.put("type", type);
        result.put("gpsCoordinates", gpsCoordinates);
        result.put("timestamp", timestamp);

        return result;
    }
}
