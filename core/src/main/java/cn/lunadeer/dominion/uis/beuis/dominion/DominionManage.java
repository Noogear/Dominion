package cn.lunadeer.dominion.uis.beuis.dominion;

import cn.lunadeer.dominion.Dominion;
import cn.lunadeer.dominion.dtos.DominionDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.geyser.api.GeyserApi;

public class DominionManage {
    //player.performCommand("dominion manager "+response.clickedButton().text());
    public static void sendDominionManageMenu(Player player, DominionDTO dominion) {
        SimpleForm.Builder DominionManageMenu = SimpleForm.builder()
                .title("领地管理")
                .content("领地[ " + dominion.getName() + " ]管理页面\n请选择一个操作：")
                .button("详细信息")
                .button("环境设置")
                .button("访客权限")
                .button("成员管理")
                .button("权限组")
                .button("设置传送点")
                .button("重命名")
                .button("编辑欢迎提示语")
                .button("编辑离开提示语")
                .optionalButton("设置颜色", Dominion.config.getBlueMap())
                .validResultHandler(response -> {
                    switch (response.clickedButtonId()) {
                        case 1:
                            player.performCommand("dominion info " + dominion.getName());
                            return;
                        case 2:
                            player.performCommand("dominion env_setting " + dominion.getName());
                            return;
                        case 3:
                            player.performCommand("dominion guest_setting " + dominion.getName());
                            return;
                        case 4:
                            player.performCommand("dominion member list " + dominion.getName());
                            return;
                        case 5:
                            player.performCommand("dominion group list " + dominion.getName());
                            return;
                        case 6:
                            player.performCommand("dominion set_tp_location " + dominion.getName());
                            return;
                        case 7:
                            player.performCommand("dominion dominion cui_rename " + dominion.getName());
                            return;
                        case 8:
                            player.performCommand("dominion cui_edit_join_message " + dominion.getName());
                            return;
                        case 9:
                            player.performCommand("dominion cui_edit_leave_message " + dominion.getName());
                            return;
                        case 10:
                            player.performCommand("dominion cui_set_map_color " + dominion.getName());
                            break;
                    }
                });
        GeyserApi.api().sendForm(player.getUniqueId(), DominionManageMenu);
    }
}
