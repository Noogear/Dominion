package cn.lunadeer.dominion.uis.beuis.dominion.manage.member;

import cn.lunadeer.dominion.Cache;
import cn.lunadeer.dominion.dtos.DominionDTO;
import cn.lunadeer.dominion.dtos.GroupDTO;
import cn.lunadeer.dominion.dtos.MemberDTO;
import cn.lunadeer.dominion.dtos.PlayerDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;

import java.util.List;

import static cn.lunadeer.dominion.uis.beuis.dominion.DominionManage.sendDominionManageMenu;

public class MemberList {
    public static void sendMemberListMenu(Player player, DominionDTO dominion) {

        SimpleForm.Builder memberListMenu = SimpleForm.builder()
                .title("领地 " + dominion.getName() + " 成员列表")
                .closedOrInvalidResultHandler(response -> sendDominionManageMenu(player,dominion));

        List<MemberDTO> privileges = MemberDTO.select(dominion.getId());
        for (MemberDTO privilege : privileges) {
            PlayerDTO p_player = PlayerDTO.select(privilege.getPlayerUUID());
            if (p_player == null) continue;

        }
    }

}
