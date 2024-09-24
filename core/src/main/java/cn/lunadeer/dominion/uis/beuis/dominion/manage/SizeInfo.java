package cn.lunadeer.dominion.uis.beuis.dominion.manage;

import cn.lunadeer.dominion.controllers.PlayerController;
import cn.lunadeer.dominion.dtos.DominionDTO;
import cn.lunadeer.dominion.dtos.PlayerDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.geyser.api.GeyserApi;

import static cn.lunadeer.dominion.uis.beuis.dominion.DominionManage.sendDominionManageMenu;

public class SizeInfo {
    public static void sendSizeInfoMenu(Player player, DominionDTO dominion) {
        PlayerDTO owner = PlayerController.getPlayerDTO(dominion.getOwner());
        Integer x1 = dominion.getX1();
        Integer y1 = dominion.getY1();
        Integer z1 = dominion.getZ1();
        Integer x2 = dominion.getX2();
        Integer y2 = dominion.getY2();
        Integer z2 = dominion.getZ2();

        String context =
                "领地所有者：" + owner.getLastKnownName() + "\n" +
                "领地大小：" + dominion.getWidthX() + " x " + dominion.getHeight() + " x " + dominion.getWidthZ() + "\n" +
                "中心坐标：" + (x1 + (x2 - x1) / 2) + " " + (y1 + (y2 - y1) / 2) + " " + (z1 + (z2 - z1) / 2) + "\n" +
                "垂直高度：" + dominion.getHeight() + "\n" +
                "Y轴坐标：" + y1 + " ~ " + y2 + "\n" +
                "水平面积：" + dominion.getSquare() + "\n" +
                "领地体积：" + dominion.getVolume() + "\n" +
                "传送点坐标：" +
                (dominion.getTpLocation() == null ?
                        "无" :
                        dominion.getTpLocation().getX() + " " + dominion.getTpLocation().getY() + " " + dominion.getTpLocation().getZ());

        SimpleForm.Builder SizeInfoMenu = SimpleForm.builder()
                .title("领地 " + dominion.getName() + " 的尺寸信息")
                .content(context)
                .closedOrInvalidResultHandler(response -> sendDominionManageMenu(player, dominion));

        GeyserApi.api().sendForm(player.getUniqueId(), SizeInfoMenu);
    }
}
