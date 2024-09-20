package com.mirea.elizarovnm.mireaproject;

import com.yandex.mapkit.geometry.Point;
public class Establishment {
    private String name;
    private String description;
    private Point location;

    public Establishment(String name, String description, Point location) {
        this.name = name;
        this.description = description;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Point getLocation() {
        return location;
    }
}
