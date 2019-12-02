package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ch.ribeironelson.kargobike.BaseApp;
import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.database.repository.SchedulesRepository;
import ch.ribeironelson.kargobike.database.repository.UserRepository;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class UserViewModel extends AndroidViewModel {
    private Application application;
    private UserRepository repository;
    private final MediatorLiveData<UserEntity> observableUser;

    public UserViewModel(@NonNull Application application,
                              final String userId, UserRepository repository) {
        super(application);

        this.application = application;
        this.repository = repository;

        observableUser = new MediatorLiveData<>();
        observableUser.setValue(null);

        LiveData<UserEntity> shop = repository.getUser(userId);

        observableUser.addSource(shop, observableUser::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String userId;
        private final UserRepository repository;

        public Factory(@NonNull Application application, String userId) {
            this.application = application;
            this.userId = userId;
            repository = ((BaseApp) application).getUserRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new UserViewModel(application, userId, repository);
        }
    }

    public LiveData<UserEntity> getUser() {
        return observableUser;
    }

    public void createUser(UserEntity user, OnAsyncEventListener callback) {
        repository.insert(user, callback);
    }

    public void updateUser(UserEntity user, OnAsyncEventListener callback) {
        repository.update(user, callback);
    }

    public void deleteUser(UserEntity user, OnAsyncEventListener callback){
        repository.delete(user,callback);
    }
}
