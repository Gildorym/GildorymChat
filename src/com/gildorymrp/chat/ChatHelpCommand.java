package com.gildorymrp.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChatHelpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("chathelp")) {
			if (sender.hasPermission("gildorym.chat.help")) {
				sender.sendMessage(ChatColor.AQUA + "Chat help:");
				sender.sendMessage(ChatColor.BLUE + "/ch list: " + ChatColor.YELLOW + "Lists channels");
				sender.sendMessage(ChatColor.BLUE + "/ch talkin [channel]: " + ChatColor.YELLOW + "Makes you talk in the given channel");
				sender.sendMessage(ChatColor.BLUE + "/ch mute [channel]: " + ChatColor.YELLOW + "Mutes a certain channel");
				sender.sendMessage(ChatColor.BLUE + "/ch listen [channel]: " + ChatColor.YELLOW + "Unmutes a certain channel");
			}
			return true;
		}
		return false;
	}

}
