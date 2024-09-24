package cn.lunadeer.dominion.uis.beuis.dominion.manage;

import cn.lunadeer.dominion.dtos.DominionDTO;
import cn.lunadeer.dominion.dtos.Flag;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import static cn.lunadeer.dominion.uis.beuis.dominion.DominionManage.sendDominionManageMenu;

public class EnvSetting {
    public static void sendEnvSetting(Player player, DominionDTO dominion) {

        CustomForm.Builder EnvSettingMenu = CustomForm.builder()
                .title("环境设置")
                .closedOrInvalidResultHandler(response -> sendDominionManageMenu(player,dominion));
        for (Flag flag : Flag.getDominionOnlyFlagsEnabled()) {
            EnvSettingMenu.toggle(flag.getFlagName(),dominion.getFlagValue(flag));
        }
        EnvSettingMenu.validResultHandler(response -> {
            int i = 0;
            for (Flag flag : Flag.getDominionOnlyFlagsEnabled()) {
                if(!dominion.getFlagValue(flag).equals(response.asToggle(i))){
                    player.performCommand("dominion set" + flag.getFlagName() + " " + (response.asToggle(i) ? "true" : "false") + " " + dominion.getName());
                }
                i++;
            }
        });
        GeyserApi.api().sendForm(player.getUniqueId(), EnvSettingMenu);
    }
}
