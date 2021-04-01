package shops.Gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import shops.Shops;
import shops.Utils.ShopManager;
import shops.Utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarpsGui {

    private YamlConfiguration config = Utils.loadConfig("/warpsGui.yml");
    private Gui warpsGui;

    Shops shops = Shops.getInstance();
    ShopManager sm = new ShopManager(shops);

    public Gui gui() {
        warpsGui = new Gui(Utils.chat(config.getString("title")), config.getInt("size"))
                .c();

        List<String> format = config.getStringList("format");

        Utils.makeFormat(config, warpsGui, format, "items");

        List<String> ids = sm.getIds();
        for(String s : ids) {
            if(sm.getOwner(s).equals("null")) {
                ItemStack i = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
                ItemMeta im = i.getItemMeta();
                SkullMeta skm = (SkullMeta) i.getItemMeta();
                skm.setOwningPlayer(Bukkit.getOfflinePlayer("ElMarcosFTW"));
                skm.setDisplayName(Utils.chat(shops.getConfig().getString("unclaimedName")));
                List<String> lore = new ArrayList<>();
                for(String ls : shops.getConfig().getStringList("unclaimedLore")) {
                    lore.add(Utils.chat(ls.replace("{id}", s)).replace("{price}", sm.getPrice(s)));
                }
                skm.setLore(lore);
                i.setItemMeta(skm);
                warpsGui.i(i);
            } else {
                OfflinePlayer op = (Bukkit.getOfflinePlayer(sm.getOwner(s)));
                String name = sm.getName(s);
                String description = sm.getDescription(s);
            }

        }

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
