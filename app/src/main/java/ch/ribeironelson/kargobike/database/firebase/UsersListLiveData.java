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
import ch.ribeironelson.kargobike.database.entity.UserEntity;

public class UsersListLiveData extends LiveData<List<UserEntity>> {
    private static final String TAG = "UsersListLiveData";

    private final DatabaseReference reference;
    private final UsersListLiveData.MyValueEventListener listener = new UsersListLiveData.MyValueEventListener();

    public UsersListLiveData(DatabaseReference ref) {
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
            setValue(toUsers(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<UserEntity> toUsers(DataSnapshot snapshot) {
        List<UserEntity> users = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            UserEntity entity = childSnapshot.getValue(UserEntity.class);
            entity.setIdUser(childSnapshot.getKey());
            users.add(entity);
        }
        return users;
    }
}
