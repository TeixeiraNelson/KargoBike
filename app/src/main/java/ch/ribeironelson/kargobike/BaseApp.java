package ch.ribeironelson.kargobike;

import android.app.Application;

import ch.ribeironelson.kargobike.database.repository.AccountRepository;
import ch.ribeironelson.kargobike.database.repository.ClientRepository;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BaseApp extends Application {

    public AccountRepository getAccountRepository() {
        return AccountRepository.getInstance();
    }

    public ClientRepository getClientRepository() {
        return ClientRepository.getInstance();
    }
}