package cn.lunadeer.dominion.uis.beuis.cuis;

import cn.lunadeer.dominion.controllers.AbstractOperator;
import cn.lunadeer.dominion.controllers.BukkitPlayerOperator;
import cn.lunadeer.dominion.controllers.DominionController;
import cn.lunadeer.dominion.dtos.DominionDTO;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import java.util.Map;
import java.util.Objects;

import static cn.lunadeer.dominion.uis.beuis.Menu.sendMainMenu;
import static cn.lunadeer.dominion.uis.beuis.dominion.DominionManage.sendDominionManageMenu;
import static cn.lunadeer.dominion.utils.CommandUtils.autoPoints;
import static cn.lunadeer.dominion.utils.TuiUtils.getDominionNameArg_1;

public class CreateDominion {
    public static void sendCreateDominionMenu(Player player) {

        CustomForm.Builder CreateDominionMenu = CustomForm.builder()
                .title("未命名领地")
                .input("输入要创建的领地名称")
                .closedOrInvalidResultHandler(response -> sendMainMenu(player))
                .validResultHandler(response -> {
                    String input = response.asInput();
                    if (input != null && !input.isEmpty()) {
                        input = input.replaceAll(" ", "_");
                        BukkitPlayerOperator operator = BukkitPlayerOperator.create(player);
                        Map<Integer, Location> points = autoPoints(player);
                        operator.getResponse().thenAccept(result -> {
                            if (Objects.equals(result.getStatus(), AbstractOperator.Result.SUCCESS)) {
                                DominionDTO dominion = getDominionNameArg_1(player, new String[]{"list"});
                                sendDominionManageMenu(player, dominion);
                            }
                        });
                        DominionController.create(operator, input, points.get(0), points.get(1));
                    }

                });
        GeyserApi.api().sendForm(player.getUniqueId(), CreateDominionMenu);

    }
}
