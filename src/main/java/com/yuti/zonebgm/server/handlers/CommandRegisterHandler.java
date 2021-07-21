package com.yuti.zonebgm.server.handlers;

import com.yuti.zonebgm.server.commands.CommandZoneBgm;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * This class is used to register the commands of the mod.
 * @author Yuti
 *
 */
public class CommandRegisterHandler {
	
	public static void registerCommands(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandZoneBgm());
	}
	
}
