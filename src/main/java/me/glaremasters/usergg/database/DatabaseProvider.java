package me.glaremasters.usergg.database;

public interface DatabaseProvider {

    void initialize();

    void insertUser(String uuid, String token);

    boolean hasToken(String uuid);

    void setToken(String token, String uuid);
}
