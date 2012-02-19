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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class WTChatListener implements Listener {

    public final static String MESSAGE_FORMAT = "%prefix %player: &f%message";
    public final static String LOCAL_MESSAGE_FORMAT = "<%player> : &f%message";
    public final static String RADIO_MESSAGE_FORMAT = "<%player> : &e%message";
    public final static Boolean RANGED_MODE = true;
    public final static double CHAT_RANGE = 200d;
    public final static double RADIO_RANGE = 1000d;
    protected String messageFormat = MESSAGE_FORMAT;
    protected String localMessageFormat = LOCAL_MESSAGE_FORMAT;
    protected String radioMessageFormat = RADIO_MESSAGE_FORMAT;
    protected boolean rangedMode = RANGED_MODE;
    protected double chatRange = CHAT_RANGE;
    protected double radioRange = RADIO_RANGE;
    protected String displayNameFormat = "%prefix%player%suffix";
    protected String optionChatRange = "chat-range";
    protected String optionMessageFormat = "message-format";
    protected String optionRangedMode = "force-ranged-mode";
    protected String optionDisplayname = "display-name-format";
    private final WalkieTalkies plugin;
    Functions f;

    public WTChatListener(YamlConfiguration config, WalkieTalkies aThis) {
        this.messageFormat = config.getString("message-format", this.messageFormat);
        this.localMessageFormat = config.getString("local-message-format", this.localMessageFormat);
        this.radioMessageFormat = config.getString("radio-message-format", this.radioMessageFormat);
        this.rangedMode = config.getBoolean("ranged-mode", this.rangedMode);
        this.chatRange = config.getDouble("chat-range", this.chatRange);
        this.displayNameFormat = config.getString("display-name-format", this.displayNameFormat);
        this.plugin = aThis;
        this.f = new Functions(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        Player player = event.getPlayer();
        
        if (player.getItemInHand().getType() != Material.COMPASS) {
        	if (!player.getInventory().contains(new ItemStack(Material.REDSTONE,1))) {
        		player.sendMessage(ChatColor.RED + "You need more redstone to use your radio");
        		return;
        	}
        }

        String message = messageFormat;
        boolean localChat = rangedMode;

        String chatMessage = event.getMessage();

        if (localChat == true) {
        	if (player.getItemInHand().getType() == Material.COMPASS) {
        		message = radioMessageFormat;
        	}
        	if (player.getItemInHand().getType() != Material.COMPASS) {
        		message = localMessageFormat;
        	}
        }

        message = message.replace("%message", "%2$s").replace("%displayname", "%1$s");

        event.setFormat(message);
        event.setMessage(chatMessage);

        if (localChat) {
        	if (player.getItemInHand().getType() == Material.COMPASS) {
        		double range = radioRange;
        		event.getRecipients().clear();
        		event.getRecipients().addAll(f.getRadioRecipients(player, message, range));
        	}
        	if (player.getItemInHand().getType() != Material.COMPASS) {
        		double range = chatRange;
        		event.getRecipients().clear();
        		event.getRecipients().addAll(f.getLocalRecipients(player, message, range));
        		player.getInventory().removeItem(new ItemStack(Material.REDSTONE,1));
        	}
        }
    }
}
