package com.yuti.zonebgm.server.handlers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.forgeessentials.api.UserIdent;
import com.forgeessentials.api.permissions.PermissionEvent;
import com.forgeessentials.api.permissions.Zone;
import com.forgeessentials.util.events.PlayerChangedZone;
import com.yuti.zonebgm.server.utils.ServerUtils;
import com.yuti.regionbgm.server.events.PlayerAskRefreshBgmEvent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

/**
 * This class is used to handle server-side events of the mod.
 * @author Yuti
 *
 */
public class ServerHandler {
	
	private static Set<Zone> zonesToRefresh = new HashSet<Zone>();
	private static Set<EntityPlayer> playersToRefresh = new HashSet<EntityPlayer>();
	private static Object zoneRefreshLock = new Object();
	
	/**
	 * Refresh player's BGM when they move into another zone.
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerChangeZone(PlayerChangedZone event) {
		EntityPlayer player = event.getEntityPlayer();
		if(player != null) {
			ServerUtils.refreshPlayerBGM(player);
		}
	}

	/**
	 * Refresh player's BGM when a client asked a refresh (when RegionBGM is reactivated).
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerAskForRefresh(PlayerAskRefreshBgmEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player != null) {
			ServerUtils.refreshPlayerBGM(player);
			
		}
	}

	/**
	 * Schedules a BGM's refresh for the players in a zone which got updated. (when the resource change, or the loop, etc...)
	 * @param event
	 */
	@SubscribeEvent
	public void onMusicResourceZoneUpdateGroup(PermissionEvent.Group.ModifyPermission event) {
		if(event.permissionNode != null && event.permissionNode.startsWith(PropertiesZoneRegister.REGIONBGM_NODE)) {
			synchronized (zoneRefreshLock) {
				zonesToRefresh.add(event.zone);				
			}
		}
	}
	
	/**
	 * Schedules a BGM's refresh for the players of a group which got deleted.
	 * It's for the special case where a group have a different BGM in a zone (different that the one of the default group)
	 * @param event
	 */
	@SubscribeEvent
	public void onMusicResourceZoneDeleteGroup(PermissionEvent.Group.Delete event) {
		synchronized (zoneRefreshLock) {
			Map<String, Set<UserIdent>> groups = event.serverZone.getGroupPlayers();
			if(groups != null) {
				Set<UserIdent> users = groups.get(event.group);
				if(users != null) {
					for(UserIdent ident : users) {
						scheduleRefreshUserPermissionUser(ident);
					}			
				}
			}
		}
	}
	
	/**
	 * Schedules a BGM's refresh for the players in a zone which got deleted. (stop the music or play the one from the parent zone)
	 * @param event
	 */
	@SubscribeEvent
	public void onDeleteZoneRefreshMusic(PermissionEvent.Zone.Delete event) {
		List<EntityPlayer> players = ServerUtils.getPlayersInZone(event.zone);
		synchronized (zoneRefreshLock) {
			for(EntityPlayer player : players) {
				playersToRefresh.add(player);
			}
		}
	}
	
	/**
	 * Schedules a BGM's refresh for a player who got their permission (for the music in the zone) updated.
	 * @param event
	 */
	@SubscribeEvent
	public void onMusicResourceZoneUpdatPermissioneUser(PermissionEvent.User.ModifyPermission event) {
		if(event.permissionNode != null && event.permissionNode.startsWith(PropertiesZoneRegister.REGIONBGM_NODE)) {
			scheduleRefreshUserPermissionUser(event.ident);
		}
	}
	
	/**
	 * Schedules a BGM's refresh for a player who got their group updated.
	 * It's for the special case where a group have a different BGM in a zone (different that the one of the default group)
	 * @param event
	 */
	@SubscribeEvent
	public void onMusicResourceZoneUpdateGroupUser(PermissionEvent.User.ModifyGroups event) {
		scheduleRefreshUserPermissionUser(event.ident);
	}
	
	/**
	 * Schedules a BGM's refresh for a player
	 * @param ident The ident of the player.
	 */
	private void scheduleRefreshUserPermissionUser(UserIdent ident) {
		if(ident != null) {
			EntityPlayer player = ident.getPlayer();
			if(player != null) {
				synchronized (zoneRefreshLock) {
					playersToRefresh.add(player);				
				}					
			}
		}
	}
	
	/**
	 * Refresh the BGM for the scheduled zones and players
	 * @param event
	 */
	@SubscribeEvent
	public void onRefreshPlayersAfterPermissionUpdate(ServerTickEvent event) {
		synchronized (zoneRefreshLock) {
			if(!zonesToRefresh.isEmpty()) {
				for(Zone zone : zonesToRefresh) {
					ServerUtils.refreshPlayersBgmInZone(zone);
				}
				zonesToRefresh.clear();
			}		
			if(!playersToRefresh.isEmpty()) {
				for(EntityPlayer player : playersToRefresh) {
					ServerUtils.refreshPlayerBGM(player);
				}
				playersToRefresh.clear();
			}
		}
	}
}
