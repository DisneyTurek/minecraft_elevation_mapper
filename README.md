# Minecraft Elevation Mapper

Minecraft plugin that puts digital elevation models (DEMs) into a Minecraft World

This plugin was made with the specific purpose of transfering TIF files into a Minecraft world. This means that any other kind of DEM is not supported. This also means you can take any image and convert it to a TIF and use this to put it in Minecraft, though it obviously won't have the best effect.

What you need:

- A Minecraft Server capable of running plugins on version 1.21 or later
- A void world named "void_world" (You can find one in the code here or you can create one yourself)
- A tif file that you would like to map (You can find two example files inside the code here)

How to set up this plugin:

1. Place the jar file in your server's "plugins" folder. (You will need a Minecraft server type that supports plugins like Spigot or Paper)
2. Make sure your void world is named "void_world" and place it in the base folder for your server
3. Start your server with the plugin enabled, once you have done so, a new folder should appear in your "plugins" folder called "ElevationMapper"
4. Inside the "ElevationMapper" folder, create a new folder with any one word name you want (i.e. it can't have spaces)
5. Inside this new folder you created place your tif file and make sure it is renamed to "elevation.tif" otherwise the plugin will not work.
6. Once you have set that up, you can run one of the following two commands in Minecraft:

    a. "mapper quickMap \<average/max/median/midpoint/mid/mode\> \<folderName\>"

    b. "mapper largeMap \<average/max/median/midpoint/mid/mode\> \<folderName\>"
7. Wait a minute for the plugin to create the world and send you to it. Sometimes, when you run the largeMap version of the command it will disconnect you from the server. If it does this, simply wait a few seconds and rejoin the server and you will be able to view the map properly
