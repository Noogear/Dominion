package cn.lunadeer.dominion.uis.beuis.cuis;

import cn.lunadeer.dominion.controllers.BukkitPlayerOperator;
import cn.lunadeer.dominion.controllers.DominionController;
import cn.lunadeer.dominion.controllers.GroupController;
import cn.lunadeer.dominion.dtos.DominionDTO;
import cn.lunadeer.minecraftpluginutils.ColorParser;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import static cn.lunadeer.dominion.uis.beuis.dominion.DominionManage.sendDominionManageMenu;

public class SetMapColor {
    public static void sendSetMapColorMenu(Player player, DominionDTO dominion, String oldName) {

        CustomForm.Builder SetMapColorMenu = CustomForm.builder()
                .title("输入卫星地图地块颜色（16进制）")
                .input("请输入：")
                .closedOrInvalidResultHandler(response -> sendDominionManageMenu(player, dominion))
                .validResultHandler(response -> {
                    String input = response.asInput();
                    if(input != null && !input.isEmpty()) {
                        input = input.replaceAll(" ","_");
                        BukkitPlayerOperator operator = BukkitPlayerOperator.create(player);
                        DominionController.setMapColor(operator, input, dominion.getName());
                        sendDominionManageMenu(player, dominion);
                    }
                });
        GeyserApi.api().sendForm(player.getUniqueId(), SetMapColorMenu);

    }
}
