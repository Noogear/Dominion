package cn.lunadeer.dominion.uis.beuis.dominion.manage;

import cn.lunadeer.dominion.dtos.DominionDTO;
import cn.lunadeer.dominion.dtos.Flag;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import static cn.lunadeer.dominion.uis.beuis.dominion.DominionManage.sendDominionManageMenu;

public class GuestSetting {
    public static void sendGuestSetting(Player player, DominionDTO dominion) {

        CustomForm.Builder GuestSettingMenu = CustomForm.builder()
                .title("访客权限")
                .closedOrInvalidResultHandler(response -> sendDominionManageMenu(player, dominion));
        for (Flag flag : Flag.getPrivilegeFlagsEnabled()) {
            GuestSettingMenu.toggle(flag.getFlagName(), dominion.getFlagValue(flag));
        }
        GuestSettingMenu.validResultHandler(response -> {
            int i = 0;
            for (Flag flag : Flag.getPrivilegeFlagsEnabled()) {
                if (!dominion.getFlagValue(flag).equals(response.asToggle(i))) {
                    player.performCommand("dominion set" + flag.getFlagName() + " " + (response.asToggle(i) ? "true" : "false") + " " + dominion.getName());
                }
                i++;
            }
        });
        GeyserApi.api().sendForm(player.getUniqueId(), GuestSettingMenu);
    }
}
