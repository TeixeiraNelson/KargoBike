package ch.ribeironelson.kargobike.database.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.firebase.CheckpointListLiveData;
import ch.ribeironelson.kargobike.database.firebase.CheckpointLiveData;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class CheckpointRepository {
    private static CheckpointRepository instance ;

    public static CheckpointRepository getInstance(){
        if(instance == null) {
            synchronized (CheckpointRepository.class) {
                if (instance == null) {
                    instance = new CheckpointRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<CheckpointEntity> getCheckpoint(final String Id){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("checkpoints")
                .child(Id);
        return new CheckpointLiveData(reference);
    }



    public LiveData<List<CheckpointEntity>> getAllCheckpoints(){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("checkpoints");
        return new CheckpointListLiveData(reference);
    }

    public void insert(final CheckpointEntity checkpoint, final OnAsyncEventListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("checkpoints");
        String key = reference.push().getKey();
        checkpoint.setIdCheckpoint(key);
        FirebaseDatabase.getInstance()
                .getReference("checkpoints")
                .child(key)
                .setValue(checkpoint, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final CheckpointEntity checkpoint, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("checkpoints")
                .child(checkpoint.getIdCheckpoint())
                .updateChildren(checkpoint.toMap(),(databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final CheckpointEntity checkpoint, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("checkpoints")
                .child(checkpoint.getIdCheckpoint())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
