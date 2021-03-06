package ch.ribeironelson.kargobike.database.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.CustomerEntity;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;

public class CustomerListLiveData extends LiveData<List<CustomerEntity>> {
    private static final String TAG = "DeliveryListLiveData";

    private final DatabaseReference reference;
    private final CustomerListLiveData.MyValueEventListener listener = new CustomerListLiveData.MyValueEventListener();

    public CustomerListLiveData(DatabaseReference ref) {
        reference = ref;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toCustomers(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<CustomerEntity> toCustomers(DataSnapshot snapshot) {
        List<CustomerEntity> customers = new ArrayList<>();
        Log.d(TAG,"Transforming into customers");
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            CustomerEntity entity = childSnapshot.getValue(CustomerEntity.class);
            entity.setIdCustomer(childSnapshot.getKey());
            customers.add(entity);
        }
        return customers;
    }
}
