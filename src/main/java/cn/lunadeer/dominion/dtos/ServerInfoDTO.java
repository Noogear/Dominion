package cn.lunadeer.dominion.dtos;

import cn.lunadeer.minecraftpluginutils.databse.Field;
import cn.lunadeer.minecraftpluginutils.databse.FieldType;
import cn.lunadeer.minecraftpluginutils.databse.TableColumn;
import cn.lunadeer.minecraftpluginutils.databse.syntax.CreateTable;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ServerInfoDTO {

    private final Field id = new Field("id", FieldType.INT);
    private final Field name = new Field("name", FieldType.STRING);
    private final Field uuid = new Field("uuid", FieldType.STRING);

    private void initTable() {
        TableColumn server_info_id = new TableColumn("id", FieldType.INT, true, true, true, true, 0);
        TableColumn server_info_name = new TableColumn("name", FieldType.STRING, false, false, true, true, "server");
        TableColumn server_info_uuid = new TableColumn("uuid", FieldType.STRING, false, false, true, true, "00000000-0000-0000-0000-000000000000");
        CreateTable privilege_template = new CreateTable().ifNotExists();
        privilege_template.table("server_info")
                .field(server_info_id)
                .field(server_info_name)
                .field(server_info_uuid);
        privilege_template.execute();
    }


    public static void initServerInfo() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");

        JSONObject json = new JSONObject();

    }

}
