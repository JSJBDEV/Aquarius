package space.bbkr.aquarius.forge;

import net.minecraft.item.Items;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static space.bbkr.aquarius.common.Aquarius.MODID;

@Mod.EventBusSubscriber
public class AquariusEvents {
    @SubscribeEvent
    public static void LootTableLoading(LootTableLoadEvent event) {
        LootManager lootManager = event.getLootTableManager();
        Identifier id = event.getName();

        Identifier pool = new Identifier("minecraft", "chests/shipwreck_treasure");
        if (id.equals(pool)) {
            event.getTable().addPool(LootPool.builder().
                with(ItemEntry.builder(Items.HEART_OF_THE_SEA)
                    .conditionally(RandomChanceLootCondition.builder(0.1f)))
                .with(ItemEntry.builder(Items.NAUTILUS_SHELL)
                    .conditionally(RandomChanceLootCondition.builder(0.33f)))
                .build());
        }

    }
}
