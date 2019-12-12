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
import ch.ribeironelson.kargobike.database.repository.SchedulesRepository;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.util.OnAsyncEventListenerSchedule;

public class SchedulesViewModel extends AndroidViewModel {
    private Application application;
    private SchedulesRepository repository;
    private final MediatorLiveData<SchedulesEntity> observableSchedule;

    public SchedulesViewModel(@NonNull Application application,
                         final String scheduleId, SchedulesRepository repository) {
        super(application);

        this.application = application;
        this.repository = repository;

        observableSchedule = new MediatorLiveData<>();
        observableSchedule.setValue(null);

        LiveData<SchedulesEntity> shop = repository.getSchedules(scheduleId);

        observableSchedule.addSource(shop, observableSchedule::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String schedulesId;
        private final SchedulesRepository repository;

        public Factory(@NonNull Application application, String schedulesId) {
            this.application = application;
            this.schedulesId = schedulesId;
            repository = ((BaseApp) application).getSchedulesRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new SchedulesViewModel(application, schedulesId, repository);
        }
    }

    public LiveData<SchedulesEntity> getSchedule() {
        return observableSchedule;
    }

    public void createSchedule(SchedulesEntity schedule, OnAsyncEventListenerSchedule callback) {
        repository.insertSchedules(schedule, callback);
    }

    public void updateSchedule(SchedulesEntity schedule, OnAsyncEventListener callback) {
        repository.updateSchedules(schedule, callback);
    }

    public void deleteSchedule(SchedulesEntity schedule, OnAsyncEventListener callback){
        repository.deleteSchedules(schedule,callback);
    }
}
