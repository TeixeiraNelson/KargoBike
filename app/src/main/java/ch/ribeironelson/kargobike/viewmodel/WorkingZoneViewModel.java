package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.ribeironelson.kargobike.BaseApp;
import ch.ribeironelson.kargobike.database.entity.WorkingZoneEntity;
import ch.ribeironelson.kargobike.database.repository.WorkingZoneRepository;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class WorkingZoneViewModel extends AndroidViewModel {
    private Application application;
    private WorkingZoneRepository repository;
    private final MediatorLiveData<WorkingZoneEntity> observableWorkingZone;

    public WorkingZoneViewModel(@NonNull Application application,
                              final String workingZoneId, WorkingZoneRepository repository) {
        super(application);

        this.application = application;
        this.repository = repository;

        observableWorkingZone = new MediatorLiveData<>();
        observableWorkingZone.setValue(null);

        LiveData<WorkingZoneEntity> workingZone = repository.getWorkingZone(workingZoneId);

        observableWorkingZone.addSource(workingZone, observableWorkingZone::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String workingZoneId;
        private final WorkingZoneRepository repository;

        public Factory(@NonNull Application application, String workingZoneId) {
            this.application = application;
            this.workingZoneId = workingZoneId;
            repository = ((BaseApp) application).getWorkingZoneRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new WorkingZoneViewModel(application, workingZoneId, repository);
        }
    }

    public LiveData<WorkingZoneEntity> getWorkingZone() {
        return observableWorkingZone;
    }

    public void createWorkingZone(WorkingZoneEntity workingZone, OnAsyncEventListener callback) {
        repository.insertWorkingZone(workingZone, callback);
    }

    public void updateWorkingZone(WorkingZoneEntity workingZone, OnAsyncEventListener callback) {
        repository.updateWorkingZone(workingZone, callback);
    }

    public void deleteWorkingZone(WorkingZoneEntity workingZone, OnAsyncEventListener callback){
        repository.deleteWorkingZone(workingZone,callback);
    }
}
