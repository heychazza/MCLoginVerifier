package me.glaremasters.usergg;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import me.glaremasters.usergg.database.DatabaseProvider;
import me.glaremasters.usergg.database.databases.mysql.MySQL;
import me.glaremasters.usergg.events.Login;
import org.bukkit.plugin.java.JavaPlugin;

public final class Usergg extends JavaPlugin {

    private static TaskChainFactory taskChainFactory;
    private static Usergg usergg;
    private DatabaseProvider database;


    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    public static Usergg getUsergg() {
        return usergg;
    }

    @Override
    public void onEnable() {
        usergg = this;
        saveDefaultConfig();
        taskChainFactory = BukkitTaskChainFactory.create(this);

        database = new MySQL();
        database.initialize();

        getServer().getPluginManager().registerEvents(new Login(this), this);
    }

    @Override
    public void onDisable() {

    }

    public DatabaseProvider getDatabaseProvider() {
        return database;
    }
}
