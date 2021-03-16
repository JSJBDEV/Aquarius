package space.bbkr.aquarius.fabric;

//import dev.adox.bundlesplus.common.BundlesPlusMod;
//import dev.adox.bundlesplus.common.init.BundleItems;
//import dev.adox.bundlesplus.common.init.BundleResources;
//import dev.adox.bundlesplus.common.util.BundleItemUtils;
//import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.client.renderer.item.ItemPropertyFunction;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import space.bbkr.aquarius.common.Aquarius;

//import static dev.adox.bundlesplus.common.init.BundleResources.MOD_ID;

public class AquariusFabric {
//    private static final ResourceLocation SHULKER_BOX_CAP = new ResourceLocation(MODID, "shulker_box_drop_in");




    public static void initialize() {
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (id.equals(new Identifier("minecraft", "chests/shipwreck_treasure"))) {
                supplier.withPool(FabricLootPoolBuilder.builder()
                    .withEntry(ItemEntry.builder(Items.HEART_OF_THE_SEA)
                        .conditionally(RandomChanceLootCondition.builder(0.1f)).build()
                    ).withEntry(ItemEntry.builder(Items.NAUTILUS_SHELL).conditionally(RandomChanceLootCondition.builder(0.33f)).build()).build());
            }
        });

        Aquarius.onInitialize();
    }

    public AquariusFabric() {
    }


}
