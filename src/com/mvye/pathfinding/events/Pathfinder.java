package com.mvye.pathfinding.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pathfinder implements Listener {
    private static List<Node> open = new ArrayList<>();
    private static List<Node> closed = new ArrayList<>();
    private static List<Node> path = new ArrayList<>();
    private static Node current;

    public static World world;

    public static int endX;
    public static int endY;
    public static int endZ;

    public static final int CARDINAL_COST = 10;
    public static final int DIAGNOAL_COST_INCREASE = 4;
    public static final int HEIGHT_CHANCE_COST_INCREASE = 3;

    @EventHandler
    public static void onProjectileHitEvent(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            if (event.getEntity() instanceof Snowball) {
                if (event.getHitBlock() != null) {
                    Player player = (Player) event.getEntity().getShooter();
                    Block block = event.getHitBlock();
                    if (player.getLocation().subtract(0,1,0).getBlock().getType() != Material.WHITE_WOOL) {
                        return;
                    }
                    if (block.getType() != Material.WHITE_WOOL) {
                        return;
                    }
                    if (player.getLocation().distance(block.getLocation()) > 50.0) {
                        return;
                    }
                    open.clear();
                    closed.clear();
                    path.clear();
                    world = player.getWorld();
                    createShortestPath(player.getLocation(), block.getLocation());
                    addCarpetToPath();
                }
            }
        }
    }

    private static void addCarpetToPath() {
        Node currentNode;
        Location currentLocation;
        for (int i = 0; i < path.size(); i++) {
            currentNode = path.get(i);
            currentLocation = new Location(world, currentNode.getX(), currentNode.getY()+1, currentNode.getZ());
            currentLocation.getBlock().setType(Material.BLACK_CARPET);
        }
    }

    private static void createShortestPath(Location initialLocation, Location goalLocation1) {
        endX = goalLocation1.getBlockX();
        endY = goalLocation1.getBlockY();
        endZ = goalLocation1.getBlockZ();
        current = new Node(initialLocation.getBlockX(), initialLocation.getBlockY()-1, initialLocation.getBlockZ());
        current.setParent(null);
        current.setParameters(0,0);
        int counter = 0;
        open.add(current);
        while (true) {
            //counter++;
            if (open.isEmpty()) { break; }
            current = open.remove(0);
            closed.add(current);
            if (current.getX() == endX && current.getZ() == endZ) { break;}
            getNeighbors();
        }
        path.add(0, current);
        while (current.getParent() != null) {
            current = current.getParent();
            path.add(0, current);
        }
    }

    private static void getNeighbors() {
        Node node;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) { continue; }
                node = new Node(current.getX()+x, current.getZ()+z);
                Location nodeLocation = new Location(world, node.getX(), current.getY(), node.getZ());
                Location nodeBlockLocation;
                Block b = world.getBlockAt(nodeLocation);
                if (b.getType() == Material.WHITE_WOOL) {
                    nodeBlockLocation = b.getLocation();
                }
                else {
                    if (nodeLocation.clone().add(0,1,0).getBlock().getType() == Material.WHITE_WOOL) {
                        nodeBlockLocation = nodeLocation.clone().add(0,1,0);
                    }
                    else if (nodeLocation.clone().subtract(0,1,0).getBlock().getType() == Material.WHITE_WOOL) {
                        nodeBlockLocation = nodeLocation.clone().subtract(0,1,0);
                    }
                    else {
                        continue;
                    }
                }
                int differenceInHeight = nodeLocation.getBlockY()-nodeBlockLocation.getBlockY();
                if (Math.abs(differenceInHeight) > 1) { continue; }
                else {
                    node.setY(nodeBlockLocation.getBlockY());
                }
                if (nodeBlockLocation.getBlock().getType() != Material.WHITE_WOOL) { continue; }
                if (nodeBlockLocation.getBlock().getType() == Material.AIR) { continue; }
                if (nodeInList(node, closed)) { continue; }
                double g = calculateG(node, x, z);
                double h = calculateH(node);
                node.setParameters(g, h);
                node.setParent(current);
                if (!nodeInList(node, open)) {
                    open.add(node);
                } else {
                    Node foundNode = getNode(open, node);
                    if (foundNode.getF() > node.getF()) {
                        open.remove(foundNode);
                        open.add(node);
                    }
                }
            }
        }
        Collections.sort(open);
    }

    private static boolean nodeInList(Node node, List<Node> array) {
        boolean isNodeInList = array.stream().anyMatch((n) -> (n.getX() == node.getX() && n.getY() == node.getY() && n.getZ() == node.getZ()));
        return isNodeInList;
    }

    private static double calculateG(Node node, double x, double z) {
        double g = current.getG() + CARDINAL_COST;
        if (x != 0 && z != 0)
            g += DIAGNOAL_COST_INCREASE;
        if (node.getY() != current.getY())
            g += HEIGHT_CHANCE_COST_INCREASE;
        return g;
    }

    private static double calculateH(Node node) {
        double distance = Math.pow(endX-node.getX(), 2) +
                Math.pow(endY-node.getY(), 2) +
                Math.pow(endZ-node.getZ(), 2);
        return Math.sqrt(distance);
    }

    public static Node getNode(List<Node> array, Node node) {
        int size = array.size();
        Node currentNode;
        for (int i = 0; i < size; i++) {
            currentNode = array.get(i);
            if (currentNode.getX() == node.getX()
                    && currentNode.getY() == node.getY()
                    && currentNode.getZ() == node.getZ())
                return currentNode;
        }
        return null;
    }
}
