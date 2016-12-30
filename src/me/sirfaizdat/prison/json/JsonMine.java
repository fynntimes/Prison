package me.sirfaizdat.prison.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

public class JsonMine {
	public JsonMine fromJson(String json)
	{
		Gson g = new Gson();
		JsonMine mine = g.fromJson(json, JsonMine.class);
		return mine;
	}
	public String toJson()
	{
		Gson g = new Gson();
		return g.toJson(this);
	}
	public void toFile(File file) throws IOException
	{
		FileWriter w = new FileWriter(file);
		Gson g = new Gson();
		g.toJson(this, w);
	}
    public String name;
    public String world;
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;
    public HashMap<String, Double> blocks = new HashMap<>();
    public ArrayList<String> ranks = new ArrayList<>();
}
