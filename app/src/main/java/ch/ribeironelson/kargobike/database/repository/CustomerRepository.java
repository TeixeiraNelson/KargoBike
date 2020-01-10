package ch.ribeironelson.kargobike.database.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.CustomerEntity;
import ch.ribeironelson.kargobike.database.entity.ProductEntity;
import ch.ribeironelson.kargobike.database.firebase.CustomerListLiveData;
import ch.ribeironelson.kargobike.database.firebase.CustomerLiveData;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class CustomerRepository {
    private static CustomerRepository instance ;

    public static CustomerRepository getInstance(){
        if(instance == null) {
            synchronized (CustomerRepository.class) {
                if (instance == null) {
                    instance = new CustomerRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<List<CustomerEntity>> getAllCustomers() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("customers");

        return new CustomerListLiveData(reference);
    }

    public LiveData<CustomerEntity> getCustomer(final String productId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("customers")
                .child(productId);
        return new CustomerLiveData(reference);
    }

    public void insertCustomer(final CustomerEntity customer, OnAsyncEventListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("customers");
        String key = reference.push().getKey();
        customer.setIdCustomer(key);
        FirebaseDatabase.getInstance()
                .getReference("customers")
                .child(key)
                .setValue(customer, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void updateCustomer(final CustomerEntity customer, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("customers")
                .child(customer.getIdCustomer())
                .updateChildren(customer.toMap(),(databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void deleteCustomer(final CustomerEntity customer, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("customers")
                .child(customer.getIdCustomer())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
