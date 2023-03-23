/*
 * Copyright (c) 2023. JEFF Media GbR / mfnalex et al.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.jeff_media.jefflib.data.worldboundingbox;

import com.jeff_media.jefflib.LocationUtils;
import com.jeff_media.jefflib.exceptions.InvalidRegionDefinitionException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents a region inside a {@link World}
 */
public abstract class WorldBoundingBox implements ConfigurationSerializable {

    static WorldBoundingBox fromConfigurationSection(@Nonnull final ConfigurationSection section, @Nullable final String path) {

        final String worldPath = path == null ? "world" : path + ".world";
        final String pointsPath = path == null ? "points" : path + ".points";


        if (path != null && !section.isConfigurationSection(path)) {
            throw new InvalidRegionDefinitionException("No valid region definition found at given path.");
        }

        if (!section.isString(worldPath))
            throw new InvalidRegionDefinitionException("Region definitions must contain a world.");
        final World world = Bukkit.getWorld(section.getString(worldPath));
        if (world == null)
            throw new InvalidRegionDefinitionException("World " + section.getString(worldPath) + " not found.");

        if (section.isList(pointsPath)) {
            return getUnknownWorldBoundingBoxFromConfigurationSection(section, section.getList(pointsPath), world);
        }

        return getCuboidWorldBoundingBoxFromConfigurationSection(section, path, world);
    }

    @Nonnull
    private static WorldBoundingBox getUnknownWorldBoundingBoxFromConfigurationSection(@Nonnull final ConfigurationSection section, @Nonnull final List<?> points, @Nonnull final World world) {
        final List<Vector> vectors = new ArrayList<>();
        for (final Object point : points) {
            if (!(point instanceof Vector)) {
                throw new InvalidRegionDefinitionException("Points list contains an object not instanceof org.bukkit.util.Vector");
            }
            vectors.add((Vector) point);
        }
        if (vectors.size() < 2) {
            throw new InvalidRegionDefinitionException("Region definitions must at least contain two points.");
        }
        if (vectors.size() == 2) {
            return new CuboidWorldBoundingBox(world, BoundingBox.of(vectors.get(0), vectors.get(1)));
        }
        return PolygonWorldBoundingBox.fromVectors(world, vectors);
    }

    @Nonnull
    private static CuboidWorldBoundingBox getCuboidWorldBoundingBoxFromConfigurationSection(@Nonnull final ConfigurationSection section, @Nullable final String path, @Nonnull final World world) {
        final String minPath = path == null ? "min" : path + ".min";
        final String maxPath = path == null ? "max" : path + ".max";

        if (!section.contains(minPath) || !section.isConfigurationSection(minPath) || !section.contains(maxPath) || !section.isConfigurationSection(maxPath)) {
            throw new InvalidRegionDefinitionException("Invalid region defined. Cuboid regions must contain a min and max position.");
        }

        final Location min = LocationUtils.getLocationFromSection(section.getConfigurationSection(minPath), world);
        final Location max = LocationUtils.getLocationFromSection(section.getConfigurationSection(maxPath), world);
        return new CuboidWorldBoundingBox(world, BoundingBox.of(min, max));
    }

    abstract boolean contains(Location location);

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("world", getWorld());
        final List<Vector> points = new ArrayList<>(getPoints());
        return map;
    }

    @Nonnull
    abstract World getWorld();

    abstract List<Vector> getPoints();
}
