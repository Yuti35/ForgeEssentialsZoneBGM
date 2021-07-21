package com.yuti.zonebgm.server.handlers;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.permissions.Zone;

/**
 * This class is used to list, register and initialize the regionbgm's properties for ForgeEssentials.
 * @author Yuti
 *
 */
public class PropertiesZoneRegister {
	
	/**
	 * Default node of regionbgm properties
	 */
	public static final String REGIONBGM_NODE = "regiongbm";
	
	/**
	 * Property for music (use an URL)
	 */
	public static final String REGIONBGM_MUSIC = REGIONBGM_NODE + ".music";

	/**
	 * Property for loop (true / false). Tells if the music must loop in the zone.
	 */
	public static final String REGIONBGM_LOOP = REGIONBGM_NODE + ".loop";
	
	/**
	 * Property for enabled (true / false). Tells if the music should play in the zone or not.
	 */
	public static final String REGIONBGM_ENABLED = REGIONBGM_NODE + ".enabled";
	
	/**
	 * Register and initialize the properties and permissions.
	 */
	public static void registerPermissions() {
		APIRegistry.perms.registerPermissionProperty(REGIONBGM_MUSIC, null, "Set zone music (regionbgm)");
		APIRegistry.perms.registerPermissionProperty(REGIONBGM_LOOP, null, "Makes the music looping or not in the zone");
		APIRegistry.perms.registerPermissionProperty(REGIONBGM_ENABLED, null, "Enable or disable music in the zone");
		if(APIRegistry.perms.getGlobalPermissionProperty(REGIONBGM_MUSIC) == null) {
			APIRegistry.perms.setGroupPermissionProperty(Zone.GROUP_DEFAULT, REGIONBGM_MUSIC, null);			
		}
		if(APIRegistry.perms.getGlobalPermissionProperty(REGIONBGM_LOOP) == null) {
			APIRegistry.perms.setGroupPermissionProperty(Zone.GROUP_DEFAULT, REGIONBGM_LOOP, "true");
		}
		if(APIRegistry.perms.getGlobalPermissionProperty(REGIONBGM_ENABLED) == null) {
			APIRegistry.perms.setGroupPermissionProperty(Zone.GROUP_DEFAULT, REGIONBGM_ENABLED, "true");			
		}
	}
}
