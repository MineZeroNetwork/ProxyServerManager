package server.mecs.proxyservermanager.commands.report;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import server.mecs.proxyservermanager.ProxyServerManager;
import server.mecs.proxyservermanager.commands.staffmessage.StaffMessage;
import server.mecs.proxyservermanager.utils.getDate;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public class Report extends Command {

    public ProxyServerManager plugin;

    public static HashMap<UUID, Long> CoolTime = new HashMap<>();

    public Report(ProxyServerManager plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer))return;

        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder("").create());
            sender.sendMessage(new ComponentBuilder("§c/report <requirement>").create());
            sender.sendMessage(new ComponentBuilder("").create());
            return;
        }

        UUID uuid = ((ProxiedPlayer) sender).getUniqueId();
        if (CoolTime.containsKey(uuid)){
            if (CoolTime.get(uuid) + 1000 * 30 <= System.currentTimeMillis()){
                CoolTime.remove(uuid);
            }else{
                double timeleft = CoolTime.get(uuid) + 1000 * 30 - System.currentTimeMillis();
                int timeleftformat = (int)Math.floor(timeleft / 1000);
                sender.sendMessage(new ComponentBuilder("§cYour report cooldown has " + timeleftformat + "§c seconds left.").create());
                return;
            }
        }

        CoolTime.put(uuid, System.currentTimeMillis());

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            str.append(args[i] + " ");
        }
        String message = str.toString().trim();

        String date = getDate.getDate();

        plugin.discord.eb.setTitle("**ServerReport**", null);
        plugin.discord.eb.setColor(new Color(0, 255, 255));
        plugin.discord.eb.setDescription(date);
        plugin.discord.eb.addField("**[Description]**", "**[Sender]** `" + sender + "`\n \n`" + message + "`", false);

        plugin.discord.receivereport(plugin.discord.eb.build());

        plugin.discord.eb.clear();

        StaffMessage.sendStaffMessage(plugin, "§c§l[REPORT]\n§bReported by " + sender.getName() + ".\n§6Description: " + message);

        sender.sendMessage(new ComponentBuilder("§aレポートを送信しました。\n§aあなたのレポートを受け取り早急に対応したします。" +
                "\n§aThe report was sent.\n§aWe will take your report and respond to it as soon as possible.").create());
    }
}
