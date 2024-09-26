package cn.lunadeer.dominion.uis.beuis.cuis;

import cn.lunadeer.dominion.controllers.BukkitPlayerOperator;
import cn.lunadeer.dominion.controllers.GroupController;
import cn.lunadeer.dominion.dtos.DominionDTO;
import cn.lunadeer.minecraftpluginutils.ColorParser;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import static cn.lunadeer.dominion.uis.beuis.Menu.sendMainMenu;

public class CreateGroup {
    public static void sendCreateGroupMenu(Player player, DominionDTO dominion) {
        CustomForm.Builder CreateGroupMenu = CustomForm.builder()
                .title("未命名权限组")
                .input("输入要创建的权限组名称")
                .closedOrInvalidResultHandler(response -> sendMainMenu(player))
                .validResultHandler(response -> {
                    String input = response.asInput();
                    if (input != null && !input.isEmpty()) {
                        input = input.replaceAll(" ", "_");
                        BukkitPlayerOperator operator = BukkitPlayerOperator.create(player);
                        GroupController.createGroup(operator, dominion.getName(), ColorParser.getPlainText(input), input);
                        //发送权限组列表菜单，未写
                    }
                });
        GeyserApi.api().sendForm(player.getUniqueId(), CreateGroupMenu);
    }

}
