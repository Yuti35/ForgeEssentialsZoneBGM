# Zone BGM (for Forge Essentials)

Welcome to the **Zone BGM** repository!

This mod allows you to use zones from [Forge Essentials](https://www.curseforge.com/minecraft/mc-mods/forge-essentials-74735) to assign background musics to zones.

When a player will enter a zone, the assigned music (or playlist) will be streamed on their side.

The played resource is an URL from an audio source (youtube, twitch, almost anything...)

With this tool, you can, for example, have a music for a town, a shop, a pirate's ship...

This mod uses the [RegionBGM API](https://www.curseforge.com/minecraft/mc-mods/regionbgm-api) to stream music to players and [Forge Essentials](https://www.curseforge.com/minecraft/mc-mods/forge-essentials-74735) to create and manage zones.

It's only required on **server-side**. The clients do not need to install it.

Currently the mod is made for **1.12.2** (so, you'll need to download **1.12.2** releases of the dependencies)

You can download the mod on [curseforge](https://www.curseforge.com/minecraft/mc-mods/forge-essentials-zone-bgm).

## Installation

1. Download and install [RegionBGM](https://www.curseforge.com/minecraft/mc-mods/regionbgm-api) on **client-side** and **server-side** (players from your server will need to install it).

2. Download and install [Forge Essentials](https://www.curseforge.com/minecraft/mc-mods/forge-essentials-74735) on **server-side**.

3. Download and install [Zone BGM](https://www.curseforge.com/minecraft/mc-mods/forge-essentials-zone-bgm) on **server-side**.

You might want to install [WorledEdit](https://www.curseforge.com/minecraft/mc-mods/worldedit) too, on **server-side** to select your zones more easily.

## Supported sources

The music can be a source from internet : A youtube video, a youtube playlist, a twitch livestream...

As the [RegionBGM API](https://www.curseforge.com/minecraft/mc-mods/regionbgm-api) uses [lavaplayer](https://github.com/sedmelluq/lavaplayer) you can check all the supported sources [here](https://github.com/sedmelluq/lavaplayer#supported-formats).

## Forge Essentials module

**ZoneBGM** is loaded as a Forge Essentials's module. That means you can disable or enable it any time through the **main.cfg** configuration file from Forge Essentials :

```
Modules {
        ...
        B:zonebgm=true
    }
```

The module is enabled by default.

## Get started

### Create a zone

First, you'll need to have **zones** to assign musics! These zones are a feature from **Forge Essentials** (to manage permissions).

* On your server, select an area for **ForgeEssentials**. You can do it by using **WorldEdit**, with the **//wand** or with **//pos1** and **//pos2**. If you don't have worldedit, you can use **//fewand** instead.

* Once your area is selected, define a **zone** with **ForgeEssentials** :

```
/zone define <your_zone_name>
```

### Assign a music to a zone

* Once the **zone** is created, you can simply assign a music to it by running :

```
/zonebgm dimension your_zone_name music <music_url>
```

* Now, when a player enters the zone, the music will starts playing on their side!

For more information about the properties of the zone and the **/zonebgm** command, read the next sections.

## Zones properties

The mod adds **3 properties** to each zones :

* **music** : The URL of the resource which should be played in the zone.
* **loop** : A value (**true** or **false**) which controls if the music should loops or not in the area.
* **enabled** : A value (**true** or **false**) which controls if the music should be played or not in the area.

As with **Forge Essentials**, these properties are undefined when you create the zone and **inherit** from their parent zone (because you can create subzones into zones).

The zone hierarchy is :

1. The **server zone**, containing all the **world zones**
2. The **world zones** containing all zones of a world (all zones from the surface, from nether, from the end, and extra multiworlds)
3. The zones you create in a world (their parent are the **worldzone**)
4. the subzones you create in a zone you defined (which is the parent of the subzone), etc...

By default, the mod assigns these values to the musical properties of the **server zone** :

* **music** : **null** (no music)
* **loop** : **true**
* **enabled** : **true**

That means if you don't change anything, when you create a zone, it will be set to have no music, and the loop and enabled properties set to **true** (because it's inherit from the world zone, which inherit from the server-zone).

With the **/zonebgm** command, you can manually assign and change properties's values of a zone (which will not inherit anymore from their parent anymore).

### A more advanced example :

1. You decide to create a big zone for your town, named **"main_town"**

2. You assign it a music (for example : [https://www.youtube.com/watch?v=roMWEeV2P4U](https://www.youtube.com/watch?v=roMWEeV2P4U))

3. You want the music to loop (and be enabled). You don't need to do anything because you inherit from the world zone which inherit from the **server zone** where the **loop** and **enabled** properties are set on true. But, you can't still manually assign these values with the **/zonebgm** command (in case you have changed or will change the **world zone** or **server zone** properties)

4. You define a subzone **"castle"** inside your zone **"main-town"**. You don't want the music to change in this zone, so you don't need to touch anything, because it will inherit from the parent zone (**main_town**).

5. You define a subzone **"shop"** inside your zone **"main-town"**. you want a different music in this subzone. you just need to assign a music with **/zonebgm**.

6. When you leave the **shop** zone, the **main_town** zone music will be played, nothing will change if you go to the **castle** zone, and when you leave the **main_town** zone, the music will stops (except if you set a music for the **world zone** you are or the **server zone**)

## Commands

The mod provides one command named **/zonebgm** which allows you to check and manage the musical properties of a zone.

### Display information

This command will display the information (the 3 properties's values) of the zone.

- **dimension** refers to a **world zone**. You have :
	- **WORLD_0** : The "surface" (main dimension)
	- **WORLD_-1** : The nether
	- **WORLD_1** : The end
	- **WORLD_X** : For multiworls, where "X" is the ID of the dimension.
	- **server** : The whole **server zone**
- **your_zone_name** refers to the name of your zone. You can also use **global_zone** to select the whole **world zone** or **server zone**.

You can use **tabulations** to find the right dimension and zone.

You can click on the displayed message to open the music's URL in your browser.

```
/zonebgm dimension your_zone_name
```

This command will display the information about the **music** property of the zone (you can click on the message to open the URL in your browser).

```
/zonebgm dimension your_zone_name music
```

This command will display the information about the **loop** property of the zone.

```
/zonebgm dimension your_zone_name loop
```

This command will display the information about the **enabled** property of the zone.

```
/zonebgm dimension your_zone_name enabled
```

### Assign and clear a music

This command will assign a **music** to the zone.

```
/zonebgm dimension your_zone_name music music_url
```

This command will clear the **music** of the zone (will inherit from the parent zone).

```
/zonebgm dimension your_zone_name music clear
```

### Enable, disable and clear looping

This command will enable music (or playlist) **looping** in the zone

```
/zonebgm dimension your_zone_name loop true
```

This command will disable music (or playlist) **looping** in the zone

```
/zonebgm dimension your_zone_name loop false
```

This command will clear the **loop** property of the zone (will inherit from the parent zone).

```
/zonebgm dimension your_zone_name loop clear
```

### Enable, disable and clear activation

This command will allows the music to be played in the zone.

```
/zonebgm dimension your_zone_name enabled true
```

This command will prevents the music to be played in the zone.

```
/zonebgm dimension your_zone_name enabled false
```

This command will clear the **enabled** property of the zone (will inherit from the parent zone).

```
/zonebgm dimension your_zone_name enabled clear
```

### Disable the mod

The [RegionBGM API](https://www.curseforge.com/minecraft/mc-mods/regionbgm-api) creates a gamerule for each mod using the API. This gamerule allows you to enable or disable the mod at any time. 

For **Zone BGM** you can use :

```
/gamerule bgm_zonebgm <true:false>
```

## Forge Essentials properties

The 3 musical properties of each zone are managed with **Forge Essentials properties** : **regionbgm.music**, **regionbgm.loop** and **regionbgm.enabled**.

As it will be the main usage of the mod, the **/zonebgm** command update these properties for the default group (all players). But, if you want, you can assign different values for the different groups (or users!) for each zone with the **/p** command from **ForgeEssentials** (for example : having a different music playing in a zone if you're in creative or in survival)

## Javadoc

You can find the Javadoc of the mod (for developers) here : [Zone BGM documentation](https://yuti35.github.io/ZoneBGM/)
