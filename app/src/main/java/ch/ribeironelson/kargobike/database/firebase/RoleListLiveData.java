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
import ch.ribeironelson.kargobike.database.entity.ProductEntity;
import ch.ribeironelson.kargobike.database.entity.RoleEntity;

public class RoleListLiveData extends LiveData<List<RoleEntity>> {
    private static final String TAG = "RoleListLiveData";

    private final DatabaseReference reference;
    private final RoleListLiveData.MyValueEventListener listener = new RoleListLiveData.MyValueEventListener();

    public RoleListLiveData(DatabaseReference ref) {
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
            setValue(toRole(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<RoleEntity> toRole(DataSnapshot snapshot) {
        List<RoleEntity> roles = new ArrayList<>();
        Log.d(TAG,"Transforming into roles");
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            RoleEntity entity = childSnapshot.getValue(RoleEntity.class);
            entity.setRoleId(childSnapshot.getKey());
            roles.add(entity);
        }
        return roles;
    }
}

