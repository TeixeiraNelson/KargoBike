package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.ribeironelson.kargobike.BaseApp;
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.repository.CheckpointRepository;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class CheckpointViewModel extends AndroidViewModel {
    private Application application;
    private CheckpointRepository repository;
    private final MediatorLiveData<CheckpointEntity> observableDelivery;

    public CheckpointViewModel(@NonNull Application application,
                             final String checkpointId, CheckpointRepository repository) {
        super(application);

        this.application = application;
        this.repository = repository;

        observableDelivery = new MediatorLiveData<>();
        observableDelivery.setValue(null);

        LiveData<CheckpointEntity> delivery = repository.getCheckpoint(checkpointId);

        observableDelivery.addSource(delivery, observableDelivery::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String checkpointId;
        private final CheckpointRepository repository;

        public Factory(@NonNull Application application, String checkpointId) {
            this.application = application;
            this.checkpointId = checkpointId;
            repository = ((BaseApp) application).getCheckpointRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CheckpointViewModel(application, checkpointId, repository);
        }
    }

    public LiveData<CheckpointEntity> getDelivery() {
        return observableDelivery;
    }

    public void createDelivery(CheckpointEntity checkpointEntity, OnAsyncEventListener callback) {
        repository.insert(checkpointEntity, callback);
    }

    public void updateDelivery(CheckpointEntity checkpointEntity, OnAsyncEventListener callback) {
        repository.update(checkpointEntity, callback);
    }

    public void deleteDelivery(CheckpointEntity checkpointEntity, OnAsyncEventListener callback){
        repository.delete(checkpointEntity,callback);
    }
}
