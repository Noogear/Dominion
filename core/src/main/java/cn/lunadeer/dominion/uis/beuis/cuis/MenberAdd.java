package cn.lunadeer.dominion.uis.beuis.cuis;

import cn.lunadeer.dominion.controllers.BukkitPlayerOperator;
import cn.lunadeer.dominion.controllers.MemberController;
import cn.lunadeer.dominion.dtos.DominionDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import static cn.lunadeer.dominion.uis.beuis.template.TemplateList.sendTemplateListMenu;

public class MenberAdd {
    public static void sendMenberAddMenu(Player player, DominionDTO dominion) {

        CustomForm.Builder MenberAddMenu = CustomForm.builder()
                .title("添加成员")
                .input("输入玩家名称以添加为成员")
                .closedOrInvalidResultHandler(response -> sendTemplateListMenu(player))
                .validResultHandler(response -> {
                    String input = response.asInput();
                    if(input != null && !input.isEmpty()) {
                        input = input.replaceAll(" ","_");
                        BukkitPlayerOperator operator = BukkitPlayerOperator.create(player);
                        //这里还缺一块代码，不知道啥作用
                        MemberController.memberAdd(operator, dominion.getName(), input);
                    }

                });
        GeyserApi.api().sendForm(player.getUniqueId(), MenberAddMenu);

    }
}
