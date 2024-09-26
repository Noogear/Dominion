package cn.lunadeer.dominion.uis.beuis.dominion.manage.member;

import cn.lunadeer.dominion.Cache;
import cn.lunadeer.dominion.dtos.*;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.geyser.api.GeyserApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.lunadeer.dominion.uis.beuis.dominion.DominionManage.sendDominionManageMenu;
import static cn.lunadeer.dominion.uis.beuis.dominion.manage.member.MemberSetting.sendMemberSettingMenu;

public class MemberList {
    public static void sendMemberMenu(Player player, DominionDTO dominion) {

        Map<Integer, List<PlayerDTO>> members = new HashMap<>() {{
            put(1, new ArrayList<>());
            put(2, new ArrayList<>());
            put(3, new ArrayList<>());
            put(4, new ArrayList<>());
        }};
        List<MemberDTO> privileges = MemberDTO.select(dominion.getId());
        for (MemberDTO privilege : privileges) {
            PlayerDTO p_player = PlayerDTO.select(privilege.getPlayerUUID());
            if (p_player == null) continue;
            GroupDTO group = Cache.instance.getGroup(privilege.getGroupId());
            if (group != null) {
                members.get(1).add(p_player); //groupTag
            } else if (privilege.getAdmin()) {
                members.get(2).add(p_player); //adminTag
            } else {
                if (!privilege.getFlagValue(Flag.MOVE)) {
                    members.get(3).add(p_player); //banTag
                } else {
                    members.get(4).add(p_player); //normalTag
                }
            }
        }

        SimpleForm.Builder memberMenu = SimpleForm.builder()
                .title("领地 " + dominion.getName() + " 成员列表")
                .optionalButton("管理员", !members.get(2).isEmpty())
                .optionalButton("权限组成员", !members.get(1).isEmpty())
                .optionalButton("普通成员", !members.get(4).isEmpty())
                .optionalButton("黑名单成员", !members.get(3).isEmpty())
                .closedOrInvalidResultHandler(response -> sendDominionManageMenu(player, dominion))
                .validResultHandler(response -> {
                    sendMemberListMenu(player, dominion, members.get(response.clickedButtonId()), response.clickedButton().text());
                });

        GeyserApi.api().sendForm(player.getUniqueId(), memberMenu);
    }

    public static void sendMemberListMenu(Player player, DominionDTO dominion, List<PlayerDTO> members, String tag) {
        SimpleForm.Builder memberListMenu = SimpleForm.builder()
                .title(tag)
                .validResultHandler(response -> {
                    sendMemberSettingMenu(player, dominion, members.get(response.clickedButtonId()));
                })
                .closedOrInvalidResultHandler(response -> sendMemberMenu(player, dominion));
        for (PlayerDTO p : members) {
            memberListMenu.button(p.getLastKnownName());
        }
        GeyserApi.api().sendForm(player.getUniqueId(), memberListMenu);
    }

}
