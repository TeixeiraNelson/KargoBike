package ch.ribeironelson.kargobike.database.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;

public class DeliveryLiveData extends LiveData<DeliveryEntity> {
    private static final String TAG = "DeliveryLiveData";

    private final DatabaseReference reference;
    private final DeliveryLiveData.MyValueEventListener listener = new DeliveryLiveData.MyValueEventListener();

    public DeliveryLiveData(DatabaseReference ref) {
        this.reference = ref;
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
            DeliveryEntity entity = dataSnapshot.getValue(DeliveryEntity.class);
            entity.setIdDelivery(dataSnapshot.getKey());
            setValue(entity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}
