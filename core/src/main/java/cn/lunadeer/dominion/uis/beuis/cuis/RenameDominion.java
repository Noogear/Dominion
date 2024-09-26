package cn.lunadeer.dominion.uis.beuis.cuis;

import cn.lunadeer.dominion.controllers.BukkitPlayerOperator;
import cn.lunadeer.dominion.controllers.DominionController;
import cn.lunadeer.dominion.dtos.DominionDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import static cn.lunadeer.dominion.uis.beuis.dominion.DominionManage.sendDominionManageMenu;
import static cn.lunadeer.dominion.utils.TuiUtils.getDominionNameArg_1;

public class RenameDominion {
    public static void sendRenameDominionMenu(Player player, DominionDTO dominion) {

        CustomForm.Builder RenameDominionMenu = CustomForm.builder()
                .title("领地重命名")
                .input("请输入：")
                .closedOrInvalidResultHandler(response -> sendDominionManageMenu(player, dominion))
                .validResultHandler(response -> {
                    String input = response.asInput();
                    if (input != null && !input.isEmpty()) {
                        input = input.replaceAll(" ", "_");
                        BukkitPlayerOperator operator = BukkitPlayerOperator.create(player);
                        DominionController.rename(operator, dominion.getName(), input);
                        sendDominionManageMenu(player, getDominionNameArg_1(player, new String[]{"manage", input}));
                    }
                });
        GeyserApi.api().sendForm(player.getUniqueId(), RenameDominionMenu);

    }
}
