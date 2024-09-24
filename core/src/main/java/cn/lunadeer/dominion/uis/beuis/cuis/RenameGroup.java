package cn.lunadeer.dominion.uis.beuis.cuis;

import cn.lunadeer.dominion.controllers.BukkitPlayerOperator;
import cn.lunadeer.dominion.controllers.GroupController;
import cn.lunadeer.dominion.dtos.DominionDTO;
import cn.lunadeer.minecraftpluginutils.ColorParser;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import static cn.lunadeer.dominion.uis.beuis.dominion.DominionManage.sendDominionManageMenu;

public class RenameGroup {
    public static void sendRenameGroupMenu(Player player, DominionDTO dominion, String oldName) {

        CustomForm.Builder RenameGroupMenu = CustomForm.builder()
                .title("输入新的权限组名称")
                .input("请输入：")
                .closedOrInvalidResultHandler(response -> sendDominionManageMenu(player, dominion))
                .validResultHandler(response -> {
                    String input = response.asInput();
                    if(input != null && !input.isEmpty()) {
                        input = input.replaceAll(" ","_");
                        BukkitPlayerOperator operator = BukkitPlayerOperator.create(player);
                        GroupController.renameGroup(operator, dominion.getName(), oldName, ColorParser.getPlainText(input), input);
                        //发送权限组设置菜单
                    }
                });
        GeyserApi.api().sendForm(player.getUniqueId(), RenameGroupMenu);

    }
}
