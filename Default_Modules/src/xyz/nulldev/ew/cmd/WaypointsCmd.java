package xyz.nulldev.ew.cmd;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import tk.wurst_client.api.Chat;
import tk.wurst_client.api.Module;
import tk.wurst_client.api.World;
import tk.wurst_client.commands.Cmd;
import tk.wurst_client.utils.BlockUtils;
import tk.wurst_client.utils.F;
import xyz.nulldev.ew.waypoints.Waypoint;

import java.util.Iterator;
import java.util.Objects;

/**
 * Project: EvenWurse
 * Created: 23/12/15
 * Author: nulldev
 */
@Module.ModuleInfo(minVersion = 133)
@Cmd.Info(name = "waypoints", help = "Used to manipulate waypoints." +
        "\n\nUsage: waypoints [operation] [args]...\n\n" +
        "Possible Operations:\n" +
        "add <name>: Add a waypoint with the following name.\n" +
        "add <name> <x> <y> <z>: Add a waypoint at the coordinates with the following name.\n" +
        "{set/move} <name>: Move a waypoint with the following name to the player's coordinates.\n" +
        "{set/move} <name> <x> <y> <Z>: Move a waypoint with the following name to the specified coordinates.\n" +
        "del <name>: Delete a waypoint.\n" +
        "delnear: Deletes the waypoint nearest to the player.\n" +
        "list: List all waypoints in the current world.\n" +
        "listall: List all waypoints.",
        syntax = "waypoints [operation] [args]...", aliases = {"waypoint", "wp"})
public class WaypointsCmd extends Cmd {
    @Override
    public void execute(String[] args) throws Error {
        if(args.length < 1) {
            Chat.sendError("No operation specified!");
            printHelp();
            return;
        }

        String[] withoutArgs = new String[args.length - 1];
        System.arraycopy(args, 1, withoutArgs, 0, withoutArgs.length);
        int l = withoutArgs.length;

        switch(args[0]) {
            case "add":
                if(l == 1) {
                    String name = withoutArgs[0];
                    if(!checkExists(name) && checkTags(name)) {
                        Waypoint waypoint = fromPlayer(name);
                        Waypoint.WAYPOINTS.add(waypoint);
                        waypointAdded(waypoint);
                    } else {
                        alreadyExists(name);
                    }
                } else if(l == 4) {
                    String name = withoutArgs[0];
                    if(!checkExists(name) && checkTags(name)) {
                        try {
                            Waypoint waypoint = new Waypoint(name,
                                    Double.parseDouble(withoutArgs[1]),
                                    Double.parseDouble(withoutArgs[2]),
                                    Double.parseDouble(withoutArgs[3]),
                                    World.getUUID().toString());
                            Waypoint.WAYPOINTS.add(waypoint);
                            waypointAdded(waypoint);
                        } catch(NumberFormatException e) {
                            invalidCoordinates();
                        }
                    } else {
                        alreadyExists(name);
                    }
                } else {
                    invalidNumberArgs("add");
                }
                break;
            case "move":
                //Alias to set
            case "set":
                if(l == 1) {
                    String name = withoutArgs[0];
                    if(checkExists(name) && checkTags(name)) {
                        Waypoint waypoint = fromPlayer(name);
                        removeWaypoint(name);
                        Waypoint.WAYPOINTS.add(waypoint);
                        waypointEdited(waypoint);
                    } else {
                        doesNotExist(name);
                    }
                } else if(l == 4) {
                    String name = withoutArgs[0];
                    if(checkExists(name) && checkTags(name)) {
                        try {
                            Waypoint waypoint = new Waypoint(name,
                                    Double.parseDouble(withoutArgs[1]),
                                    Double.parseDouble(withoutArgs[2]),
                                    Double.parseDouble(withoutArgs[3]));
                            removeWaypoint(name);
                            Waypoint.WAYPOINTS.add(waypoint);
                            waypointEdited(waypoint);
                        } catch(NumberFormatException e) {
                            invalidCoordinates();
                        }
                    } else {
                        doesNotExist(name);
                    }
                } else {
                    invalidNumberArgs("set");
                }
                break;
            case "del":
                if(l == 1) {
                    String name = withoutArgs[0];
                    if (checkExists(name)) {
                        removeWaypoint(name);
                        Waypoint.saveAll();
                        waypointDeleted(name);
                    } else {
                        doesNotExist(name);
                    }
                } else {
                    invalidNumberArgs("del");
                }
                break;
            case "delnear":
                if(l != 0) {
                    invalidNumberArgs("delnear");
                    break;
                }
                double smallestDiff = Double.MAX_VALUE;
                Waypoint smallestDiffWp = null;
                for(Waypoint waypoint : Waypoint.WAYPOINTS) {
                    if(!Objects.equals(curWorld(), waypoint.getWorld())) continue;
                    double dis = BlockUtils.getPlayerBlockDistance(waypoint.getX(), waypoint.getY(), waypoint.getZ());
                    if(dis < smallestDiff) {
                        smallestDiff = dis;
                        smallestDiffWp = waypoint;
                    }
                }
                if(smallestDiffWp != null) {
                    Waypoint.WAYPOINTS.remove(smallestDiffWp);
                    Waypoint.saveAll();
                    waypointDeleted(smallestDiffWp.getName());
                } else {
                    noWaypoints();
                }
                break;
            case "list":
                Chat.sendCmd("Listing all waypoints in current world:");
                for(Waypoint waypoint : Waypoint.WAYPOINTS) {
                    if(!Objects.equals(curWorld(), waypoint.getWorld())) continue;
                    Chat.sendCmd(F.f("<UNDERLINE>" + waypoint.getName() + "</UNDERLINE>: ") + waypoint.getX() + ", " + waypoint.getY() + ", " + waypoint.getZ());
                }
                break;
            case "listall":
                Chat.sendCmd("Listing all waypoints:");
                for(Waypoint waypoint : Waypoint.WAYPOINTS) {
                    Chat.sendCmd(F.f("<UNDERLINE>" + waypoint.getName() + "</UNDERLINE>: ") + waypoint.getX() + ", " + waypoint.getY() + ", " + waypoint.getZ());
                }
                break;
            default:
                Chat.sendError("Invalid operation!");
        }
    }

    void removeWaypoint(String name) {
        Iterator<Waypoint> waypointIterator = Waypoint.WAYPOINTS.iterator();
        while(waypointIterator.hasNext()) {
            Waypoint next = waypointIterator.next();
            if(next.getName().equals(name) && Objects.equals(curWorld(), next.getWorld())) {
                waypointIterator.remove();
                break;
            }
        }
    }

    boolean checkExists(String name) {
        for(Waypoint waypoint : Waypoint.WAYPOINTS) {
            if(waypoint.getName().equals(name) && Objects.equals(curWorld(), waypoint.getWorld())) {
                return true;
            }
        }
        return false;
    }

    String curWorld() {
        if(Waypoint.getConfig().getBoolean("Per World/Server", true)) {
            return World.getUUID().toString();
        } else {
            return null;
        }
    }

    boolean checkTags(String name) {
        try {
            F.f(name);
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

    Waypoint fromPlayer(String string) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        return new Waypoint(string, player.posX, player.posY, player.posZ, World.getUUID().toString());
    }

    void waypointDeleted(String name) {
        Chat.sendSuccess("Deleted waypoint with name '" + F.f(name) + "'!");
    }

    void waypointAdded(Waypoint waypoint) {
        Chat.sendSuccess("Waypoint with name '"
                + F.f(waypoint.getName()) + "' added at '"
                + waypoint.getX() + ", "
                + waypoint.getY() + ", "
                + waypoint.getZ() + "'!");
    }

    void waypointEdited(Waypoint waypoint) {
        Chat.sendSuccess("Waypoint with name '"
                + F.f(waypoint.getName()) + "' moved to '"
                + waypoint.getX() + ", "
                + waypoint.getY() + ", "
                + waypoint.getZ() + "'!");
    }

    void noWaypoints() {
        Chat.sendError("No waypoints exist!");
    }

    void alreadyExists(String name) {
        Chat.sendError("A waypoint with the name '"+ F.f(name) + "' already exists!");
    }

    void doesNotExist(String name) {
        Chat.sendError("No waypoint exists with the name '"+ F.f(name) + "'!");
    }

    void invalidNumberArgs(String op) {
        Chat.sendError("Invalid number of arguments for operation: '" + op + "'!");
    }

    void invalidCoordinates() {
        Chat.sendError("Invalid coordinates!");
    }
}
