package com.yuti.zonebgm.server.commands;

import java.util.ArrayList;
import java.util.List;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.permissions.AreaZone;
import com.forgeessentials.api.permissions.WorldZone;
import com.forgeessentials.api.permissions.Zone;
import com.forgeessentials.commons.selections.AreaBase;
import com.yuti.zonebgm.server.handlers.PropertiesZoneRegister;
import com.yuti.zonebgm.server.utils.ServerUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;

/**
 * This command is used to easily manage music's parameters in a zone.
 * It will apply for the default group (everyone) in a zone.
 * To do manage advanced properties for specific groups and users, you can use the /p command from ForgeEssentials
 * @author Yuti
 *
 */
public class CommandZoneBgm extends CommandBase {
	
	private static List<String> commandsUsages = new ArrayList<String>();
	
	static {
		commandsUsages.add("/zonebgm [dimension] [zoneName]");
		commandsUsages.add("/zonebgm [dimension] [zoneName] music");
		commandsUsages.add("/zonebgm [dimension] [zoneName] music [musicurl]");
		commandsUsages.add("/zonebgm [dimension] [zoneName] music clear");
		commandsUsages.add("/zonebgm [dimension] [zoneName] loop");
		commandsUsages.add("/zonebgm [dimension] [zoneName] loop <true / false / clear>");
		commandsUsages.add("/zonebgm [dimension] [zoneName] enabled");
		commandsUsages.add("/zonebgm [dimension] [zoneName] enabled <true / false / clear>");
	}

	@Override
	public String getName() {
		return "zonebgm";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return String.join("\n", commandsUsages);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length >= 2) {
			String worldZoneName = args[0];
			if(worldZoneName != null) {
				Zone mainZone = null;
				WorldZone worldZone = null;
				if(worldZoneName.equals("server")) {
					mainZone = APIRegistry.perms.getServerZone();
				}
				else {
					worldZone = ServerUtils.getWorldZoneByName(worldZoneName);
					mainZone = worldZone;
				}
				if(mainZone == null) {
					ServerUtils.sendErrorMessageToCommandSender(sender, "Main zone "+worldZoneName+" not found");
					return;
				}
				String zoneName = args[1];
				if(zoneName != null) {
					Zone zone = null;
					if(zoneName.equals("global_zone")) {
						zone = mainZone;
					}
					else {
						if(worldZone != null) {
							zone = ServerUtils.getZoneByName(worldZone, zoneName);													
						}
					}
					
					if(zone == null) {
						ServerUtils.sendErrorMessageToCommandSender(sender, "Zone "+zoneName+" not found");
						return;
					}
					
					String groupname = Zone.GROUP_DEFAULT;
					
					String musicValue = zone.getGroupPermission(groupname, PropertiesZoneRegister.REGIONBGM_MUSIC);
					String loopValue = zone.getGroupPermission(groupname, PropertiesZoneRegister.REGIONBGM_LOOP);
					String enabled = zone.getGroupPermission(groupname, PropertiesZoneRegister.REGIONBGM_ENABLED);
					
					if(args.length == 2) {
						AreaBase base = null;
						if(zone instanceof AreaZone) {
							AreaZone area = (AreaZone) zone;
							if(area != null) {
								base = area.getArea();
								
							}
						}
						String infoZone = "Zone " + zone.getName() + " : \n"
										+ (base != null ? "  Coordinates : " + base.toString() +"\n" : "")
										+ "  music = " + (musicValue == null ? "inherited" : musicValue) + "\n" 
										+ "  loop = " + (loopValue == null ? "inherited" : loopValue) + "\n" 
										+ "  enabled = " + (enabled == null ? "inherited" : enabled);
						
						ServerUtils.sendValidMessageToCommandSender(sender, infoZone, musicValue);
						return;
					}
					
					else {
						if(args[2].equals("music")) {
							if(args.length == 3) {
								if(musicValue == null) {
									ServerUtils.sendInfoMessageToCommandSender(sender, "The music is not set yet for this zone (will inherit from the parent zone)");
								}
								else {
									ServerUtils.sendValidMessageToCommandSender(sender, "music = " + musicValue +" in zone "+zone.getName(), musicValue);								
								}
								return;
							}
							else {
								String paramMusic = args[3];
								if(paramMusic.equals("clear")) {
									paramMusic = null;
								}
								zone.setGroupPermissionProperty(groupname, PropertiesZoneRegister.REGIONBGM_MUSIC, paramMusic);
								ServerUtils.sendValidMessageToCommandSender(sender, "Music updated in zone " + zone.getName());
								return;
							}
						}
						else if(args[2].equals("loop")) {
							if(args.length == 3) {
								if(loopValue == null) {
									ServerUtils.sendInfoMessageToCommandSender(sender, "The loop state is not set yet for this zone (will inherit from the parent zone)");
								}
								else {
									ServerUtils.sendValidMessageToCommandSender(sender, "loop = " + loopValue + " in zone " + zone.getName());							
								}
								return;
							}
							else {
								String paramloop = args[3];
								if(paramloop.equals("true") || paramloop.equals("false") || paramloop.equals("clear")) {
									if(paramloop.equals("clear")) {
										paramloop = null;
									}
									zone.setGroupPermissionProperty(groupname, PropertiesZoneRegister.REGIONBGM_LOOP, paramloop);
									ServerUtils.sendValidMessageToCommandSender(sender, "Music loop updated in zone " + zone.getName());
									return;
								}
								else {
									ServerUtils.sendErrorMessageToCommandSender(sender, "Invalid value '"+paramloop + "' for loop");
									return;
								}
							}
						}
						else if(args[2].equals("enabled")) {
							if(args.length == 3) {
								if(enabled == null) {
									ServerUtils.sendInfoMessageToCommandSender(sender, "The activation state is not set yet for this zone (will inherit from the parent zone)");
								}
								else {
									ServerUtils.sendValidMessageToCommandSender(sender, "enabled = " + enabled +" in zone "+zone.getName());								
								}
								return;
							}
							else {
								String paramEnabled = args[3];
								if(paramEnabled.equals("true") || paramEnabled.equals("false") || paramEnabled.equals("clear")) {
									if(paramEnabled.equals("clear")) {
										paramEnabled = null;
									}
									zone.setGroupPermissionProperty(groupname, PropertiesZoneRegister.REGIONBGM_ENABLED, paramEnabled);
									ServerUtils.sendValidMessageToCommandSender(sender, "Activation state updated in zone " + zone.getName());
									return;
								}
								else {
									ServerUtils.sendErrorMessageToCommandSender(sender, "Invalid value '"+ paramEnabled + "' for enabled");
									return;
								}							
							}
						}
					}
				}
			}
		}
		//Default : Nothing has been triggered
		this.showUsage(sender);
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		List<String> optionsList = new ArrayList<String>();
		if(args.length == 1) {
			optionsList.addAll(ServerUtils.getWorldZonesNames());
			optionsList.add("server");
		}
		if(args.length == 2) {
			String prevArg = args[0];
			if(prevArg.equals("server")) {
				optionsList.add("global_zone");
			}
			else {
				WorldZone worldZone = ServerUtils.getWorldZoneByName(prevArg);
				if(worldZone != null) {
					optionsList.addAll(ServerUtils.getZonesNames(worldZone));
					optionsList.add("global_zone");
				}				
			}
		}
		else if(args.length == 3) {
			optionsList.add("music");
			optionsList.add("loop");
			optionsList.add("enabled");
		}
		else if(args.length == 4) {
			String prevArg = args[2];
			if(prevArg != null) {
				if(prevArg.equals("music")) {
					optionsList.add("clear");
				}
				if(prevArg.equals("loop") || prevArg.equals("enabled")) {
					optionsList.add("true");
					optionsList.add("false");
					optionsList.add("clear");
				}
			}	
		}
		return getListOfStringsMatchingLastWord(args, optionsList);
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if(sender instanceof MinecraftServer) {
			return true;
		}
		if(sender instanceof CommandBlockBaseLogic) {
			return true;
		}
		if(sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
			return server.getPlayerList().canSendCommands(player.getGameProfile());
		}
		
		return false;
	}
	
	public void showUsage(ICommandSender sender) {
		ServerUtils.sendInfoMessageToCommandSender(sender, this.getUsage(sender));
	}

}
