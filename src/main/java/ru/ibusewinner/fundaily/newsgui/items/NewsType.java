package ru.ibusewinner.fundaily.newsgui.items;

import org.bukkit.Material;

public class NewsType {
    private String id;
    private String name;
    private Material material;
    private int amount;
    private int modelData;

    public NewsType(String id, String name, Material material, int amount, int modelData) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.amount = amount;
        this.modelData = modelData;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public int getModelData() {
        return modelData;
    }

    public int getAmount() {
        return amount;
    }
}
