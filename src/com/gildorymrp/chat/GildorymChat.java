package com.gildorymrp.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.gildorymrp.api.Gildorym;
import com.gildorymrp.api.plugin.chat.Channel;
import com.gildorymrp.api.plugin.chat.GildorymChatPlugin;

public class GildorymChat extends JavaPlugin implements GildorymChatPlugin {
	
	private Map<String, Channel> channels = new HashMap<String, Channel>();
	
	@Override
	public void onEnable() {
		Gildorym.registerChatPlugin(this);
		
		this.registerListeners(new AsyncPlayerChatListener(this), new PlayerJoinListener(this), new PlayerQuitListener(this), new PlayerCommandPreprocessListener(this));
		this.getCommand("ch").setExecutor(new ChCommand(this));
		this.getCommand("chathelp").setExecutor(new ChatHelpCommand());
		
		this.saveDefaultConfig();
		
		for (String section : this.getConfig().getConfigurationSection("channels").getKeys(false)) {
			ChannelImpl channel = new ChannelImpl(this, section.toLowerCase());
			channels.put(channel.getName(), channel);
			channel.setCommand(new QuickChannelSwitchCommand(channel.getName(), "Allows you to talk in " + channel.getName() + " on-the-fly", "/<command> [message]", Arrays.asList(new String[] {channel.getName()}), this, channel));
		}
		
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			for (Channel channel : this.getChannels()) {
				if (player.hasPermission("gildorym.chat.ch.listen." + channel.getName().toLowerCase())) {
					channel.addListener(player);
				}
			}
			this.setPlayerChannel(player, this.getChannel(this.getConfig().getString("default-channel").toLowerCase()));
		}
		
	}
	
	private void registerListeners(Listener... listeners) {
		for (Listener listener : listeners) {
			this.getServer().getPluginManager().registerEvents(listener, this);
		}
	}
	
	@Override
	public Channel getPlayerChannel(Player player) {
		for (Channel channel : this.getChannels()) {
			if (channel.getSpeakers().contains(player)) {
				return channel;
			}
		}
		return this.getChannel(this.getConfig().getString("default-channel"));
	}
	
	public void handleChat(Player talking, String message) {
		if (message != "") {
			String format = "";
			if (message.startsWith("*") && message.endsWith("*")) {
				((ChannelImpl) this.getChannel(this.getConfig().getString("default-channel"))).log(talking.getName() + "/" + talking.getDisplayName() + ": " + message);
				for (Player player : new ArrayList<Player>(talking.getWorld().getPlayers())) {
					if (this.getConfig().getInt("emotes.radius") >= 0) {
						if (talking.getLocation().distance(player.getLocation()) <= this.getConfig().getInt("emotes.radius")) {
							format = this.getConfig().getString("emotes.format").replace("%channel%", "emote").replace("%player%", talking.getDisplayName()).replace("%ign%", talking.getName()).replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", message.replace("*", ""));
							player.sendMessage(format);
						}
					} else {
						format = this.getConfig().getString("emotes.format").replace("%channel%", "emote").replace("%player%", talking.getDisplayName()).replace("%ign%", talking.getName()).replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", message.replace("*", ""));
						player.sendMessage(format);
					}
				}
			} else {
				if (this.getPlayerChannel(talking) != null) {
					((ChannelImpl) this.getPlayerChannel(talking)).log(talking.getName() + "/" + talking.getDisplayName() + ": " + message);
					for (Player player : new HashSet<Player>(this.getPlayerChannel(talking).getListeners())) {
						if (player != null) {
							if (this.getPlayerChannel(talking).getRadius() >= 0) {
								if (talking.getWorld().equals(player.getWorld())) {
									if (talking.getLocation().distance(player.getLocation()) <= (double) this.getPlayerChannel(talking).getRadius()) {
										if (((ChannelImpl) this.getPlayerChannel(talking)).isGarbleEnabled()) {
											double distance = talking.getLocation().distance(player.getLocation());
											double clearRange = 0.75D * (double) this.getPlayerChannel(talking).getRadius();
											double hearingRange = (double) this.getPlayerChannel(talking).getRadius();
											double clarity = 1.0D - ((distance - clearRange) / hearingRange);
											format = this.getPlayerChannel(talking).getFormat().replace("%channel%", this.getPlayerChannel(talking).getName()).replace("%player%", talking.getDisplayName()).replace("%ign%", talking.getName()).replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", this.garbleMessage(message, clarity, ChatColor.DARK_GRAY, ChatColor.WHITE));
										} else {
											format = this.getPlayerChannel(talking).getFormat().replace("%channel%", this.getPlayerChannel(talking).getName()).replace("%player%", talking.getDisplayName()).replace("%ign%", talking.getName()).replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", message);
										}
										player.sendMessage(format);
									}
								}
							} else {
								format = this.getPlayerChannel(talking).getFormat().replace("%channel%", this.getPlayerChannel(talking).getName()).replace("%player%", talking.getDisplayName()).replace("%ign%", talking.getName()).replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", message);
								player.sendMessage(format);
							}
						} else {
							this.getPlayerChannel(talking).removeListener(player);
						}
					}
				} else {
					talking.sendMessage(ChatColor.RED + "You must talk in a channel! Use /chathelp for help.");
				}
			}
		}
	}
	
	private String garbleMessage(String message, double clarity, ChatColor dimMessageColour, ChatColor messageColour) {
		StringBuilder newMessage = new StringBuilder();
		Random random = new Random();
		int i = 0;
		int drops = 0;
		while (i < message.length()) {
			int c = message.codePointAt(i);
			i += Character.charCount(c);
			if (random.nextDouble() < clarity) {
				newMessage.appendCodePoint(c);
			} else if (random.nextDouble() < 0.1D) {
				newMessage.append(dimMessageColour);
				newMessage.appendCodePoint(c);
				newMessage.append(messageColour);
			} else {
				newMessage.append(' ');
				drops++;
			}
		}
		if (drops == message.length()) {
			String noise = "~~~";
			if (noise != null) {
				return noise;
			}
		}
		return new String(newMessage);
	}
	
	@Override
	public Collection<Channel> getChannels() {
		return channels.values();
	}
	
	@Override
	public void setPlayerChannel(Player player, Channel channel) {
		for (Channel channel1 : this.getChannels()) {
			if (channel1.getSpeakers().contains(player)) {
				channel1.removeSpeaker(player);
			}
		}
		channel.addListener(player);
		channel.addSpeaker(player);
	}
	
	@Override
	public Channel getChannel(String name) {
		return this.channels.get(name);
	}
	
}
