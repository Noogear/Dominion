package cn.lunadeer.dominion.uis.beuis.cuis;

import cn.lunadeer.dominion.controllers.BukkitPlayerOperator;
import cn.lunadeer.dominion.controllers.DominionController;
import cn.lunadeer.dominion.dtos.DominionDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import static cn.lunadeer.dominion.uis.beuis.dominion.DominionManage.sendDominionManageMenu;

public class EditLeaveMessage {
    public static void sendEditLeaveMessageMenu(Player player, DominionDTO dominion) {

        CustomForm.Builder EditLeaveMessageMenu = CustomForm.builder()
                .title("编辑离开提示语")
                .input("提示语：")
                .closedOrInvalidResultHandler(response -> sendDominionManageMenu(player, dominion))
                .validResultHandler(response -> {
                    String input = response.asInput();
                    if (input != null && !input.isEmpty()) {
                        input = input.replaceAll(" ", "_");
                        BukkitPlayerOperator operator = BukkitPlayerOperator.create(player);
                        DominionController.setLeaveMessage(operator, input, dominion.getName());
                        sendDominionManageMenu(player, dominion);
                    }
                });
        GeyserApi.api().sendForm(player.getUniqueId(), EditLeaveMessageMenu);

    }
}
