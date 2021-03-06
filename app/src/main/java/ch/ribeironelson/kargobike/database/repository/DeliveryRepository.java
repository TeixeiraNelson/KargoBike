package ch.ribeironelson.kargobike.database.repository;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;
import ch.ribeironelson.kargobike.database.entity.TripEntity;
import ch.ribeironelson.kargobike.database.firebase.DeliveryListLiveData;
import ch.ribeironelson.kargobike.database.firebase.DeliveryLiveData;
import ch.ribeironelson.kargobike.database.firebase.SchedulesLiveData;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class DeliveryRepository {

    private static DeliveryRepository instance ;

    public static DeliveryRepository getInstance(){
        if(instance == null) {
            synchronized (DeliveryRepository.class) {
                if (instance == null) {
                    instance = new DeliveryRepository();
                }
            }
        }
        return instance;
    }


    public LiveData<List<DeliveryEntity>> getAllDeliveries() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("deliveries");

        return new DeliveryListLiveData(reference);
    }

    public LiveData<DeliveryEntity> getDelivery(final String deliveryId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("deliveries")
                .child(deliveryId);
        return new DeliveryLiveData(reference);
    }

    /*
    public LiveData<List<DeliveryEntity>> getDeliveriesByUserId(final String userId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("deliveries");

        return DeliveryListLiveData(reference, userId);

    }
     */

    //Get deliveries by trip

    public void insert(final DeliveryEntity delivery, OnAsyncEventListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("deliveries");
        String key = reference.push().getKey();
        delivery.setIdDelivery(key);
        FirebaseDatabase.getInstance()
                .getReference("deliveries")
                .child(key)
                .setValue(delivery, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final DeliveryEntity delivery, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("deliveries")
                .child(delivery.getIdDelivery())
                .updateChildren(delivery.toMap(),(databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final DeliveryEntity delivery, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("deliveries")
                .child(delivery.getIdDelivery())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }



}
