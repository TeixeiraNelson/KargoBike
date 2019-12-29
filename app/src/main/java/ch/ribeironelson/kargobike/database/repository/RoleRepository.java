package ch.ribeironelson.kargobike.database.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.ribeironelson.kargobike.database.entity.RoleEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.database.firebase.RoleListLiveData;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class RoleRepository {

    private static RoleRepository instance ;

    public static RoleRepository getInstance(){
        if(instance == null) {
            synchronized (RoleRepository.class) {
                if (instance == null) {
                    instance = new RoleRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<List<RoleEntity>> getAllRoles() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("roles");

        return new RoleListLiveData(reference);
    }



/*    public LiveData<RoleEntity> getRoleByUserId(final UserEntity user){

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.getIdUser())
                .child(user.getIdRole());

        return new RoleLiveData(reference);
    }

     */

    public void insert(final RoleEntity role, OnAsyncEventListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("roles");
        String key = reference.push().getKey();
        role.setRoleId(key);
        FirebaseDatabase.getInstance()
                .getReference("roles")
                .child(role.getRoleId())
                .setValue(role, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final RoleEntity role, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("roles")
                .child(role.getRoleId())
                .updateChildren(role.toMap(),(databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final RoleEntity role, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("roles")
                .child(role.getRoleId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
