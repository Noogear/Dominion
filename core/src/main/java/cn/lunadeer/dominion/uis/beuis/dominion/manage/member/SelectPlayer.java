package cn.lunadeer.dominion.uis.beuis.dominion.manage.member;

import cn.lunadeer.dominion.dtos.DominionDTO;
import cn.lunadeer.dominion.dtos.MemberDTO;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.geyser.api.GeyserApi;

import static cn.lunadeer.dominion.uis.beuis.cuis.MemberAdd.sendMemberAddMenu;
import static cn.lunadeer.dominion.uis.beuis.dominion.manage.member.MemberList.sendMemberMenu;

public class SelectPlayer {
    public static void sendSelectPlayerMenu(Player player, DominionDTO dominion) {
        SimpleForm.Builder SelectPlayerMenu = SimpleForm.builder()
                .title("选择玩家添加为成员")
                .button("搜索")
                .button("选择在线玩家")
                .closedOrInvalidResultHandler(response -> sendMemberMenu(player, dominion))
                .validResultHandler(response -> {
                    if (response.clickedButtonId() == 1) {
                        sendMemberAddMenu(player, dominion);
                    } else {
                        sendOnlinePlayerMenu(player, dominion);
                    }
                });
        GeyserApi.api().sendForm(player.getUniqueId(), SelectPlayerMenu);
    }

    private static void sendOnlinePlayerMenu(Player player, DominionDTO dominion) {
        SimpleForm.Builder OnlinePlayerMenu = SimpleForm.builder()
                .title("选择玩家添加为成员")
                .content("选择在线玩家")
                .closedOrInvalidResultHandler(response -> sendSelectPlayerMenu(player, dominion));
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (MemberDTO.select(p.getUniqueId(), dominion.getId()) != null) continue;
            OnlinePlayerMenu.button(p.getName());
        }
        OnlinePlayerMenu.validResultHandler(response -> {
            player.performCommand("dominion member add " + dominion.getName() + " " + response.clickedButton().text());
            GeyserApi.api().sendForm(player.getUniqueId(), OnlinePlayerMenu);
        });
        GeyserApi.api().sendForm(player.getUniqueId(), OnlinePlayerMenu);
    }
}
