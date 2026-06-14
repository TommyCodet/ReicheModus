package de.reichemodus.util;

import org.bukkit.Bukkit;

public final class MessageUtil {

    private MessageUtil() {
    }

    public static String color(String text) {

        return text.replace("&", "§");
    }

    public static void broadcast(String text) {

        Bukkit.broadcastMessage(
                color(text)
        );
    }
}
