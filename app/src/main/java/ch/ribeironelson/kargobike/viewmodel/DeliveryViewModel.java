package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.ribeironelson.kargobike.BaseApp;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;
import ch.ribeironelson.kargobike.ui.Delivery.AddDeliveryActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class DeliveryViewModel extends AndroidViewModel {
    private Application application;
    private DeliveryRepository repository;
    private final MediatorLiveData<DeliveryEntity> observableDelivery;

    public DeliveryViewModel(@NonNull Application application,
                              final String deliveryId, DeliveryRepository repository) {
        super(application);

        this.application = application;
        this.repository = repository;

        observableDelivery = new MediatorLiveData<>();
        observableDelivery.setValue(null);

        LiveData<DeliveryEntity> delivery = repository.getDelivery(deliveryId);

        observableDelivery.addSource(delivery, observableDelivery::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String deliveryId;
        private final DeliveryRepository repository;

        public Factory(@NonNull Application application, String deliveryId) {
            this.application = application;
            this.deliveryId = deliveryId;
            repository = ((BaseApp) application).getDeliveryRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new DeliveryViewModel(application, deliveryId, repository);
        }
    }

    public LiveData<DeliveryEntity> getDelivery() {
        return observableDelivery;
    }

    public void createDelivery(DeliveryEntity delivery, OnAsyncEventListener callback) {
        repository.insert(delivery, callback);
    }

    public void updateDelivery(DeliveryEntity delivery, OnAsyncEventListener callback) {
        repository.update(delivery, callback);
    }

    public void deleteDelivery(DeliveryEntity delivery, OnAsyncEventListener callback){
        repository.delete(delivery,callback);
    }
}
