package space.bbkr.aquarius.forge;

//import dev.adox.bundlesplus.common.BundlesPlusMod;
//import dev.adox.bundlesplus.common.init.BundleItems;
//import dev.adox.bundlesplus.common.init.BundleResources;
//import dev.adox.bundlesplus.common.util.BundleItemUtils;
//import io.github.prospector.modmenu.api.ModMenuApi;
//import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.client.renderer.item.ItemPropertyFunction;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.ItemStack;
//import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Items;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import space.bbkr.aquarius.common.Aquarius;
import space.bbkr.aquarius.common.AquariusClient;
import space.bbkr.aquarius.common.ChorusConduitRenderer;
import space.bbkr.aquarius.common.TridentBeamRenderer;

import static space.bbkr.aquarius.common.Aquarius.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AquariusForgeClient {
    public static void initClient(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(Aquarius.CHORUS_CONDUIT_BE, ChorusConduitRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Aquarius.TRIDENT_BEAM.get(), TridentBeamRenderer::new);
//         SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE
//        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((provider, registry) -> {
//            registry.register(new Identifier(Aquarius.MODID, "entity/chorus_conduit/base"));
//            registry.register(new Identifier(Aquarius.MODID, "entity/chorus_conduit/cage"));
//            registry.register(new Identifier(Aquarius.MODID, "entity/chorus_conduit/wind"));
//            registry.register(new Identifier(Aquarius.MODID, "entity/chorus_conduit/wind_vertical"));
//            registry.register(new Identifier(Aquarius.MODID, "entity/chorus_conduit/open_eye"));
//            registry.register(new Identifier(Aquarius.MODID, "entity/chorus_conduit/closed_eye"));
//        });
        AquariusClient.onInitializeClient();
    }

    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGH)
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        event.addSprite(new Identifier(Aquarius.MODID, "entity/chorus_conduit/base"));
        event.addSprite(new Identifier(MODID, "entity/chorus_conduit/cage"));
        event.addSprite(new Identifier(MODID, "entity/chorus_conduit/wind"));
        event.addSprite(new Identifier(MODID, "entity/chorus_conduit/wind_vertical"));
        event.addSprite(new Identifier(MODID, "entity/chorus_conduit/open_eye"));
        event.addSprite(new Identifier(MODID, "entity/chorus_conduit/closed_eye"));
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
