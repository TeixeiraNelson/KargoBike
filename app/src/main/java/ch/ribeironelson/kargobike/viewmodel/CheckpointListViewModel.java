package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.ribeironelson.kargobike.BaseApp;
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.repository.CheckpointRepository;
import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;

public class CheckpointListViewModel extends AndroidViewModel {
    private static final String TAG = "DeliveriesListViewModel";

    private CheckpointRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<CheckpointEntity>> mObservableDeliveries;

    public CheckpointListViewModel(@NonNull Application application, CheckpointRepository repo) {
        super(application);

        mRepository = repo;

        mObservableDeliveries = new MediatorLiveData<>();

        // set by default null, until we get data from the database.
        mObservableDeliveries.setValue(null);


        LiveData<List<CheckpointEntity>> deliveries = mRepository.getAllCheckpoints();

        // observe the changes of the entities from the database and forward them
        mObservableDeliveries.addSource(deliveries, mObservableDeliveries::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final CheckpointRepository mRepository;

        public Factory(@NonNull Application application) {
            mApplication = application;
            mRepository = ((BaseApp) application).getCheckpointRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CheckpointListViewModel(mApplication, mRepository);
        }
    }

    /**
     * Expose the LiveData ClientWithAccounts query so the UI can observe it.
     */
    public LiveData<List<CheckpointEntity>> getCheckpoints() {
        return mObservableDeliveries;
    }
}
