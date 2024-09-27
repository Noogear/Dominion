package cn.lunadeer.dominion.uis.beuis.dominion.manage;

import cn.lunadeer.dominion.dtos.DominionDTO;
import cn.lunadeer.dominion.dtos.Flag;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import java.util.List;

import static cn.lunadeer.dominion.uis.beuis.dominion.DominionManage.sendDominionManageMenu;

public class EnvSetting {
    public static void sendEnvSetting(Player player, DominionDTO dominion) {

        CustomForm.Builder EnvSettingMenu = CustomForm.builder()
                .title("环境设置")
                .closedOrInvalidResultHandler(response -> sendDominionManageMenu(player, dominion));
        List<Flag> flags = Flag.getDominionOnlyFlagsEnabled();
        for (Flag flag : flags) {
            EnvSettingMenu.toggle(flag.getFlagName(), dominion.getFlagValue(flag));
        }
        EnvSettingMenu.validResultHandler(response -> {
            int i = 0;
            for (Flag flag : flags) {
                if (!dominion.getFlagValue(flag).equals(response.asToggle(i))) {
                    player.performCommand(String.format("dominion set %s %b %s", flag.getFlagName(), response.asToggle(i), dominion.getName()));
                }
                i++;
            }
        });
        GeyserApi.api().sendForm(player.getUniqueId(), EnvSettingMenu);
    }
}
