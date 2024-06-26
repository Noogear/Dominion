package cn.lunadeer.dominion.managers;

import cn.lunadeer.dominion.Dominion;
import cn.lunadeer.dominion.commands.DominionOperate;
import cn.lunadeer.dominion.dtos.DominionDTO;
import cn.lunadeer.dominion.dtos.ServerInfoDTO;
import cn.lunadeer.minecraftpluginutils.Notification;
import cn.lunadeer.minecraftpluginutils.Teleport;
import cn.lunadeer.minecraftpluginutils.XLogger;
import cn.lunadeer.minecraftpluginutils.databse.DatabaseManager;
import cn.lunadeer.minecraftpluginutils.databse.Field;
import cn.lunadeer.minecraftpluginutils.databse.syntax.InsertRow;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.io.File;
import java.sql.ResultSet;
import java.util.Map;

import static cn.lunadeer.dominion.DominionNode.isInDominion;

public class GlobalTeleport implements PluginMessageListener, Listener {

    public static GlobalTeleport instance;

    private final JavaPlugin plugin;
    private final ServerInfoDTO thisServerInfo;
    private final Map<Integer, String> allServerInfo;

    public GlobalTeleport(JavaPlugin plugin) {
        this.plugin = plugin;
        File infoFile = new File(plugin.getDataFolder(), "server_info.json");
        this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, "BungeeCord");
        this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, "BungeeCord", this);
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
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

    public int getThisServerId() {
        return thisServerInfo.getId();
    }

    public String getServerName(int id) {
        return allServerInfo.get(id);
    }

    public Map<Integer, String> getAllServerInfo() {
        return allServerInfo;
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

    private void teleportToServer(Player player, DominionDTO dominionDTO) {
        Field player_uuid = new Field("player_uuid", player.getUniqueId().toString());
        Field dom_id = new Field("dom_id", dominionDTO.getId());
        InsertRow addCache = new InsertRow();
        addCache.field(player_uuid)
                .field(dom_id)
                .onConflictOverwrite(player_uuid)
                .table("bc_tp_cache");
        addCache.execute();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(dominionDTO.getServerName());
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String sql = "SELECT dom_id FROM bc_tp_cache WHERE player_uuid = ?";
        int dominionId;
        try (ResultSet res = DatabaseManager.instance.query(sql, player.getUniqueId().toString())) {
            if (res.next()) {
                dominionId = res.getInt("dom_id");
            } else {
                XLogger.debug("玩家 %s 没有传送缓存", player.getName());
                return;
            }
        } catch (Exception e) {
            DatabaseManager.handleDatabaseError("获取玩家的传送缓存失败", e, sql);
            return;
        }
        DominionDTO dominionDTO = DominionDTO.select(dominionId);
        if (dominionDTO == null) {
            Notification.error(player, "无法获取目标领地信息");
        } else {
            if (dominionDTO.getServerId() == getThisServerId()) {
                doTp(player, dominionDTO);
            }
        }
        sql = "DELETE FROM bc_tp_cache WHERE player_uuid = ?";
        DatabaseManager.instance.query(sql, player.getUniqueId().toString());
    }

    public static void doTp(@NotNull Player player, @NotNull DominionDTO dominionDTO) {
        if (dominionDTO.getServerId() != GlobalTeleport.instance.getThisServerId()) {
            GlobalTeleport.instance.teleportToServer(player, dominionDTO);
            return;
        }

        Location location = dominionDTO.getTpLocation();
        int center_x = (dominionDTO.getX1() + dominionDTO.getX2()) / 2;
        int center_z = (dominionDTO.getZ1() + dominionDTO.getZ2()) / 2;
        World world = Dominion.instance.getServer().getWorld(dominionDTO.getWorld());
        if (location == null) {
            location = new Location(world, center_x, player.getLocation().getY(), center_z);
            XLogger.warn("领地 %s 没有设置传送点，将尝试传送到中心点", dominionDTO.getName());
        } else if (!isInDominion(dominionDTO, location)) {
            location = new Location(world, center_x, player.getLocation().getY(), center_z);
            XLogger.warn("领地 %s 传送点不在领地内，将尝试传送到中心点", dominionDTO.getName());
        }
        if (player.isOnline()) {
            Teleport.doTeleportSafely(player, location).thenAccept(b -> {
                if (b) {
                    Notification.info(player, "已将你传送到 " + dominionDTO.getName());
                } else {
                    Notification.error(player, "传送失败，请重试");
                }
            });
        }
    }
}
