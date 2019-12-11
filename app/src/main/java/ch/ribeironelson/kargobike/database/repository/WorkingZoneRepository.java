package ch.ribeironelson.kargobike.database.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.WorkingZoneEntity;
import ch.ribeironelson.kargobike.database.firebase.WorkingZoneListLiveData;
import ch.ribeironelson.kargobike.database.firebase.WorkingZoneLiveData;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class WorkingZoneRepository {
    private static WorkingZoneRepository instance;

    private WorkingZoneRepository(){}

    public static WorkingZoneRepository getInstance(){
        if(instance == null){
            synchronized (WorkingZoneRepository.class){
                if(instance == null){
                    instance = new WorkingZoneRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<WorkingZoneEntity> getWorkingZone(final String workingZoneId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("workingZone")
                .child(workingZoneId);
        return new WorkingZoneLiveData(reference);
    }

    public LiveData<List<WorkingZoneEntity>> getWorkingZones(){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("workingZone");
        return new WorkingZoneListLiveData(reference);
    }

    public void insertWorkingZone(final WorkingZoneEntity workingZone, final OnAsyncEventListener callback){
        String id = FirebaseDatabase.getInstance().getReference("workingZone").push().getKey();
        workingZone.setWorkingZoneId(id);
        FirebaseDatabase.getInstance()
                .getReference("workingZone")
                .child(id)
                .setValue(workingZone, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void updateWorkingZone(final WorkingZoneEntity workingZone, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("workingZone")
                .child(workingZone.getWorkingZoneId())
                .updateChildren(workingZone.toMap(), (databaseError, databaseReference) -> {
                    if(databaseError != null){
                        callback.onFailure(databaseError.toException());
                    }
                    else{
                        callback.onSuccess();
                    }
                });
    }

    public void deleteWorkingZone(final WorkingZoneEntity workingZone, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("workingZone")
                .child(workingZone.getWorkingZoneId())
                .removeValue(((databaseError, databaseReference) -> {
                    if(databaseError != null){
                        callback.onFailure(databaseError.toException());
                    }
                    else{
                        callback.onSuccess();
                    }
                }));
    }
}
