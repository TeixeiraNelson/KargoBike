package ch.ribeironelson.kargobike.database.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;

public class UsersLiveData extends LiveData<UserEntity> {
    private static final String TAG = "UsersLiveData";

    private final DatabaseReference reference;
    private final UsersLiveData.MyValueEventListener listener = new UsersLiveData.MyValueEventListener();

    public UsersLiveData(DatabaseReference ref) {
        this.reference = ref;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserEntity entity = dataSnapshot.getValue(UserEntity.class);
            entity.setIdUser(dataSnapshot.getKey());
            setValue(entity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}


