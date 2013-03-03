package com.ov3rk1ll.nse.config;

import org.bukkit.Location;

public class WorldLocation {
	private String world;
	private int x;
	private int y;
	private int z;
	
	public WorldLocation(String world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public WorldLocation(String world, Location location) {
		this.world = world;
		this.x = location.getBlockX();
		this.y = location.getBlockY();
		this.z = location.getBlockZ();
	}
	
	public WorldLocation(String line) {
		String[] data = line.split(" ");
		this.x = Integer.valueOf(data[0]);
		this.y = Integer.valueOf(data[1]);
		this.z = Integer.valueOf(data[2]);
		this.world = data[3];
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
	@Override
	public String toString() {
		return x + " " + y + " " + z + " " + world;
	}
	
}
