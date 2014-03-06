package com.gildorymrp.chat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.gildorymrp.api.plugin.chat.Channel;

public class ChannelImpl implements Channel {
	
	private String name;
	private ChatColor colour;
	private String format;
	private Set<String> speakers;
	private Set<String> listeners;
	private int radius;
	private boolean garbleEnabled;
	private Command command;
	private File log;
	
	public ChannelImpl(GildorymChat plugin, String name) {
		this.name = name;
		this.colour = ChatColor.valueOf(plugin.getConfig().getString("channels." + this.getName() + ".colour").toUpperCase());
		this.format = plugin.getConfig().getString("channels." + this.getName() + ".format");
		this.speakers = new HashSet<String>();
		this.listeners = new HashSet<String>();
		this.radius = plugin.getConfig().getInt("channels." + this.getName() + ".radius");
		this.setGarbleEnabled(plugin.getConfig().getBoolean("channels." + this.getName() + ".garble-enabled") && this.radius >= 0);
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		log = new File(plugin.getDataFolder() + File.separator + "chat-" + this.getName() + ".log");
		if (!log.exists()) {
			try {
				log.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
	
	@Override
	public ChatColor getColour() {
		return colour;
	}
	
	@Override
	public String getFormat() {
		return format;
	}
	
	@Override
	public Set<Player> getListeners() {
		Set<Player> listeners = new HashSet<Player>();
		for (String listener : this.listeners) {
			listeners.add((Player) Bukkit.getOfflinePlayer(listener));
		}
		return listeners;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public int getRadius() {
		return radius;
	}
	
	@Override
	public Collection<Player> getSpeakers() {
		Set<Player> speakers = new HashSet<Player>();
		for (String speaker : this.speakers) {
			speakers.add((Player) Bukkit.getOfflinePlayer(speaker));
		}
		return speakers;
	}
	
	@Override
	public void setColour(ChatColor colour) {
		this.colour = colour;
	}
	
	@Override
	public void setFormat(String format) {
		this.format = format;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public boolean isGarbleEnabled() {
		return garbleEnabled;
	}
	
	public void setGarbleEnabled(boolean garbleEnabled) {
		this.garbleEnabled = garbleEnabled;
	}
	
	@Override
	public void addListener(Player listener) {
		this.listeners.add(listener.getName());
	}
	
	@Override
	public void addSpeaker(Player speaker) {
		this.speakers.add(speaker.getName());
	}
	
	@Override
	public void removeListener(Player listener) {
		this.listeners.remove(listener.getName());
	}
	
	@Override
	public void removeSpeaker(Player speaker) {
		this.speakers.remove(speaker.getName());
	}
	
	public void setCommand(Command command) {
		this.command = command;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public void log(String message) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(log, true));
			writer.append(message + "\n");
			writer.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
}
