package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.ribeironelson.kargobike.database.entity.CustomerEntity;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.repository.CustomerRepository;
import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class CustomerListViewModel extends AndroidViewModel {
    private static final String TAG = "CustomerListViewModel";

    private CustomerRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<CustomerEntity>> observableCustomers;

    public CustomerListViewModel(@NonNull Application application, CustomerRepository beverageRepository) {
        super(application);

        repository = beverageRepository;

        observableCustomers = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableCustomers.setValue(null);

        LiveData<List<CustomerEntity>> customers = repository.getAllCustomers();

        // observe the changes of the entities from the database and forward them
        observableCustomers.addSource(customers, observableCustomers::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final CustomerRepository beverageRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            beverageRepository = CustomerRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CustomerListViewModel(application, beverageRepository);
        }
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<CustomerEntity>> getCustomers() {
        return observableCustomers;
    }
    
}
