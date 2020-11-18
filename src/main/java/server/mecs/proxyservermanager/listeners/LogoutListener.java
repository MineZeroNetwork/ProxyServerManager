package server.mecs.proxyservermanager.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import server.mecs.proxyservermanager.ProxyServerManager;
import server.mecs.proxyservermanager.threads.LogoutLog;

public class LogoutListener implements Listener {

    ProxyServerManager plugin;

    @EventHandler
    public void onLogout(PlayerDisconnectEvent e){
        plugin.NickMap.remove(e.getPlayer().getUniqueId());
        plugin.MuteMap.remove(e.getPlayer().getUniqueId());

        ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> LogoutLog.LogoutLog(plugin, e.getPlayer()));

        ProxyServer.getInstance().broadcast(new ComponentBuilder(e.getPlayer().getDisplayName() + " §ehas left the network.").create());
    }
}
