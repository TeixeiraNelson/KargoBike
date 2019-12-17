package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.ribeironelson.kargobike.BaseApp;
import ch.ribeironelson.kargobike.database.entity.CustomerEntity;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.repository.CustomerRepository;
import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class CustomerViewModel extends AndroidViewModel {
    private Application application;
    private CustomerRepository repository;
    private final MediatorLiveData<CustomerEntity> observableCustomer;

    public CustomerViewModel(@NonNull Application application,
                             final String customerId, CustomerRepository repository) {
        super(application);

        this.application = application;
        this.repository = repository;

        observableCustomer = new MediatorLiveData<>();
        observableCustomer.setValue(null);

        LiveData<CustomerEntity> customer = repository.getCustomer(customerId);

        observableCustomer.addSource(customer, observableCustomer::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String customerId;
        private final CustomerRepository repository;

        public Factory(@NonNull Application application, String customerId) {
            this.application = application;
            this.customerId = customerId;
            repository = ((BaseApp) application).getCustomerRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CustomerViewModel(application, customerId, repository);
        }
    }

    public LiveData<CustomerEntity> getCustomer() {
        return observableCustomer;
    }

    public void createCustomer(CustomerEntity customer, OnAsyncEventListener callback) {
        repository.insertCustomer(customer, callback);
    }

    public void updateCustomer(CustomerEntity customer, OnAsyncEventListener callback) {
        repository.updateCustomer(customer, callback);
    }

    public void deleteCustomer(CustomerEntity customer, OnAsyncEventListener callback){
        repository.deleteCustomer(customer,callback);
    }
}
