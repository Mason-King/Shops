package hotels.Utils;

import hotels.Hotels;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

public class HotelManager {

    Statement statement;
    Hotels hotels;

    public HotelManager(Hotels plugin) {
        this.hotels = plugin;
        try {
            statement = hotels.connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void CreateHotel(String id, Location l) {
        String s = l.getWorld().getName() + ";" + l.getX() + ";" + l.getY() + ";" + l.getZ();
        try {
            statement.executeUpdate("INSERT INTO hotels (id, Owner, Warp) VALUES ('" + id  + "', '', '" + s + "')");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void setOwner(Player p, String id) {
        try {
            statement.executeUpdate("UPDATE hotels SET Owner= '"+ p.getUniqueId().toString() +"' WHERE id='"+id+"'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void setWarp(Location l, String id) {
        String s = l.getWorld().getName() + ";" + l.getX() + ";" + l.getY() + ";" + l.getZ();
        try {
            statement.executeUpdate("UPDATE hotels SET Warp= '"+ l +"' WHERE id='"+id+"'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Location getWarp(String id) {
        Location loc = null;
        ResultSet rs = null;
        try {
            rs = statement.executeQuery("SELECT * FROM hotels WHERE id='"+id+"'");
            if(rs.next()) {
                String locString = rs.getString("location");
                String[] split = locString.split(";");
                loc = new Location(Bukkit.getWorld(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[2]));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return loc;
    }

    public UUID getOwner(String id) {
        ResultSet rs = null;
        try {
            rs = statement.executeQuery("SELECT * FROM hotels WHERE id='"+id+"'");
            if(rs.next()) {
                return UUID.fromString(rs.getString("Owner"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


}
