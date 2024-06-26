package cn.lunadeer.dominion.dtos;

import cn.lunadeer.minecraftpluginutils.JsonFile;
import cn.lunadeer.minecraftpluginutils.XLogger;
import cn.lunadeer.minecraftpluginutils.databse.DatabaseManager;
import cn.lunadeer.minecraftpluginutils.databse.Field;
import cn.lunadeer.minecraftpluginutils.databse.FieldType;
import cn.lunadeer.minecraftpluginutils.databse.TableColumn;
import cn.lunadeer.minecraftpluginutils.databse.syntax.CreateTable;
import cn.lunadeer.minecraftpluginutils.databse.syntax.InsertRow;
import com.alibaba.fastjson.JSONObject;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.ResultSet;
import java.util.Map;

public class ServerInfoDTO {

    private final Field id = new Field("id", FieldType.INT);
    private final Field name = new Field("name", FieldType.STRING);

    public Integer getId() {
        return (Integer) id.value;
    }

    public String getName() {
        return (String) name.value;
    }


    public static ServerInfoDTO initServerInfo(JavaPlugin plugin, File se) {
        ServerInfoDTO serverInfoDTO = new ServerInfoDTO();
        serverInfoDTO.name.value = plugin.getServer().getName();
        InsertRow insertRow = new InsertRow();
        insertRow.returningAll()
                .field(serverInfoDTO.name)
                .table("server_info");
        try (ResultSet res = insertRow.execute()) {
            if (res.next()) {
                serverInfoDTO.id.value = res.getInt("id");
            }
        } catch (Exception e) {
            DatabaseManager.handleDatabaseError("创建服务器信息失败", e, "");
            return null;
        }

        JSONObject json = new JSONObject();
        json.put("name", serverInfoDTO.name.value);
        json.put("id", serverInfoDTO.id.value);
        try {
            JsonFile.saveToFile(json, se);
        } catch (Exception e) {
            XLogger.err("保存服务器信息失败: %s", e.getMessage());
            return null;
        }
        return serverInfoDTO;
    }

    public static ServerInfoDTO updateServerInfo(JavaPlugin plugin, File se) {
        try {
            JSONObject json = JsonFile.loadFromFile(se);
            ServerInfoDTO serverInfoDTO = new ServerInfoDTO();
            serverInfoDTO.id.value = json.getInteger("id");
            serverInfoDTO.name.value = json.getString("name");
            String sql = "UPDATE server_info SET name = ? WHERE id = ?";
            DatabaseManager.instance.query(sql, serverInfoDTO.name.value, serverInfoDTO.id.value);
            return serverInfoDTO;
        } catch (Exception e) {
            XLogger.err("加载服务器信息失败: %s", e.getMessage());
            return null;
        }
    }

    public static Map<Integer, String> getAllServerInfo() {
        Map<Integer, String> allServerInfo = new java.util.HashMap<>();
        String sql = "SELECT * FROM server_info";
        try (ResultSet res = DatabaseManager.instance.query(sql)) {
            while (res.next()) {
                ServerInfoDTO serverInfoDTO = new ServerInfoDTO();
                serverInfoDTO.id.value = res.getInt("id");
                serverInfoDTO.name.value = res.getString("name");
                allServerInfo.put(serverInfoDTO.getId(), serverInfoDTO.getName());
            }
        } catch (Exception e) {
            DatabaseManager.handleDatabaseError("获取服务器信息失败", e, "");
        }
        return allServerInfo;
    }

}
