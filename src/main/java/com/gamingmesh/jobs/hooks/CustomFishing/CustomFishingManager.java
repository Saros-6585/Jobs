package com.gamingmesh.jobs.hooks.CustomFishing;

import com.gamingmesh.jobs.Jobs;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.momirealms.customfishing.api.event.FishingLootSpawnEvent;
import net.momirealms.customfishing.api.mechanic.loot.LootType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class CustomFishingManager implements Listener {

    private static String lastFish;
    private static long time;
    private final Jobs plugin;

    public CustomFishingManager() {
        this.plugin = Jobs.getInstance();
        registerListener();
    }

    @EventHandler
    public void onFishLootSpawn(FishingLootSpawnEvent event) {
        if (event.getLoot().type() != LootType.ITEM || event.getLoot().id().equals("vanilla")) return;
        lastFish = event.getLoot().id();
        time = System.currentTimeMillis();
    }

    public static String getFish() {
        if (time + 60 < System.currentTimeMillis())
            return null;
        return lastFish;
    }

    public boolean check() {
        Plugin cf = Bukkit.getPluginManager().getPlugin("CustomFishing");
        if (cf == null) return false;

        CMIMessages.consoleMessage("&e[Jobs] &6CustomFishing was found - Enabling capabilities.");
        return true;
    }

    public void registerListener() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


}
