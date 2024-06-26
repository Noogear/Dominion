package cn.lunadeer.dominion.managers;

import cn.lunadeer.dominion.dtos.ServerInfoDTO;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GlobalTeleport implements PluginMessageListener {

    public static GlobalTeleport instance;

    private final JavaPlugin plugin;
    private final File infoFile;
    private ServerInfoDTO thisServerInfo;
    private Map<Integer, ServerInfoDTO> allServerInfo = new HashMap<>();

    public GlobalTeleport(JavaPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, "BungeeCord");
        this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, "BungeeCord", this);
        this.infoFile = new File(plugin.getDataFolder(), "server_info.json");
        instance = this;

        if (!infoFile.exists()) {
            ServerInfoDTO.initServerInfo();
        }
        // todo load & update info
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
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("SomeSubChannel")) {
            // 使用下文中的"返回（Response）"一节的代码进行读取
            // 数据处理
        }
    }
}
