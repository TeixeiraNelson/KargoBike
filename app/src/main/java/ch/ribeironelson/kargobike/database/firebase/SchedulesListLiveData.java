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
import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;

public class SchedulesListLiveData extends LiveData<List<SchedulesEntity>> {
    private static final String TAG = "ShopListLiveData";

    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    public SchedulesListLiveData(DatabaseReference ref) {
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
            setValue(toScheduels(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<SchedulesEntity> toScheduels(DataSnapshot snapshot) {
        List<SchedulesEntity> schedules = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            SchedulesEntity entity = childSnapshot.getValue(SchedulesEntity.class);
            entity.setScheduledId(childSnapshot.getKey());
            schedules.add(entity);
        }
        return schedules;
    }
}
