package server.mecs.proxyservermanager.commands.discord;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import server.mecs.proxyservermanager.ProxyServerManager;
import server.mecs.proxyservermanager.threads.AccountUnSync;
import server.mecs.proxyservermanager.threads.CheckSynced;
import server.mecs.proxyservermanager.threads.getIDfromMCID;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class McToDiscord extends Command {

    ProxyServerManager plugin;

    public static Map<String, Integer> number = new HashMap<>();

    public McToDiscord(ProxyServerManager plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder("§b↓↓↓Discord Invitation Link↓↓↓").create());
            sender.sendMessage(new ComponentBuilder("§l==== https://discord.gg/FrSn838 ====").create());
            sender.sendMessage(new ComponentBuilder("§cDiscordで発言するためにはアカウントを同期する必要があります！\n" +
                    "§cYou will need to sync your account in order to speak on Discord!").create());
            return;
        }
        if (args[0].equals("sync")) {
            if (number.containsKey(sender.getName())) {
                sender.sendMessage(new ComponentBuilder("§cあなたはすでに連携を申請中です。\n§cしばらく待ってからもう一度試してください。\n" +
                        "§cYou are already in the process of requesting an account sync.\n§cPlease wait a while and try again.").create());
                sender.sendMessage(new ComponentBuilder("§c/discord cancel").create());
                return;
            }
            ProxiedPlayer player = (ProxiedPlayer) sender;
            UUID uuid = player.getUniqueId();
            if (plugin.MuteMap.containsKey(uuid)) {
                sender.sendMessage(new ComponentBuilder("§cあなたは現在Muteされているためアカウント同期をできません。\n" +
                        "§cYou are currently Mute and therefore unable to sync your account.").create());
                return;
            }
            try {
                if (CheckSynced.isSynced(plugin, sender.getName(), null)) {
                    sender.sendMessage(new ComponentBuilder("§cあなたはすでにアカウントを同期しています。\n" +
                            "§cYou have already synced your account.").create());
                    return;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            int randomNumber = (int) (Math.random() * 9999);
            if (randomNumber <= 1000) {
                randomNumber = randomNumber + 1000;
            }
            number.put(sender.getName(), randomNumber);
            sender.sendMessage(new ComponentBuilder("§aあなたの認証IDは §b" + number.get(sender.getName()) + "§a です。\n" +
                    "§aこの認証IDをDiscordのMECS Bot#2386のDMに送信してください。\n" +
                    "§aYour authentication ID is §b" + number.get(sender.getName()) + "\n" +
                    "§aPlease send this authentication ID to the direct message on MECS Bot#2386.").create());
        }

        if (args[0].equals("unsync")) {
            try {
                if (CheckSynced.isSynced(plugin, sender.getName(), null)) {
                    plugin.discord.removeRole(sender.getName());
                    plugin.discord.guild.modifyNickname(plugin.discord.guild.getMemberById(getIDfromMCID.getIDfromMCID(plugin, sender.getName())), "An_Unlinked_Player").queue();
                    try {
                        AccountUnSync.AccountUnSync(plugin, sender.getName(), null);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage(new ComponentBuilder("§aアカウント同期を解除しました。\n" +
                            "§aYour account has been unsynced.").create());
                    return;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            sender.sendMessage(new ComponentBuilder("§cあなたはアカウントを同期していません。\n" +
                    "§cYou have not synced your account.").create());
        }

        if (args[0].equals("cancel")) {
            number.remove(sender);
            sender.sendMessage(new ComponentBuilder("§aアカウント同期申請をキャンセルしました。\n" +
                    "§aCancelled your synchronization request.").create());
        }
    }
}
