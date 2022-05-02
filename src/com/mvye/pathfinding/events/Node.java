package com.mvye.pathfinding.events;

public class Node implements Comparable {
    Node parent;
    int x, y, z;
    double h, g, f;

    public Node(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Node(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setParameters(double g, double h) {
        this.g = g;
        this.h = h;
        this.f = g+h;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Node getParent() {
        return this.parent;
    }

    public double getG() {
        return this.g;
    }

    public double getF() {
        return this.f;
    }

    @Override
    public int compareTo(Object o) {
        Node other =(Node) o;
        return (int)((this.f) - (other.getF()));
    }
}
