This fork is an attempt to make v249 work with some of the more modern shaders. Many of Botania's built-in shaders conflict in one way or another with other mods, 
however, some still work. The goal of this project was to be able to manually switch on and off things such as the shader for the gaia guardian, mana pylons, etc, 
so that the shader setup could be customized to work with a wider variety of shaders and other mods. 

I had no idea how hard it would be to get this to compile due to the age of the code, with the https changes, so the gradle setup is a mess, I had to localize a bunch of deps, and I have a bazillion commits. 

But on the bright side, the build/package system is working now, so I can actually start on the changes I wanted to make. 

What works: 

- You can select individual shaders from Botania, each of which have been added to the config file. 

What does not work: 

- If you select them on/off in-game, it will not function correctly without a reboot. I have yet to set it so that these are not configurable in-game. 

![](https://github.com/Vazkii/Botania/blob/master/web/img/logo.png)  
Welcome to the Botania repository.  

Botania is a [Minecraft](https://minecraft.net/) mod based on adding natural magic to the game. It's inspired by other magic mods, such as [Thaumcraft](http://www.minecraftforum.net/topic/2011841-) or [Blood Magic](http://www.minecraftforum.net/topic/1899223-).  

Botania by Vazkii is licensed under the [Botania License](http://botaniamod.net/license.php)
