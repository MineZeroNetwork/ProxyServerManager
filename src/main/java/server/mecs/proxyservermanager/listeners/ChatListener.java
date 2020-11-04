package server.mecs.proxyservermanager.listeners;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import server.mecs.proxyservermanager.ProxyServerManager;

public class ChatListener implements Listener {

    ProxyServerManager plugin;

    @EventHandler
    public void onChat(ChatEvent e){
        if (e.isCancelled() || e.isProxyCommand())return;

        Connection player = e.getSender();

        if (!(player instanceof ProxiedPlayer))return;

        if (plugin.MuteMap.containsKey(((ProxiedPlayer) player).getUniqueId())){
            ((ProxiedPlayer) player).sendMessage(new ComponentBuilder("§cYou have been muted.").create());
            e.setCancelled(true);
        }
    }
}