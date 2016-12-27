package me.sirfaizdat.prison.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class JsonRank {
	public JsonRank fromJson(String json)
	{
		Gson g = new Gson();
		JsonRank rank = g.fromJson(json, JsonRank.class);
		return rank;
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
	public int id;
    public String name;
    public double price;
    public String prefix;
}
