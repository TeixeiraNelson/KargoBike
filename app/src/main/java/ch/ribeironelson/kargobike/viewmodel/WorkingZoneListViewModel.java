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
import ch.ribeironelson.kargobike.database.entity.WorkingZoneEntity;
import ch.ribeironelson.kargobike.database.repository.UserRepository;
import ch.ribeironelson.kargobike.database.repository.WorkingZoneRepository;

public class WorkingZoneListViewModel extends AndroidViewModel {
    private WorkingZoneRepository repository;
    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<WorkingZoneEntity>> observableUsers;
    private WorkingZoneEntity wk;

    public WorkingZoneListViewModel(@NonNull Application application,
                              WorkingZoneRepository repository) {
        super(application);

        this.repository = repository;
        this.application = application;

        observableUsers = new MediatorLiveData<>();


        // set by default null, until we get data from the database.
        observableUsers.setValue(null);

        LiveData<List<WorkingZoneEntity>> workingZones = repository.getWorkingZones();

        // observe the changes of the entities from the database and forward them
        observableUsers.addSource(workingZones, observableUsers::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final WorkingZoneRepository repository;


        public Factory(@NonNull Application application) {
            this.application = application;
            repository = WorkingZoneRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ch.ribeironelson.kargobike.viewmodel.WorkingZoneListViewModel(application, repository);
        }
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<WorkingZoneEntity>> getAllWorkingZones() {
        return observableUsers;
    }

}
