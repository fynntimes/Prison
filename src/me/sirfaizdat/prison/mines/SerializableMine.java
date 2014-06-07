/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author SirFaizdat
 */
public class SerializableMine implements Serializable {

	private static final long serialVersionUID = 1L;
	public String name;
	public String world;
	public int minX;
	public int minY;
	public int minZ;
	public int maxX;
	public int maxY;
	public int maxZ;
	public HashMap<String, Block> blocks = new HashMap<String, Block>();
}
