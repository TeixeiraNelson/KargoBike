package ch.ribeironelson.kargobike.database.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class SchedulesRepository {
    private static SchedulesRepository instance;

    private SchedulesRepository(){}

    public static SchedulesRepository getInstance(){
        if(instance == null){
            synchronized (WorkingZoneRepository.class){
                if(instance == null){
                    instance = new SchedulesRepository();
                }
            }
        }
        return instance;
    }

    /*
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

     */

    public void insertSchedules(final SchedulesEntity schedules, final OnAsyncEventListener callback){
        String id = FirebaseDatabase.getInstance().getReference("schedules").push().getKey();
        FirebaseDatabase.getInstance()
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


    public void updateSchedules(final SchedulesEntity schedules, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("schedules")
                .child(schedules.getScheduledId())
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
                .child(schedules.getScheduledId())
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
