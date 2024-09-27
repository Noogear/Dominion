package cn.lunadeer.dominion.uis.beuis.dominion.manage.member;

import cn.lunadeer.dominion.dtos.DominionDTO;
import cn.lunadeer.dominion.dtos.Flag;
import cn.lunadeer.dominion.dtos.MemberDTO;
import cn.lunadeer.dominion.dtos.PlayerDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;

import java.util.List;

import static cn.lunadeer.dominion.uis.beuis.dominion.manage.member.MemberList.sendMemberMenu;

public class MemberSetting {
    public static void sendMemberSettingMenu(Player player, DominionDTO dominion, PlayerDTO playerDTO) {

        CustomForm.Builder MemberSettingMenu = CustomForm.builder()
                .title("玩家 " + playerDTO.getLastKnownName() + " 在领地 " + dominion.getName() + " 的权限设置")
                .closedOrInvalidResultHandler(response -> sendMemberMenu(player, dominion));

        MemberDTO privilege = MemberDTO.select(playerDTO.getUuid(), dominion.getId());
        List<Flag> flags = Flag.getPrivilegeFlagsEnabled();

        if (dominion.getOwner().equals(player.getUniqueId())) {
            MemberSettingMenu.toggle("管理员", privilege.getAdmin());
            for (Flag flag : flags) {
                MemberSettingMenu.toggle(flag.getFlagName(), privilege.getFlagValue(flag));
            }
            MemberSettingMenu.validResultHandler(response -> {
                if (response.asToggle(0)) {
                    if (!privilege.getAdmin()) {
                        player.performCommand(String.format("dominion member set_flag %s %s admin true", dominion.getName(), playerDTO.getLastKnownName()));
                    }
                    sendMemberMenu(player, dominion);
                    return;
                } else {
                    if (privilege.getAdmin()) {
                        player.performCommand(String.format("dominion member set_flag %s %s admin false", dominion.getName(), playerDTO.getLastKnownName()));
                    }
                }
                int i = 1;
                for (final Flag flag : flags) {
                    if (!privilege.getFlagValue(flag).equals(response.asToggle(i))) {
                        player.performCommand(String.format("dominion member set_flag %s %s %s %b", dominion.getName(), playerDTO.getLastKnownName(), flag.getFlagName(), response.asToggle(i)));
                    }
                    i++;
                }
                sendMemberMenu(player, dominion);
            });
        } else {
            for (Flag flag : flags) {
                MemberSettingMenu.toggle(flag.getFlagName(), privilege.getFlagValue(flag));
            }
            MemberSettingMenu.validResultHandler(response -> {
                int i = 0;
                for (Flag flag : flags) {
                    if (!privilege.getFlagValue(flag).equals(response.asToggle(i))) {
                        player.performCommand(String.format("dominion member set_flag %s %s %s %b", dominion.getName(), playerDTO.getLastKnownName(), flag.getFlagName(), response.asToggle(i)));
                    }
                    i++;
                }
                sendMemberMenu(player, dominion);
            });
        }

        GeyserApi.api().sendForm(player.getUniqueId(), MemberSettingMenu);
    }
}
