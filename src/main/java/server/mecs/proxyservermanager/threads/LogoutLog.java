package server.mecs.proxyservermanager.threads;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import server.mecs.proxyservermanager.ProxyServerManager;
import server.mecs.proxyservermanager.database.MySQLManager;
import server.mecs.proxyservermanager.utils.getDate;

public class LogoutLog extends Thread{

    ProxyServerManager plugin;
    ProxiedPlayer player;

    public LogoutLog(ProxyServerManager plugin, ProxiedPlayer player){
        this.plugin = plugin;
        this.player = player;
    }

    public void run(){
        try(MySQLManager mysql = new MySQLManager(plugin, "LogoutLog")) {
            String address = player.getAddress().getHostString();

            if (address == null) {
                plugin.getLogger().info(player.getName() + "のIPアドレスの取得に失敗");
            }

            mysql.execute("INSERT INTO logout_log (mcid,uuid,address,date) " +
                    "VALUES ('" + player.getName() + "','" + player.getUniqueId() + "','" + address + "','" + getDate.getDate() + "');");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void LogoutLog(ProxyServerManager plugin, ProxiedPlayer player) throws InterruptedException {
        LogoutLog logoutLog = new LogoutLog(plugin, player);
        logoutLog.start();
        logoutLog.join();
    }
}
