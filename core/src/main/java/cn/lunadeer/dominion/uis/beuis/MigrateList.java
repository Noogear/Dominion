package cn.lunadeer.dominion.uis.beuis;

import cn.lunadeer.dominion.Cache;
import cn.lunadeer.dominion.utils.ResMigration;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.geyser.api.GeyserApi;

import java.util.List;

import static cn.lunadeer.dominion.uis.beuis.Menu.sendMainMenu;

public class MigrateList {
    public static void sendMigrateListMenu(Player player) {

        StringBuilder content = new StringBuilder();
        List<ResMigration.ResidenceNode> res_data = Cache.instance.getResidenceData(player.getUniqueId());
        SimpleForm.Builder MigrateListMenu = SimpleForm.builder()
                .title("从 Residence 迁移数据")
                .closedOrInvalidResultHandler(response -> {
                    sendMainMenu(player);
                });

        if (res_data == null) {
            MigrateListMenu.content(content.append("你没有可迁移的数据！").toString());
            GeyserApi.api().sendForm(player.getUniqueId(), MigrateListMenu);
            return;
        }
        content.append("您有").append(res_data.size()).append("个领地可以迁移\n")
                .append("请选择您要迁移的领地：");
        MigrateListMenu.content(content.toString());

        for (ResMigration.ResidenceNode node : res_data) {
            MigrateListMenu.button(node.name);
        }
        MigrateListMenu.validResultHandler(response -> {
            player.performCommand("dominion migrate " + response.clickedButton().text());
        });

        GeyserApi.api().sendForm(player.getUniqueId(), MigrateListMenu);
    }
}
