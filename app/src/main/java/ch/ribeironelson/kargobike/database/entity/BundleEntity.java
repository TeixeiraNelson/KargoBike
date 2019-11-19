package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class BundleEntity {
    private String idBundle;
    private String name;
    private float price;

    public BundleEntity(){}

    public BundleEntity(String name, float price){
        this.name = name;
        this.price = price;
    }

    public String getIdBundle() {
        return idBundle;
    }

    public void setIdBundle(String idBundle) {
        this.idBundle = idBundle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("price", price);
        return result;
    }
}
