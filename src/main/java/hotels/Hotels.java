package hotels;

import hotels.Cmds.HotelCmd;
import hotels.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public final class Hotels extends JavaPlugin {

    private static Hotels instance;
    private Statement statement;
    String host = "localhost", port = "3306", database = "Hotels", username = "root", password = "password";
    public Connection connection;

    @Override
    public void onEnable() {
        instance = this;

        try {
            openConnection();
            this.statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS HOTELS (" +
                    "id VARCHAR(32) PRIMARY KEY," +
                    "Owner VARCHAR(55) NOT NULL," +
                    "warp VARCHAR(55)" +
                    ");");
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&c&LCould not enable due to invalid database"));
        }

        getCommand("hotels").setExecutor(new HotelCmd());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Hotels getInstance() {
        return instance;
    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if(connection != null && !connection.isClosed()) {
            return;
        }
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"
                        + this.host + ":" + this.port + "/" + this.database,
                this.username, this.password);
    }

}
