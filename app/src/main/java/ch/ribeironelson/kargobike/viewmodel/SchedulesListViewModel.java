package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;
import ch.ribeironelson.kargobike.database.repository.SchedulesRepository;

public class SchedulesListViewModel extends AndroidViewModel {
    private SchedulesRepository repository;

    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<SchedulesEntity>> observableSchedules;
    private SchedulesEntity schedule;
    String idSchedule;

    public SchedulesListViewModel(@NonNull Application application,
                                    SchedulesRepository repository) {
        super(application);

        this.repository = repository;
        this.application = application;

        observableSchedules = new MediatorLiveData<>();


        // set by default null, until we get data from the database.
        observableSchedules.setValue(null);

        LiveData<List<SchedulesEntity>> schedules = repository.getAllSchedules();

        // observe the changes of the entities from the database and forward them
        observableSchedules.addSource(schedules, observableSchedules::setValue);
    }

    public SchedulesListViewModel(@NonNull Application application,
                                    SchedulesRepository repository, String idSchedule) {
        super(application);

        this.repository = repository;
        this.application = application;
        this.idSchedule=idSchedule;

        observableSchedules = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableSchedules.setValue(null);

        LiveData<List<SchedulesEntity>> workingZones = repository.getAllSchedules();

        // observe the changes of the entities from the database and forward them
        observableSchedules.addSource(workingZones, observableSchedules::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final SchedulesRepository repository;

        private String idSchedule ="";


        public Factory(@NonNull Application application, String idSchedule) {
            this.application = application;
            repository = SchedulesRepository.getInstance();
            this.idSchedule=idSchedule;
        }
        public Factory(@NonNull Application application) {
            this.application = application;
            repository = SchedulesRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            if (idSchedule.equals("")){
                return (T) new SchedulesListViewModel(application, repository);
            } else {
                return (T) new SchedulesListViewModel(application, repository, idSchedule);
            }

        }
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<SchedulesEntity>> getSchedules() {
        return observableSchedules;
    }

    public SchedulesEntity getSchedule(){
        return schedule;
    }
}
