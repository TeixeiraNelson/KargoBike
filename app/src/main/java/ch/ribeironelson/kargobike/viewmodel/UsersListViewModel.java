package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.database.repository.UserRepository;

public class UsersListViewModel extends AndroidViewModel {

    private UserRepository repository;

    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<UserEntity>> observableUsers;
    private UserEntity user;
    String idUser;

    public UsersListViewModel(@NonNull Application application,
                              UserRepository repository) {
        super(application);

        this.repository = repository;
        this.application = application;

        observableUsers = new MediatorLiveData<>();


        // set by default null, until we get data from the database.
        observableUsers.setValue(null);

        LiveData<List<UserEntity>> users = repository.getAllUsers();

        // observe the changes of the entities from the database and forward them
        observableUsers.addSource(users, observableUsers::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final UserRepository repository;


        public Factory(@NonNull Application application) {
            this.application = application;
            repository = UserRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ch.ribeironelson.kargobike.viewmodel.UsersListViewModel(application, repository);
        }
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<UserEntity>> getAllUsers() {
        return observableUsers;
    }

    public UserEntity getSchedule(){
        return user;
    }
}




