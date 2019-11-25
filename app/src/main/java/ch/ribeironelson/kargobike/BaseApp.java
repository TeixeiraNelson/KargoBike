package ch.ribeironelson.kargobike;

import android.app.Application;

import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;
import ch.ribeironelson.kargobike.database.repository.SchedulesRepository;
import ch.ribeironelson.kargobike.database.repository.WorkingZoneRepository;


/**
 * Android Application class. Used for accessing singletons.
 */
public class BaseApp extends Application {
    public SchedulesRepository getSchedulesRepository() {
        return SchedulesRepository.getInstance();
    }

    public WorkingZoneRepository getWorkingZoneRepository() {
        return WorkingZoneRepository.getInstance();
    }
}