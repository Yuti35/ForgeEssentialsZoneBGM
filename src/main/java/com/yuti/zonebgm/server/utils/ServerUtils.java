package com.yuti.zonebgm.server.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.permissions.WorldZone;
import com.forgeessentials.api.permissions.Zone;
import com.yuti.zonebgm.ZoneBGMModule;
import com.yuti.zonebgm.server.handlers.PropertiesZoneRegister;
import com.yuti.regionbgm.server.api.ApiRegionBGM;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Utility class for server.
 * @author Yuti
 *
 */
public class ServerUtils {
	
	/**
	 * The Region's BGM API (for this mod).
	 */
	private final static ApiRegionBGM API = ZoneBGMModule.ApiBGM;
	
	/**
	 * 
	 * @return List of players (connected) on the server.
	 */
	@Nullable
	public static List<EntityPlayerMP> getPlayerList() {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(server != null) {
			PlayerList list = server.getPlayerList();
			if(list != null) {
				return list.getPlayers();
			}
		}
		return null;
	}
	
	/**
	 * Refresh player's BGM according to the zone where they're standing.
	 * @param player The player who needs a BGM refresh
	 */
	public static void refreshPlayerBGM(EntityPlayer player) {
		if (player != null) {
			World world = player.getEntityWorld();
			if (world != null) {
				boolean enabled = Boolean.parseBoolean(APIRegistry.perms.getPermissionProperty(player, PropertiesZoneRegister.REGIONBGM_ENABLED));
				String resource = APIRegistry.perms.getPermissionProperty(player, PropertiesZoneRegister.REGIONBGM_MUSIC);
				if (enabled && resource != null) {
					boolean looping = Boolean.parseBoolean(APIRegistry.perms.getPermissionProperty(player, PropertiesZoneRegister.REGIONBGM_LOOP));
					API.playResource(world, (EntityPlayerMP) player, resource, looping);
				}
				else {
					API.stopMusic(world, (EntityPlayerMP) player);
				}					
			}
		}
	}
	
	/**
	 * Refresh BGM of all players in a zone.
	 * @param zone The zone where the BGM needs to be refreshed.
	 */
	public static void refreshPlayersBgmInZone(Zone zone) {
		if(zone != null) {
			List<EntityPlayerMP> players = getPlayerList();
			if(players != null) {
				for(EntityPlayerMP player : players) {
					if(zone.isPlayerInZone(player)) {
						refreshPlayerBGM(player);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param zone
	 * @return All players in the specified zone.
	 */
	public static List<EntityPlayer> getPlayersInZone(Zone zone) {
		List<EntityPlayer> playersInzones = new ArrayList<EntityPlayer>();
		if(zone != null) {
			List<EntityPlayerMP> players = getPlayerList();
			if(players != null) {
				for(EntityPlayerMP player : players) {
					if(zone.isPlayerInZone(player)) {						
						playersInzones.add(player);
					}
				}
			}
		}
		return playersInzones;
	}
	
	/**
	 * Gets the world zone (surface, nether, end, or extras dimensions) by it's  name.
	 * @param zoneName The name of the zone
	 * @return The zone corresponding to the name.
	 */
	@Nullable
	public static WorldZone getWorldZoneByName(String zoneName) {
		Collection<WorldZone> zones = APIRegistry.perms.getServerZone().getWorldZones().values();
		if(zones != null) {
			for(WorldZone zone : zones) {
				if(zone != null && zone.getName().equals(zoneName)) {
					return zone;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Gets a subZone in a worldZone.
	 * @param worldZone The worldZone where the subZone is located.
	 * @param zoneName Name of the subZone
	 * @return The subZone corresponding to the name.
	 */
	@Nullable
	public static Zone getZoneByName(WorldZone worldZone, String zoneName) {
		if(zoneName != null) {
			for(Zone subzone : worldZone.getAreaZones()) {
				if(subzone != null && subzone.getName().equals(zoneName)) {
					return subzone;
				}						
			}		
		}
		
		return null;
	}
	
	/**
	 * 
	 * @return All worldZones names (surface, nether, end, or extras dimensions)
	 */
	@Nullable
	public static List<String> getWorldZonesNames() {
		List<String> zonesNames = new ArrayList<String>();
		Collection<WorldZone> zones = APIRegistry.perms.getServerZone().getWorldZones().values();
		for(WorldZone zone : zones) {
			if(zone != null) {
				zonesNames.add(zone.getName());
			}						
		}			
		return zonesNames;
	}
	
	/**
	 * 
	 * @param worldZone A worldZone (surface, nether, end, or an extra dimension)
	 * @return All subZones names of zones located in the specified worldZone.
	 */
	@Nullable
	public static List<String> getZonesNames(WorldZone worldZone) {
		List<String> zonesNames = new ArrayList<String>();
		for(Zone subzone : worldZone.getAreaZones()) {
			if(subzone != null) {
				zonesNames.add(subzone.getName());
			}						
		}			
		return zonesNames;
	}
	
	/**
	 * Sends an error (red) message to a command sender (player, console...)
	 * @param sender The entity (player, console;..) at who the message must be sent.
	 * @param message The message to send.
	 */
	public static void sendErrorMessageToCommandSender(ICommandSender sender, String message) {
		sendErrorMessageToCommandSender(sender, message, null);
	}
	
	/**
	 * Sends an error (red) message to a command sender (player, console...)
	 * @param sender The entity (player, console;..) at who the message must be sent.
	 * @param message The message to send.
	 * @param url An URL to open when the sender clicks on the message.
	 */
	public static void sendErrorMessageToCommandSender(ICommandSender sender, String message, String url) {
		sendMessageToCommandSender(sender, message, TextFormatting.RED, url);
	}
	
	/**
	 * Sends a valid (green) message to a command sender (player, console...)
	 * @param sender The entity (player, console;..) at who the message must be sent.
	 * @param message The message to send.
	 */
	public static void sendValidMessageToCommandSender(ICommandSender sender, String message) {
		sendValidMessageToCommandSender(sender, message, null);
	}
	
	/**
	 * Sends a valid (green) message to a command sender (player, console...)
	 * @param sender The entity (player, console;..) at who the message must be sent.
	 * @param message The message to send.
	 * @param url An URL to open when the sender clicks on the message.
	 */
	public static void sendValidMessageToCommandSender(ICommandSender sender, String message, String url) {
		sendMessageToCommandSender(sender, message, TextFormatting.GREEN, url);
	}
	
	/**
	 * Sends an information (yellow) message to a command sender (player, console...)
	 * @param sender The entity (player, console;..) at who the message must be sent.
	 * @param message The message to send.
	 */
	public static void sendInfoMessageToCommandSender(ICommandSender sender, String message) {
		sendInfoMessageToCommandSender(sender, message, null);
	}
	
	/**
	 * Sends an information (yellow) message to a command sender (player, console...)
	 * @param sender The entity (player, console;..) at who the message must be sent.
	 * @param message The message to send.
	 * @param url An URL to open when the sender clicks on the message.
	 */
	public static void sendInfoMessageToCommandSender(ICommandSender sender, String message, String url) {
		sendMessageToCommandSender(sender, message, TextFormatting.YELLOW, url);
	}
	
	/**
	 * Sends a message to a command sender (player, console...)
	 * @param sender The entity (player, console;..) at who the message must be sent.
	 * @param message The message to send.
	 * @param formating The formating (color) of the message.
	 */
	public static void sendMessageToCommandSender(ICommandSender sender, String message, TextFormatting formating) {
		sendMessageToCommandSender(sender, message, formating, null);
	}
	
	/**
	 * Sends a message to a command sender (player, console...)
	 * @param sender The entity (player, console;..) at who the message must be sent.
	 * @param message The message to send.
	 * @param formating The formating (color) of the message.
	 * @param url An URL to open when the sender clicks on the message.
	 */
	public static void sendMessageToCommandSender(ICommandSender sender, String message, TextFormatting formating, String url) {
		TextComponentString comp = new TextComponentString(message);
		comp.getStyle().setColor(formating);
		if(url != null) {
			try {
				new URL(url); //Allows to test if the URL is "valid"
				ClickEvent clickMusic = new ClickEvent(Action.OPEN_URL, url);
				comp.getStyle().setClickEvent(clickMusic);
			} catch (MalformedURLException e) {
				//Do nothing (= not adding open url action)
			}
		}
		sender.sendMessage(comp);
	}
}
