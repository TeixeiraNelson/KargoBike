package ch.ribeironelson.kargobike;

import android.app.Application;

import ch.ribeironelson.kargobike.database.repository.CheckpointRepository;
import ch.ribeironelson.kargobike.database.repository.CustomerRepository;
import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;
import ch.ribeironelson.kargobike.database.repository.ProductRepository;
import ch.ribeironelson.kargobike.database.repository.SchedulesRepository;
import ch.ribeironelson.kargobike.database.repository.UserRepository;
import ch.ribeironelson.kargobike.database.repository.WorkingZoneRepository;


/**
 * Android Application class. Used for accessing singletons.
 */
public class BaseApp extends Application {
    public CheckpointRepository getCheckpointRepository(){
        return CheckpointRepository.getInstance();
    }

    public SchedulesRepository getSchedulesRepository() {
        return SchedulesRepository.getInstance();
    }

    public DeliveryRepository getDeliveryRepository() {
        return DeliveryRepository.getInstance();
    }

    public WorkingZoneRepository getWorkingZoneRepository() {
        return WorkingZoneRepository.getInstance();
    }
    public UserRepository getUserRepository() {
        return UserRepository.getInstance();
    }

    public ProductRepository getProductRepository() {
        return ProductRepository.getInstance();
    }

    public CustomerRepository getCustomerRepository() {
        return CustomerRepository.getInstance();
    }
}