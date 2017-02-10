package me.sirfaizdat.prison.json;

import me.sirfaizdat.prison.mines.Block;
import me.sirfaizdat.prison.mines.SerializableMine;
import me.sirfaizdat.prison.ranks.SerializableRank;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class Converter {
    public static JsonMine convertMineToJson(SerializableMine sm) {
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
        for (Map.Entry<String, Block> b : sm.blocks.entrySet()) {
            blocks.put(b.getKey(), b.getValue().getChance());
        }
        mine.blocks = blocks;
        mine.ranks = sm.ranks;
        return mine;
    }

    public static JsonRank convertRankToJson(SerializableRank sr) {
        JsonRank rank = new JsonRank();
        rank.name = sr.name;
        rank.id = sr.id;
        rank.prefix = sr.prefix;
        rank.price = sr.price;
        return rank;
    }

    public static JsonMine convertMineToJson(File file) throws IOException, ClassNotFoundException {
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
        for (Map.Entry<String, Block> b : sm.blocks.entrySet()) {
            blocks.put(b.getKey(), b.getValue().getChance());
        }
        mine.blocks = blocks;
        mine.ranks = sm.ranks;
        return mine;
    }

    public static JsonRank convertRankToJson(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        SerializableRank sr = (SerializableRank) in.readObject();
        in.close();
        fileIn.close();
        JsonRank rank = new JsonRank();
        rank.name = sr.name;
        rank.id = sr.id;
        rank.prefix = sr.prefix;
        rank.price = sr.price;
        return rank;
    }

    public static SerializableMine tryLoadMine(Object mine) {
        SerializableMine result = new SerializableMine();
        if (mine.getClass().getName().equals("me.sirfaizdat.prison.mines.SerializableMine")) {
            result = (SerializableMine) mine;
        } else if (mine.getClass().getName()
            .equals("me.sirfaizdat.prison.mines.entities.SerializableMine")) {
            me.sirfaizdat.prison.mines.entities.SerializableMine mine2 =
                (me.sirfaizdat.prison.mines.entities.SerializableMine) mine;
            HashMap<String, me.sirfaizdat.prison.mines.entities.Block> map = mine2.blocks;
            HashMap<String, Double> blocks = new HashMap<>();
            for (Map.Entry<String, me.sirfaizdat.prison.mines.entities.Block> b : map.entrySet()) {
                result.blocks.put(b.getKey(),
                    new Block(b.getValue().getId(), b.getValue().getData())
                        .setChance(b.getValue().getChance()));
            }
            result.minX = mine2.minX;
            result.minY = mine2.minY;
            result.minZ = mine2.minZ;
            result.maxX = mine2.maxX;
            result.maxY = mine2.maxY;
            result.maxZ = mine2.maxZ;
            result.ranks = mine2.ranks;
        } else {
            result = null;
        }
        return result;
    }
}
