package me.glaremasters.usergg.database;

public interface Callback<T, E extends Exception> {

    void call(T result, E exception);
}
