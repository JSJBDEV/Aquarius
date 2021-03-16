package space.bbkr.aquarius.fabric;

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

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import space.bbkr.aquarius.common.Aquarius;
import space.bbkr.aquarius.common.AquariusClient;
import space.bbkr.aquarius.common.ChorusConduitRenderer;
import space.bbkr.aquarius.common.TridentBeamRenderer;

public class AquariusClientEntry {
    public static void initClient() {
        BlockEntityRendererRegistry.INSTANCE.register(Aquarius.CHORUS_CONDUIT_BE, ChorusConduitRenderer::new);
        EntityRendererRegistry.INSTANCE.register(Aquarius.TRIDENT_BEAM.get(), (dispatcher, context) -> new TridentBeamRenderer(dispatcher));
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((provider, registry) -> {
            registry.register(new Identifier(Aquarius.MODID, "entity/chorus_conduit/base"));
            registry.register(new Identifier(Aquarius.MODID, "entity/chorus_conduit/cage"));
            registry.register(new Identifier(Aquarius.MODID, "entity/chorus_conduit/wind"));
            registry.register(new Identifier(Aquarius.MODID, "entity/chorus_conduit/wind_vertical"));
            registry.register(new Identifier(Aquarius.MODID, "entity/chorus_conduit/open_eye"));
            registry.register(new Identifier(Aquarius.MODID, "entity/chorus_conduit/closed_eye"));
        });
        AquariusClient.onInitializeClient();
    }
}
