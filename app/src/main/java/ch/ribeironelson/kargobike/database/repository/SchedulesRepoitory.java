package ch.ribeironelson.kargobike.database.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class SchedulesRepoitory {
    private static SchedulesRepoitory instance;

    private SchedulesRepoitory(){}

    public static SchedulesRepoitory getInstance(){
        if(instance == null){
            synchronized (WorkingZoneRepository.class){
                if(instance == null){
                    instance = new SchedulesRepoitory();
                }
            }
        }
        return instance;
    }

    public LiveData<SchedulesEntity> getSchedules(final String schedulesId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("schedules")
                .child(schedulesId);
        return new SchedulesLiveData(reference);
    }

    public LiveData<List<SchedulesEntity>> getAllSchedules(){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("schedules");
        return new SchedulesListeLiveData(reference);
    }

    public void insertSchedules(final SchedulesEntity schedules, final OnAsyncEventListener callback){
        String id = FirebaseDatabase.getInstance().getReference("schedules").push().getKey();
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("schedules")
                .child(id)
                .setValue(schedules, (databaseError, databaseReference) -> {
                    if(databaseError != null){
                        callback.onFailure(databaseError.toException());
                    }
                    else{
                        callback.onSuccess();
                    }
                });
    }
}

    public void updateSchedules(final SchedulesEntity schedules, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("schedules")
                .child(schedules.getId())
                .updateChildren(schedules.toMap(), (databaseError, databaseReference) -> {
                    if(databaseError != null){
                        callback.onFailure(databaseError.toException());
                    }
                    else{
                        callback.onSuccess();
                    }
                });
    }

    public void deleteSchedules(final SchedulesEntity schedules, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("schedules")
                .child(schedules.getId())
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
