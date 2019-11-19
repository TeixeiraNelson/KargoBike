package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class PackageEntity {
    private String idPackage;
    private float packageWeight;
    private String details;
    private String lastPosition;
    private String name;

    public PackageEntity(){}

    public PackageEntity(float packageWeight, String details, String lastPosition, String name) {
        this.packageWeight = packageWeight;
        this.details = details;
        this.lastPosition = lastPosition;
        this.name = name;
    }

    public String getIdPackage() {
        return idPackage;
    }

    public void setIdPackage(String idPackage) {
        this.idPackage = idPackage;
    }

    public float getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(float packageWeight) {
        this.packageWeight = packageWeight;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(String lastPosition) {
        this.lastPosition = lastPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("packageWeight", packageWeight);
        result.put("details", details);
        result.put("lastPosition", lastPosition);
        result.put("name", name);
        return result;
    }
}
