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
import ch.ribeironelson.kargobike.database.entity.WorkingZoneEntity;

public class WorkingZoneListLiveData extends LiveData<List<WorkingZoneEntity>> {
    private static final String TAG = "ShopListLiveData";

    private final DatabaseReference reference;
    private final WorkingZoneListLiveData.MyValueEventListener listener = new WorkingZoneListLiveData.MyValueEventListener();

    public WorkingZoneListLiveData(DatabaseReference ref) {
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
            setValue(toWorkingZones(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<WorkingZoneEntity> toWorkingZones(DataSnapshot snapshot) {
        List<WorkingZoneEntity> workingZones = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            WorkingZoneEntity entity = childSnapshot.getValue(WorkingZoneEntity.class);
            entity.setWorkingZoneId(snapshot.getKey());
            workingZones.add(entity);
        }
        return workingZones;
    }
}
