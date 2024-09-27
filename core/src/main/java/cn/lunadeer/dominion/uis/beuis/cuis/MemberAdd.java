package cn.lunadeer.dominion.uis.beuis.cuis;

import cn.lunadeer.dominion.controllers.AbstractOperator;
import cn.lunadeer.dominion.controllers.BukkitPlayerOperator;
import cn.lunadeer.dominion.controllers.MemberController;
import cn.lunadeer.dominion.dtos.DominionDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import java.util.Objects;

import static cn.lunadeer.dominion.uis.beuis.dominion.manage.member.MemberList.sendMemberMenu;
import static cn.lunadeer.dominion.uis.beuis.dominion.manage.member.SelectPlayer.sendSelectPlayerMenu;
import static cn.lunadeer.dominion.uis.beuis.template.TemplateList.sendTemplateListMenu;

public class MemberAdd {
    public static void sendMemberAddMenu(Player player, DominionDTO dominion) {

        CustomForm.Builder MemberAddMenu = CustomForm.builder()
                .title("添加成员")
                .input("输入玩家名称以添加为成员")
                .closedOrInvalidResultHandler(response -> sendTemplateListMenu(player))
                .validResultHandler(response -> {
                    String input = response.asInput();
                    if (input != null && !input.isEmpty()) {
                        input = input.replaceAll(" ", "_");
                        BukkitPlayerOperator operator = BukkitPlayerOperator.create(player);

                        operator.getResponse().thenAccept(result -> {
                            if (Objects.equals(result.getStatus(), AbstractOperator.Result.SUCCESS)) {
                                sendMemberMenu(player,dominion);
                            } else {
                                sendSelectPlayerMenu(player,dominion);
                            }
                        });

                        MemberController.memberAdd(operator, dominion.getName(), input);
                    }

                });
        GeyserApi.api().sendForm(player.getUniqueId(), MemberAddMenu);

    }
}
