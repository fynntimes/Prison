/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks;

/**
 * @author SirFaizdat
 */
public class Rank {

	private int id;
	private String name;
	private double price;
	private String prefix;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
