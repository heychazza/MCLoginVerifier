package me.glaremasters.usergg.events;

import me.glaremasters.usergg.Usergg;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.List;

import static me.glaremasters.usergg.util.ColorUtil.color;

public class Login implements Listener {

    private Usergg usergg;

    public Login(Usergg usergg) {
        this.usergg = usergg;
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        String uuid = event.getUniqueId().toString();
        String token = randomAlphaNumeric(10);
        String name = event.getName();
        String ip = event.getAddress().toString().substring(1).split(":")[0];

        if (usergg.getDatabaseProvider().hasToken(uuid)) {
            usergg.getDatabaseProvider().setToken(token, uuid);
        } else {
            usergg.getDatabaseProvider().insertUser(uuid, token);
        }
        StringBuilder sb = new StringBuilder();
        List<String> kickMessage = usergg.getConfig().getStringList("kick-message");
        for (String line : kickMessage) {
            sb.append(color(line).replace("{uuid}", uuid).replace("{token}", token).replace("{name}", name).replace("{ip}", ip) + "\n");
        }
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, sb.toString());
    }



    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
