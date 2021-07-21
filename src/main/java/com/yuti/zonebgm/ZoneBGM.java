package com.yuti.zonebgm;

import net.minecraftforge.fml.common.Mod;

/**
 * Main class for Zone BGM mod.
 * This is mostly a serve-side mod, but can be used in a single-player world too.
 * It requires ForgeEssentials and RegionBGM.
 * @author Yuti
 *
 */
@Mod(modid = ZoneBGM.MODID, name = ZoneBGM.NAME, version = ZoneBGM.VERSION, dependencies="required-after:regionbgm@[1.0.0];required-after:forgeessentials@[12.3];", acceptableRemoteVersions="*")
public class ZoneBGM
{
    public static final String MODID = "zonebgm";
    public static final String NAME = "Zone BGM";
    public static final String VERSION = "1.0.0";
}
