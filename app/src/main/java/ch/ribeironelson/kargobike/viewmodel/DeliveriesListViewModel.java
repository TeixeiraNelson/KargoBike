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
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;


public class DeliveriesListViewModel extends AndroidViewModel {
    private static final String TAG = "DeliveriesListViewModel";

    private DeliveryRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<DeliveryEntity>> observableProducts;

    public DeliveriesListViewModel(@NonNull Application application, DeliveryRepository beverageRepository) {
        super(application);

        repository = beverageRepository;

        observableProducts = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableProducts.setValue(null);

        LiveData<List<DeliveryEntity>> products = repository.getAllDeliveries();

        // observe the changes of the entities from the database and forward them
        observableProducts.addSource(products, observableProducts::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final DeliveryRepository beverageRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            beverageRepository = DeliveryRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new DeliveriesListViewModel(application, beverageRepository);
        }
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<DeliveryEntity>> getDeliveries() {
        return observableProducts;
    }

    public void deleteDelivery(DeliveryEntity beverage) {
        repository.delete(beverage, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onFailure(Exception e) {}
        });
    }
}
