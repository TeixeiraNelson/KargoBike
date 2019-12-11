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
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;

public class CheckpointListLiveData extends LiveData<List<CheckpointEntity>> {
    private static final String TAG = "CheckpointListLiveData";

    private final DatabaseReference reference;
    private final CheckpointListLiveData.MyValueEventListener listener = new CheckpointListLiveData.MyValueEventListener();

    public CheckpointListLiveData(DatabaseReference ref) {
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
            setValue(toCheckpoint(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<CheckpointEntity> toCheckpoint(DataSnapshot snapshot) {
        List<CheckpointEntity> checkpoints = new ArrayList<>();
        Log.d(TAG,"Transforming into deliveries");
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            CheckpointEntity entity = childSnapshot.getValue(CheckpointEntity.class);
            checkpoints.add(entity);
            Log.d(TAG,entity.getIdCheckpoint());
        }
        return checkpoints;
    }
}
