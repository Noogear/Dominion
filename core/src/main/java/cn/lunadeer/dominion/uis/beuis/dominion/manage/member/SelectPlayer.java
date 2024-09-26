package cn.lunadeer.dominion.uis.beuis.dominion.manage.member;

import cn.lunadeer.dominion.dtos.DominionDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.SimpleForm;

import static cn.lunadeer.dominion.uis.beuis.dominion.manage.member.MemberList.sendMemberMenu;

public class SelectPlayer {
    public static void sendSelectPlayerMenu(Player player, DominionDTO dominion) {
        CustomForm.Builder SelectPlayerMenu = CustomForm.builder()
                .title("选择玩家添加为成员")
                .input("搜索")
                .closedOrInvalidResultHandler(response-> sendMemberMenu(player,dominion));
    }
}
