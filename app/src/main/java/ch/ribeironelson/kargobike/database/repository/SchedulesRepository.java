package ch.ribeironelson.kargobike.database.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;
import ch.ribeironelson.kargobike.database.firebase.SchedulesListLiveData;
import ch.ribeironelson.kargobike.database.firebase.SchedulesLiveData;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.util.OnAsyncEventListenerSchedule;

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

    public LiveData<SchedulesEntity> getSchedules(final String schedulesId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("schedules")
                .child(schedulesId);
        return new SchedulesLiveData(reference);
    }

    public LiveData<List<SchedulesEntity>> getAllSchedules(){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("schedules");
        return new SchedulesListLiveData(reference);
    }

    public void insertSchedules(SchedulesEntity schedules, final OnAsyncEventListenerSchedule callback){
        String id = FirebaseDatabase.getInstance().getReference("schedules").push().getKey();
        schedules.setScheduledId(id);
        FirebaseDatabase.getInstance()
                .getReference("schedules")
                .child(id)
                .setValue(schedules, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess(schedules);
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