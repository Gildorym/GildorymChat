package com.gildorymrp.chat;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.gildorymrp.api.plugin.chat.Channel;

public class QuickChannelSwitchCommand extends Command {

	private GildorymChat plugin;
	private Channel channel;
	
	public QuickChannelSwitchCommand(String name, String description, String usageMessage, List<String> aliases, GildorymChat plugin, Channel channel) {
		super(name, description, usageMessage, aliases);
		this.plugin = plugin;
		this.channel = channel;
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("gildorym.chat.ch.talkin." + this.channel.getName())) {
			Channel channel = plugin.getPlayerChannel((Player) sender);
			plugin.setPlayerChannel((Player) sender, this.channel);
			if (args.length >= 1) {
				String message = "";
				for (String arg : args) {
					message += arg + " ";
				}
				plugin.handleChat((Player) sender, message);
				plugin.setPlayerChannel((Player) sender, channel);
			} else {
				sender.sendMessage(this.channel.getColour() + "Now talking in " + this.channel.getName() + ".");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You do not have permission!");
		}
		return true;
	}

}
