package me.glaremasters.usergg.database.databases.mysql;

import com.sun.rowset.CachedRowSetImpl;
import com.zaxxer.hikari.HikariDataSource;
import me.glaremasters.usergg.Usergg;
import me.glaremasters.usergg.database.DatabaseProvider;
import org.bukkit.configuration.ConfigurationSection;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL implements DatabaseProvider {


    private HikariDataSource hikari;

    @Override
    public void initialize() {
        ConfigurationSection databaseSection = Usergg.getUsergg().getConfig().getConfigurationSection("database");

        if (databaseSection == null) {
            throw new IllegalStateException("MySQL not configured correctly. Cannot continue.");
        }

        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(databaseSection.getInt("pool-size"));

        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        hikari.addDataSourceProperty("serverName", databaseSection.getString("host"));
        hikari.addDataSourceProperty("port", databaseSection.getInt("port"));
        hikari.addDataSourceProperty("databaseName", databaseSection.getString("database"));

        hikari.addDataSourceProperty("user", databaseSection.getString("username"));
        hikari.addDataSourceProperty("password", databaseSection.getString("password"));

        hikari.addDataSourceProperty("characterEncoding", "utf8");
        hikari.addDataSourceProperty("useUnicode", "true");

        hikari.validate();

        Usergg.newChain().async(() -> execute(Query.CREATE_TABLE)).execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });
    }

    @Override
    public void insertUser(String uuid, String token) {
        Usergg.newChain().async(() -> execute(Query.INSERT_USER, uuid, token)).execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });
    }

    @Override
    public boolean hasToken(String uuid) {
        try {
            ResultSet rs = executeQuery(Query.TOKEN_CHECK, uuid);
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setToken(String token, String uuid) {
        Usergg.newChain().async(() -> execute(Query.UPDATE_TOKEN, token, uuid)).execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });
    }

    private void execute(String query, Object... parameters) {

        try (Connection connection = hikari
                .getConnection(); PreparedStatement statement = connection
                .prepareStatement(query)) {

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
            }

            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private ResultSet executeQuery(String query, Object... parameters) {
        try (Connection connection = hikari
                .getConnection(); PreparedStatement statement = connection
                .prepareStatement(query)) {
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
            }

            CachedRowSet resultCached = new CachedRowSetImpl();
            ResultSet resultSet = statement.executeQuery();

            resultCached.populate(resultSet);
            resultSet.close();

            return resultCached;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }



}
