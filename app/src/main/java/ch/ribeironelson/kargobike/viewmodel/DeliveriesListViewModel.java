package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;
import android.util.Log;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.ribeironelson.kargobike.BaseApp;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;


public class DeliveriesListViewModel extends AndroidViewModel {
    private static final String TAG = "DeliveriesListViewModel";

    private DeliveryRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<DeliveryEntity>> mObservableDeliveries;

    public DeliveriesListViewModel(@NonNull Application application, DeliveryRepository repo) {
        super(application);

        mRepository = repo;

        mObservableDeliveries = new MediatorLiveData<>();

        // set by default null, until we get data from the database.
        mObservableDeliveries.setValue(null);


        LiveData<List<DeliveryEntity>> deliveries = mRepository.getAllDeliveries();

        // observe the changes of the entities from the database and forward them
        mObservableDeliveries.addSource(deliveries, mObservableDeliveries::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final DeliveryRepository mRepository;

        public Factory(@NonNull Application application) {
            mApplication = application;
            mRepository = ((BaseApp) application).getDeliveryRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new DeliveriesListViewModel(mApplication, mRepository);
        }
    }

    /**
     * Expose the LiveData ClientWithAccounts query so the UI can observe it.
     */
    public LiveData<List<DeliveryEntity>> getDeliveries() {
        return mObservableDeliveries;
    }

    /**
     * Expose the LiveData AccountEntities query so the UI can observe it.
     *//*

    public void deleteAccount(AccountEntity account, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getAccountRepository()
                .delete(account, callback);
    }

    public void executeTransaction(final AccountEntity sender, final AccountEntity recipient,
                                   OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getAccountRepository().transaction(sender, recipient, callback);
    }*/
}
