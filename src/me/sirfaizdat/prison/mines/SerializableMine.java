/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines;

import java.io.Serializable;
import java.util.ArrayList;
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
    public int spawnX;
    public int spawnY;
    public int spawnZ;
    public float spawnPitch;
    public float spawnYaw;
    public HashMap<String, Block> blocks = new HashMap<String, Block>();
    public ArrayList<String> ranks = new ArrayList<String>();
}
