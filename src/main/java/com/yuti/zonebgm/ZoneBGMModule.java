package com.yuti.zonebgm;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.core.moduleLauncher.FEModule;
import com.forgeessentials.util.events.FEModuleEvent.FEModuleServerInitEvent;
import com.forgeessentials.util.events.ServerEventHandler;
import com.yuti.regionbgm.server.api.ApiRegionBGM;
import com.yuti.zonebgm.server.handlers.CommandRegisterHandler;
import com.yuti.zonebgm.server.handlers.PropertiesZoneRegister;
import com.yuti.zonebgm.server.handlers.ServerHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Module class for ZoneBGM
 * It will allows to register and loads the mod as a ForgeEssentials's module.
 * Then, the module can be disabled or enabled through ForgeEssentials's main config.
 * @author Yuti
 *
 */
@FEModule(name = ZoneBGM.MODID, parentMod = ZoneBGM.class)
public class ZoneBGMModule extends ServerEventHandler {
	
	public static final ApiRegionBGM ApiBGM = ApiRegionBGM.getInstance(ZoneBGM.MODID);
	    
	@SubscribeEvent
	public void initServer(FEModuleServerInitEvent feEvent) {
		FMLStateEvent fmlEvent = feEvent.getFMLEvent();
		if(fmlEvent != null && fmlEvent instanceof FMLServerStartingEvent) {
			FMLServerStartingEvent event = (FMLServerStartingEvent) fmlEvent;
			ApiBGM.registerOnServer(event.getServer());
			ServerHandler serverHandler = new ServerHandler();
			MinecraftForge.EVENT_BUS.register(serverHandler);
			APIRegistry.FE_EVENTBUS.register(serverHandler);
			PropertiesZoneRegister.registerPermissions();
			CommandRegisterHandler.registerCommands(event);			
		}
	}
}
