package cn.lunadeer.dominion.uis.beuis.cuis;

import cn.lunadeer.dominion.controllers.BukkitPlayerOperator;
import cn.lunadeer.dominion.controllers.TemplateController;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import static cn.lunadeer.dominion.uis.beuis.template.TemplateList.sendTemplateListMenu;

public class CreateTemplate {
    public static void sendCreateTemplateMenu(Player player) {

        CustomForm.Builder CreateTemplateMenu = CustomForm.builder()
                .title("创建模板")
                .input("输入要创建的模板名称")
                .closedOrInvalidResultHandler(response -> sendTemplateListMenu(player))
                .validResultHandler(response -> {
                    String input = response.asInput();
                    if (input != null && !input.isEmpty()) {
                        input = input.replaceAll(" ", "_");
                        BukkitPlayerOperator operator = BukkitPlayerOperator.create(player);
                        TemplateController.createTemplate(operator, input.replaceAll(" ", "_"));
                        sendTemplateListMenu(player);
                    }
                });
        GeyserApi.api().sendForm(player.getUniqueId(), CreateTemplateMenu);

    }
}
