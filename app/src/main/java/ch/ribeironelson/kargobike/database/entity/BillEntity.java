package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class BillEntity {
    private String idBill;
    private String toPaymentStatus;
    private float totalPrice;

    public BillEntity(){}

    public BillEntity(String toPaymentStatus, float totalPrice) {
        this.toPaymentStatus = toPaymentStatus;
        this.totalPrice = totalPrice;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public String getToPaymentStatus() {
        return toPaymentStatus;
    }

    public void setToPaymentStatus(String toPaymentStatus) {
        this.toPaymentStatus = toPaymentStatus;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("toPaymentStatus", toPaymentStatus);
        result.put("totalPrice", totalPrice);
        return result;
    }
}
