package ch.ribeironelson.kargobike.util;

import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;

public interface OnAsyncEventListenerSchedule {
    void onSuccess(SchedulesEntity sch);
    void onFailure(Exception e);
}
