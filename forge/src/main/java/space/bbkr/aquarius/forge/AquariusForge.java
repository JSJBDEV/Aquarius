package space.bbkr.aquarius.forge;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraft.item.Items;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import space.bbkr.aquarius.common.Aquarius;

import static space.bbkr.aquarius.common.Aquarius.MODID;

@Mod(MODID)
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AquariusForge {


    public AquariusForge() {
        EventBuses.registerModEventBus(MODID, FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(AquariusForgeClient::initClient);
        MinecraftForge.EVENT_BUS.addListener(AquariusEvents::LootTableLoading);
        MinecraftForge.EVENT_BUS.addListener(AquariusForgeClient::onTextureStitch);
        Aquarius.onInitialize();
    }

    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGH)
    public void LootTableLoading(LootTableLoadEvent event) {
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