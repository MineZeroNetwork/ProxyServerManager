package server.mecs.proxyservermanager.threads;

import server.mecs.proxyservermanager.ProxyServerManager;
import server.mecs.proxyservermanager.database.MySQLManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PunishBan extends Thread{

    ProxyServerManager plugin;
    String mcid;
    String reason;

    public PunishBan(ProxyServerManager plugin, String mcid, String reason){
        this.plugin = plugin;
        this.mcid = mcid;
        this.reason = reason;
    }

    public void run(){
        try(MySQLManager mysql = new MySQLManager(plugin, "PunishBan");
            ResultSet rs = mysql.query("SELECT * FROM player_data WHERE mcid='" + mcid + "';")) {
            if (rs.next()){
                mysql.execute("UPDATE player_data SET isBanned=true, ban_reason='" + reason + "' WHERE mcid='" + mcid + "';");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void PunishBan(ProxyServerManager plugin, String mcid, String reason) throws InterruptedException {
        PunishBan punishBan = new PunishBan(plugin, mcid, reason);
        punishBan.start();
        punishBan.join();
    }
}
