package shops;

import shops.Cmds.ShopCmd;
import shops.Utils.Utils;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public final class Shops extends JavaPlugin {

    private static Shops instance;
    private Statement statement;
    String host = "localhost", port = "3306", database = "shops", username = "root", password = "password";
    public Connection connection;

    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;


    @Override
    public void onEnable() {
        instance = this;

        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();

        try {
            openConnection();
            this.statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS shops (" +
                    "id VARCHAR(32) PRIMARY KEY," +
                    "owner VARCHAR(55) NOT NULL," +
                    "warp VARCHAR(55)," +
                    "name VARCHAR(32)," +
                    "price INT," +
                    "description VARCHAR(64)" +
                    ");");
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&c&LCould not enable due to invalid database"));
            e.printStackTrace();
        }

        getCommand("shops").setExecutor(new ShopCmd());

        saveResource("warpsGui.yml", false);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Shops getInstance() {
        return instance;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
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
