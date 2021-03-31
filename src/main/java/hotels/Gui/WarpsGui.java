package hotels.Gui;

import hotels.Utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpsGui {

    private YamlConfiguration config = Utils.loadConfig("warpsGui.yml");
    private Gui warpsGui;

    public Gui gui() {
        warpsGui = new Gui(Utils.chat(config.getString("title")), config.getInt("size"))
                .c();

        List<String> format = config.getStringList("format");

        Utils.makeFormat(config, warpsGui, format, "items");

        warpsGui.onClick(e -> {
            Player p = (Player) e.getWhoClicked();
            int slot = e.getRawSlot();
            int prev = config.getInt("previousPage"), next = config.getInt("nextPage"), close = config.getInt("close");

            if(slot == prev) {
                warpsGui.prevPage();
            } else if(slot == next) {
                warpsGui.nextPage();
            } else if(slot == close) {
                p.closeInventory();
            }

        });

        return warpsGui;
    }

}
