package com.qualityplus.pets.listener;

import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.base.pet.entity.tracket.PetArmorStandTracker;
import com.qualityplus.pets.base.pet.tracker.PetEntityTracker;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@Component
public final class PetChunkListener implements Listener {
    private @Inject Plugin plugin;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunk(ChunkLoadEvent e) {

        Chunk chunk = e.getChunk();
        //Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
        for(PetEntity entity : PetEntityTracker.values()){
            Location location = entity.getSpawn();

            if(!isIn(chunk.getChunkSnapshot(), location)) return;

            Bukkit.getConsoleSender().sendMessage("Cargando chunk del pet");

            entity.load();
        }
        //});

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunk(ChunkUnloadEvent e) {

        //Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
        for(Entity entity : e.getChunk().getEntities()){
            if(!(entity instanceof ArmorStand)) return;

            Optional<PetEntity> minionEntity = PetArmorStandTracker.getByID(entity.getUniqueId());

            if(!minionEntity.isPresent()) continue;

            Bukkit.getConsoleSender().sendMessage("Descargando chunk del pet");

            minionEntity.get().unload();
            //e.getChunk().unload();

        }
        //});
    }


    public boolean isIn(ChunkSnapshot chunk, Location l) {
        Chunk loc = l.getChunk();

        return loc.getWorld().getName().equals(chunk.getWorldName()) && loc.getX() == chunk.getX()
                && loc.getZ() == chunk.getZ();
    }
}