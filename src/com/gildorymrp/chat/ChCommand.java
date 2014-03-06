package com.gildorymrp.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gildorymrp.api.plugin.chat.Channel;

public class ChCommand implements CommandExecutor {

	private GildorymChat plugin;
	
	public ChCommand(GildorymChat plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ch")) {
			if (args.length >= 2) {
				if (args[0].equalsIgnoreCase("talkin")) {
					if (plugin.getChannel(args[1]) != null) {
						if (sender.hasPermission("gildorym.chat.ch.talkin." + args[1].toLowerCase())) {
							plugin.setPlayerChannel((Player) sender, plugin.getChannel(args[1].toLowerCase()));
							sender.sendMessage(ChatColor.GREEN + "You are now talking in " + args[1].toLowerCase() + "!");
						} else {
							sender.sendMessage(ChatColor.RED + "You don't have permission to talk in that channel!");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "No channel by that name exists! Use /ch list for channels.");
					}
				} else if (args[0].equalsIgnoreCase("listen")) {
					if (plugin.getChannel(args[1]) != null) {
						if (sender.hasPermission("gildorym.chat.ch.listen." + args[1].toLowerCase())) {
							if (!plugin.getChannel(args[1]).getListeners().contains(sender.getName())) {
								plugin.getChannel(args[1]).addListener((Player) sender);
								sender.sendMessage(ChatColor.GREEN + "You are now listening to " + args[1].toLowerCase() + "!");
							} else {
								sender.sendMessage(ChatColor.RED + "You are already listening to that channel!");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "You do not have permission to listen to that channel!");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "No channel by that name exists! Use /ch list for channels.");
					}
				} else if (args[0].equalsIgnoreCase("mute")) {
					if (plugin.getChannel(args[1]) != null) {
						if (sender.hasPermission("gildorym.chat.ch.mute." + args[1].toLowerCase())) {
							if (plugin.getChannel(args[1]).getListeners().contains((Player) sender)) {
								plugin.getChannel(args[1]).removeListener((Player) sender);
								sender.sendMessage(ChatColor.GREEN + "You are no longer listening to " + args[1].toLowerCase() + "!");
							} else {
								sender.sendMessage(ChatColor.RED + "You are not listening to that channel!");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "You do not have permission to mute that channel!");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "No channel by that name exists! Use /ch list for channels.");
					}
				}
			} else {
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("list")) {
						if (sender.hasPermission("gildorym.chat.ch.list")) {
							sender.sendMessage(ChatColor.GREEN + "Channel list: ");
							for (Channel channel : plugin.getChannels()) {
								sender.sendMessage(channel.getColour() + channel.getName());
								sender.sendMessage(channel.getColour() + " - Format: " + channel.getFormat());
								sender.sendMessage(channel.getColour() + " - Radius: " + channel.getRadius());
							}
						} else {
							sender.sendMessage(ChatColor.RED + "You do not have permission to list channels!");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Incorrect usage! Use /chathelp for help.");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Incorrect usage! Use /chathelp for help.");
				}
			}
			return true;
		}
		return false;
	}

}
