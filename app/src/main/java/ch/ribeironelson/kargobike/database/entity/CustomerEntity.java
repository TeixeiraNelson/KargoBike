package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerEntity {
    private String idCustomer;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    List<DeliveryEntity> orders;

    public CustomerEntity(){}

    public CustomerEntity(String firstname, String lastname, String email, String phone, String address){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<DeliveryEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<DeliveryEntity> orders) {
        this.orders = orders;
    }

    public void addOrder(DeliveryEntity delivery){
        if(orders==null)
            orders = new ArrayList<>();

        orders.add(delivery);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstname", firstname);
        result.put("lastname", lastname);
        result.put("email", email);
        result.put("phone", phone);
        result.put("address",address);
        result.put("orders", orders);
        return result;
    }
}
