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

package com.jeff_media.jefflib;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Provides a builder for ItemStacks
 */
public final class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta meta;

    /**
     * Creates a new ItemBuilder with the given material and an amount of 1
     *
     * @param mat Material
     */
    public ItemBuilder(final Material mat) {
        this(mat, 1);
    }

    /**
     * Creates a new ItemBuilder with the given material and amount
     *
     * @param mat    Material
     * @param amount Amount
     */
    public ItemBuilder(final Material mat, final int amount) {
        item = new ItemStack(mat, amount);
        meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(mat);
    }

    /**
     * Returns the built ItemStack
     *
     * @return ItemStack
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Sets the amount
     *
     * @param amount Amount
     * @return ItemBuilder instance
     */
    public ItemBuilder setAmount(final int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Sets the item's display name
     *
     * @param name Item's display name (or null)
     * @return ItemBuilder instance
     */
    public ItemBuilder setName(@Nullable final String name) {
        meta.setDisplayName(name);
        return this;
    }

    /**
     * Sets the item's lore
     *
     * @param lore Lore (or null)
     * @return ItemBuilder instance
     */
    public ItemBuilder setLore(@Nullable final List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    /**
     * Sets the item's lore
     *
     * @param lore Lore (or null)
     * @return ItemBuilder instance
     */
    public ItemBuilder setLore(@Nullable final String... lore) {
        if (lore == null) {
            meta.setLore(null);
        } else {
            meta.setLore(Arrays.asList(lore));
        }
        return this;
    }

    /**
     * Sets the item's Custom Model Data
     *
     * @param data Custom Model Data as int value, or null
     * @return ItemBuilder instance
     */
    public ItemBuilder setCustomModelData(@Nullable final Integer data) {
        meta.setCustomModelData(data);
        return this;
    }


}
