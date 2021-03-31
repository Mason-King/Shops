package hotels.Cmds;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import hotels.Hotels;
import hotels.Utils.HotelManager;
import hotels.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HotelCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        Hotels hotels = Hotels.getInstance();
        HotelManager hm = new HotelManager(hotels);
        if(args[0].equalsIgnoreCase("create")) {
                if(args.length == 1) {
                    p.sendMessage(Utils.chat("&7Sorry, Please specify a id."));
                } else {
                    com.sk89q.worldedit.entity.Player actor = BukkitAdapter.adapt(p);
                    SessionManager manager = WorldEdit.getInstance().getSessionManager();
                    LocalSession ls = manager.get(actor);

                    Region region = null;
                    World selectionWorld = ls.getSelectionWorld();
                    try {
                        if(selectionWorld == null) throw new IncompleteRegionException();
                        region = ls.getSelection(selectionWorld);
                    } catch (IncompleteRegionException e) {
                        actor.print(TextComponent.of("No region found"));
                    }

                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionManager rm = container.get(BukkitAdapter.adapt(p.getWorld()));
                    ProtectedCuboidRegion r = new ProtectedCuboidRegion(args[1], region.getMaximumPoint(), region.getMinimumPoint());
                    r.setFlag(Flags.BLOCK_PLACE, StateFlag.State.DENY);
                    r.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
                    r.setFlag(Flags.PVP, StateFlag.State.DENY);
                    rm.addRegion(r);
                    Location centerFloor = new Location(p.getWorld(), region.getCenter().getX(), region.getMinimumPoint().getY() + 1, region.getCenter().getZ());
                    hm.CreateHotel(args[1], centerFloor);
                    p.sendMessage(Utils.chat("&7Shop region created."));
                }
        }

        return false;
    }
}
