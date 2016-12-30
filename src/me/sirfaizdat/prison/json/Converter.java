package me.sirfaizdat.prison.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import me.sirfaizdat.prison.mines.SerializableMine;
import me.sirfaizdat.prison.mines.Block;
import me.sirfaizdat.prison.ranks.SerializableRank;

public class Converter {
	public static JsonMine convertMineToJson(SerializableMine sm)
	{
		JsonMine mine = new JsonMine();
		mine.name = sm.name;
		mine.world = sm.world;
		mine.minX = sm.minX;
		mine.minY = sm.minY;
		mine.minZ = sm.minZ;
		mine.maxX = sm.maxX;
		mine.maxY = sm.maxY;
		mine.maxZ = sm.maxZ;
		HashMap<String, Double> blocks = new HashMap<>();
		for (Map.Entry<String, Block> b : sm.blocks.entrySet())
		{
			blocks.put(b.getKey(), b.getValue().getChance());
		}
		mine.blocks = blocks;
		mine.ranks = sm.ranks;
		return mine;
	}
	public static JsonRank convertRankToJson(SerializableRank sr)
	{
		JsonRank rank = new JsonRank();
		rank.name = sr.name;
		rank.id = sr.id;
		rank.prefix = sr.prefix;
		rank.price = sr.price;
		return rank;
	}
	public static JsonMine convertMineToJson(File file) throws IOException, ClassNotFoundException
	{
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        SerializableMine sm = (SerializableMine) in.readObject();
        in.close();
        fileIn.close();
		JsonMine mine = new JsonMine();
		mine.name = sm.name;
		mine.world = sm.world;
		mine.minX = sm.minX;
		mine.minY = sm.minY;
		mine.minZ = sm.minZ;
		mine.maxX = sm.maxX;
		mine.maxY = sm.maxY;
		mine.maxZ = sm.maxZ;
		HashMap<String, Double> blocks = new HashMap<>();
		for (Map.Entry<String, Block> b : sm.blocks.entrySet())
		{
			blocks.put(b.getKey(), b.getValue().getChance());
		}
		mine.blocks = blocks;
		mine.ranks = sm.ranks;
		return mine;
	}
	public static JsonRank convertRankToJson(File file) throws IOException, ClassNotFoundException
	{
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        SerializableRank sr = (SerializableRank)in.readObject();
        in.close();
        fileIn.close();
		JsonRank rank = new JsonRank();
		rank.name = sr.name;
		rank.id = sr.id;
		rank.prefix = sr.prefix;
		rank.price = sr.price;
		return rank;
	}
}
