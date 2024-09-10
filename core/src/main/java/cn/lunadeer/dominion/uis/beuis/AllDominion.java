package cn.lunadeer.dominion.uis.beuis;

import cn.lunadeer.dominion.DominionNode;
import cn.lunadeer.dominion.dtos.DominionDTO;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;

import java.util.List;

public class AllDominion {

    public static void sendAllDominionMenu(Player player){

        List<DominionNode> allDominions = DominionNode.BuildNodeTree(-1, DominionDTO.selectAll());
        CheckListMenu(player,allDominions,0);

    }

}
