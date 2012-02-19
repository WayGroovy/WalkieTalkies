package com.github.WayGroovy.WalkieTalkies;

/*
 * WalkieTalkies - Ranged chat management plugin for Bukkit
 * Originally - bChatManager chat management plugin for Bukkit
 * Copyright (C) 2012 WayGroovy
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class WalkieTalkies extends JavaPlugin {
	
	protected final static Logger logger = Logger.getLogger("Minecraft");
	protected WTChatListener listener;
	
	@Override
	public void onEnable() {
		setupConfig();
		this.getServer().getPluginManager().registerEvents(this.listener, this);
		logger.info("WalkieTalkies enabled.");
	}
	
	@Override
	public void onDisable() {
		this.listener = null;
		logger.info("WalkieTalkies disabled.");
	}
	
	public void setupConfig() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		this.listener = new WTChatListener((YamlConfiguration) this.getConfig(), this);
	}

}
