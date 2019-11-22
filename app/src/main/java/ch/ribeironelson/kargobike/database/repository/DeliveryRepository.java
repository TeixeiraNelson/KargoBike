package ch.ribeironelson.kargobike.database.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
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

        return DeliveryListLiveData(reference);
    }

    //Get deliveries by trip

    public void insert(final DeliveryEntity delivery, OnAsyncEventListener callback){

    }



}
