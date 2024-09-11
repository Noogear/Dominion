package cn.lunadeer.dominion.uis.beuis.template;

import cn.lunadeer.dominion.dtos.PrivilegeTemplateDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.geyser.api.GeyserApi;

import java.util.List;

import static cn.lunadeer.dominion.uis.beuis.Menu.sendMainMenu;
import static cn.lunadeer.dominion.uis.beuis.cuis.CreateTemplate.sendCreateTemplateMenu;

public class TemplateList {
    public static void sendTemplateListMenu(Player player) {

        List<PrivilegeTemplateDTO> templates = PrivilegeTemplateDTO.selectAll(player.getUniqueId());
        if (templates.isEmpty()) {
            player.performCommand("dominion cui_template_create");
            return;
        }

        SimpleForm.Builder TemplateListMenu = SimpleForm.builder()
                .title("成员权限模板列表")
                .content("请选择一个操作：")
                .button("创建成员权限模板")
                .closedOrInvalidResultHandler(response -> sendMainMenu(player));

        for (PrivilegeTemplateDTO template : templates) {
            TemplateListMenu.button(template.getName());
        }
        TemplateListMenu.validResultHandler(response -> {
            if (response.clickedButtonId() == 1) {
                sendCreateTemplateMenu(player);
                return;
            }
            sendTemplateControlMenu(player, response.clickedButton().text());
        });
        GeyserApi.api().sendForm(player.getUniqueId(), TemplateListMenu);
    }

    public static void sendTemplateControlMenu(Player player, String templateName) {

        SimpleForm.Builder TemplateControlMenu = SimpleForm.builder()
                .title("模板管理")
                .content(templateName)
                .button("配置")
                .button("删除")
                .closedOrInvalidResultHandler(response -> sendTemplateListMenu(player))
                .validResultHandler(response -> {
                    switch (response.clickedButtonId()) {
                        case 1:
                            player.performCommand("dominion template setting " + templateName);
                            return;
                        case 2:
                            player.performCommand("dominion template delete " + templateName);
                            break;
                    }
                });
        GeyserApi.api().sendForm(player.getUniqueId(), TemplateControlMenu);
    }
}
