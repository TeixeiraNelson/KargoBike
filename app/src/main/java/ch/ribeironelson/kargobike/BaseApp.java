package ch.ribeironelson.kargobike;

import android.app.Application;

import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;


/**
 * Android Application class. Used for accessing singletons.
 */
public class BaseApp extends Application {

    public DeliveryRepository getDeliveryRepository() {
        return DeliveryRepository.getInstance();
    }

}