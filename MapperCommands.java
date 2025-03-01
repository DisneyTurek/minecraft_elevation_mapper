package org.dis.elevationMapper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapperCommands implements CommandExecutor, TabExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equals("mapper")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if(args[0].equals("quickMap")){
                    if(args[1].equals("average")) {
                        CreateMap map = new CreateMap();
                        map.createSimpleMap(args[2], args[1]);
                        map.buildMap();
                    } else if(args[1].equals("median")) {
                        CreateMap map = new CreateMap();
                        map.createSimpleMap(args[2], args[1]);
                        map.buildMap();
                    } else if(args[1].equals("mode")) {
                        CreateMap map = new CreateMap();
                        map.createSimpleMap(args[2], args[1]);
                        map.buildMap();
                    } else if(args[1].equals("min")) {
                        CreateMap map = new CreateMap();
                        map.createSimpleMap(args[2], args[1]);
                        map.buildMap();
                    } else if(args[1].equals("max")) {
                        CreateMap map = new CreateMap();
                        map.createSimpleMap(args[2], args[1]);
                        map.buildMap();
                    } else if(args[1].equals("midpoint")) {
                        CreateMap map = new CreateMap();
                        map.createSimpleMap(args[2], args[1]);
                        map.buildMap();
                    }
                } else if(args[0].equals("largeMap")){
                    if(args[1].equals("average")) {
                        CreateMap map = new CreateMap();
                        map.createLargeSimpleMap(args[2], args[1]);
                        map.buildMap();
                    } else if(args[1].equals("median")) {
                        CreateMap map = new CreateMap();
                        map.createLargeSimpleMap(args[2], args[1]);
                        map.buildMap();
                    } else if(args[1].equals("mode")) {
                        CreateMap map = new CreateMap();
                        map.createLargeSimpleMap(args[2], args[1]);
                        map.buildMap();
                    } else if(args[1].equals("min")) {
                        CreateMap map = new CreateMap();
                        map.createLargeSimpleMap(args[2], args[1]);
                        map.buildMap();
                    } else if(args[1].equals("max")) {
                        CreateMap map = new CreateMap();
                        map.createLargeSimpleMap(args[2], args[1]);
                        map.buildMap();
                    } else if(args[1].equals("midpoint")) {
                        CreateMap map = new CreateMap();
                        map.createLargeSimpleMap(args[2], args[1]);
                        map.buildMap();
                    }
                }

            }
        }



        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equals("mapper")) {
            if (args.length == 1) {
                return Arrays.asList(new String[]{"largeMap","quickMap"});
            } else if (args[0].equals("quickMap") || args[0].equals("largeMap")) {
                if (args.length == 2) {
                    return Arrays.asList(new String[]{"average","max","median","midpoint","min","mode"});
                }
            }
        }

        return new ArrayList<>();
    }
}
