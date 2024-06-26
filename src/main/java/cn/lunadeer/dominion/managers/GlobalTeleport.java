package cn.lunadeer.dominion.managers;

import cn.lunadeer.dominion.dtos.ServerInfoDTO;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

public class GlobalTeleport implements PluginMessageListener {

    public static GlobalTeleport instance;

    private final JavaPlugin plugin;
    private final ServerInfoDTO thisServerInfo;
    private final Map<Integer, String> allServerInfo;

    public GlobalTeleport(JavaPlugin plugin) {
        this.plugin = plugin;
        File infoFile = new File(plugin.getDataFolder(), "server_info.json");
        this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, "BungeeCord");
        this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, "BungeeCord", this);
        instance = this;

        if (!infoFile.exists()) {
            thisServerInfo = ServerInfoDTO.initServerInfo(plugin, infoFile);
        } else {
            thisServerInfo = ServerInfoDTO.updateServerInfo(plugin, infoFile);
        }
        allServerInfo = ServerInfoDTO.getAllServerInfo();
    }

    public String getThisServerName() {
        return thisServerInfo.getName();
    }

    public String getServerName(int id) {
        return allServerInfo.get(id);
    }

    /**
     * A method that will be thrown when a PluginMessageSource sends a plugin
     * message on a registered channel.
     *
     * @param channel Channel that the message was sent through.
     * @param player  Source of the message.
     * @param message The raw message that was sent.
     */
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {

    }
}
