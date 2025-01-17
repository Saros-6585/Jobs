package com.gamingmesh.jobs.listeners;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.actions.CustomFishingInfo;
import com.gamingmesh.jobs.container.ActionType;
import net.momirealms.customfishing.api.event.FishingLootSpawnEvent;
import net.momirealms.customfishing.api.mechanic.loot.LootType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static com.gamingmesh.jobs.listeners.JobsPaymentListener.payForItemDurabilityLoss;
import static com.gamingmesh.jobs.listeners.JobsPaymentListener.payIfCreative;

public class JobsPaymentListenerForCustomFishing implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFishLootSpawn(FishingLootSpawnEvent event) {

        Player player = event.getPlayer();

        if (!Jobs.getGCManager().canPerformActionInWorld(player.getWorld()))
            return;

        // check if in creative
        if (!payIfCreative(player))
            return;

        if (!Jobs.getPermissionHandler().hasWorldPermission(player, player.getWorld().getName()))
            return;

        // check if player is riding
        if (Jobs.getGCManager().disablePaymentIfRiding && player.isInsideVehicle() && !player.getVehicle().getType().equals(EntityType.BOAT))
            return;

        if (!payForItemDurabilityLoss(player))
            return;

        if (event.getLoot().type() != LootType.ITEM) return;
        if (event.getLoot().id().equals("vanilla")) return;

        Entity caughtItem = event.getEntity();
        Jobs.action(Jobs.getPlayerManager().getJobsPlayer(player), new CustomFishingInfo(event.getLoot().id(), ActionType.CUSTOMFISHING), caughtItem);
    }

}
