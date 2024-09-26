package cn.lunadeer.dominion.uis.beuis.template;

import cn.lunadeer.dominion.dtos.Flag;
import cn.lunadeer.dominion.dtos.PrivilegeTemplateDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import java.util.List;

import static cn.lunadeer.dominion.uis.beuis.template.TemplateList.sendTemplateListMenu;

public class TemplateSetting {
    public static void sendTemplateSetting(Player player, PrivilegeTemplateDTO template) {

        CustomForm.Builder templateSettingMenu = CustomForm.builder()
                .title("模板管理")
                .toggle("管理员", template.getAdmin())
                .closedOrInvalidResultHandler(response -> sendTemplateListMenu(player));
        List<Flag> flags = Flag.getPrivilegeFlagsEnabled();
        for (Flag flag : flags) {
            templateSettingMenu.toggle(flag.getDisplayName(), template.getFlagValue(flag));
        }
        templateSettingMenu.validResultHandler(response -> {
            if (template.getAdmin().equals(response.asToggle(0))) {
                player.performCommand("dominion template set_flag " + template.getName() + " admin " + (response.asToggle(0) ? "true" : "false"));
            }
            int i = 1;
            for (Flag flag : flags) {
                if (!template.getFlagValue(flag).equals(response.asToggle(i))) {
                    player.performCommand("dominion template set_flag " + template.getName() + " " + flag.getFlagName() + " " + (response.asToggle(i) ? "true" : "false"));
                }
                i++;
            }
        });
        GeyserApi.api().sendForm(player.getUniqueId(), templateSettingMenu);


    }
}
