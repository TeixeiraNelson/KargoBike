package ch.ribeironelson.kargobike.util;

public interface OnAsyncEventListener {
    void onSuccess();
    void onFailure(Exception e);
}
