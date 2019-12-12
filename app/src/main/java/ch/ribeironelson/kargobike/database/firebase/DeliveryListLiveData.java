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
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;

public class DeliveryListLiveData extends LiveData<List<DeliveryEntity>> {
    private static final String TAG = "DeliveryListLiveData";

    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    public DeliveryListLiveData(DatabaseReference ref) {
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
            setValue(toDeliveries(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<DeliveryEntity> toDeliveries(DataSnapshot snapshot) {
        List<DeliveryEntity> deliveries = new ArrayList<>();
        Log.d(TAG,"Transforming into deliveries");
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            DeliveryEntity entity = childSnapshot.getValue(DeliveryEntity.class);
            entity.setIdDelivery(childSnapshot.getKey());
            deliveries.add(entity);
        }
        return deliveries;
    }
}
