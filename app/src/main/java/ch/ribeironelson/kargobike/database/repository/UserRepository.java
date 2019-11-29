package ch.ribeironelson.kargobike.database.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.database.firebase.UsersListLiveData;
import ch.ribeironelson.kargobike.database.firebase.UsersLiveData;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class UserRepository {

    private static UserRepository instance ;

    public static UserRepository getInstance(){
        if(instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<UserEntity> getUser(final String userId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId);
        return new UsersLiveData(reference);
    }



    public LiveData<List<UserEntity>> getAllUsers(){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users");
        return new UsersListLiveData(reference);
    }

    public void insert(final UserEntity userEntity, final OnAsyncEventListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users");
        String key = reference.push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(key)
                .setValue(userEntity, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final UserEntity userEntity, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userEntity.getIdUser())
                .updateChildren(userEntity.toMap(),(databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final UserEntity userEntity, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userEntity.getIdUser())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

}
