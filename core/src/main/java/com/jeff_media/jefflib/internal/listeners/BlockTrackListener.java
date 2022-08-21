/*
 *     Copyright (c) 2022. JEFF Media GbR / mfnalex et al.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.jeff_media.jefflib.internal.listeners;

import com.jeff_media.jefflib.BlockTracker;
import com.jeff_media.jefflib.JeffLib;
import com.jeff_media.jefflib.internal.annotations.Internal;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.Plugin;

/**
 * Keeps track of player placed blocks
 */
@SuppressWarnings("MethodMayBeStatic")
@Internal
public final class BlockTrackListener implements Listener {

    private final Plugin plugin = JeffLib.getPlugin();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(final BlockPlaceEvent event) {
        if (!BlockTracker.isTrackedBlockType(event.getBlock().getType())) return;
        BlockTracker.setPlayerPlacedBlock(event.getBlock(), true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(final BlockBreakEvent event) {
        if (BlockTracker.isPlayerPlacedBlock(event.getBlock())) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> BlockTracker.setPlayerPlacedBlock(event.getBlock(), false), 1L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStructureGrow(final StructureGrowEvent event) {
        for (final BlockState blockState : event.getBlocks()) {
            final Block block = blockState.getBlock();
            BlockTracker.setPlayerPlacedBlock(block, false);
        }
    }


}
