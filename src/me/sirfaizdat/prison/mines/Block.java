package me.sirfaizdat.prison.mines;

import java.io.Serializable;


public class Block implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private byte data;
	private double chance;
	
	public Block(int id) {
		this.id = id;
		this.data = 0;
	}
	
	public Block(int id, byte data) {
		this.id = id;
		this.data = data;
	}
	
	public int getId() {
		return id;
	}
	
	public byte getData() {
		return data;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setData(byte data) {
		this.data = data;
	}
	
	public double getChance() {
		return chance;
	}
	
	public void setChance(double chance) {
		this.chance = chance;
	}

	public String toString() {
		return id + ":" + data; 
	}
	
	
}
