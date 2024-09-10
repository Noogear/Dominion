package cn.lunadeer.dominion.uis.beuis.template;

import cn.lunadeer.dominion.dtos.PrivilegeTemplateDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;

import java.util.List;

import static cn.lunadeer.dominion.uis.beuis.Menu.sendMainMenu;

public class TemplateList {
    public static void sendTemplateListMenu(Player player) {

        List<PrivilegeTemplateDTO> templates = PrivilegeTemplateDTO.selectAll(player.getUniqueId());

        if(templates.isEmpty()) {

        }

        SimpleForm.Builder TemplateList = SimpleForm.builder()
                .title("成员权限模板列表")
                .content("请选择一个操作：")
                .button("创建一个新的成员权限模板")
                .closedOrInvalidResultHandler(response -> sendMainMenu(player));


    }
}
