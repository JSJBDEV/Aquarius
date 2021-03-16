package space.bbkr.aquarius.common;

import me.shedaniel.architectury.hooks.TagHooks;
import me.shedaniel.architectury.networking.NetworkChannel;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import space.bbkr.aquarius.common.network.AirSwimmingC2SMsg;

import java.util.function.Supplier;

import static me.shedaniel.architectury.event.events.TickEvent.PLAYER_POST;

public class Aquarius {
    public static final String MODID = "aquarius";
    public static final @NotNull DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(MODID, Registry.ENTITY_TYPE_KEY);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MODID, Registry.ITEM_KEY);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MODID, Registry.BLOCK_KEY);
    public static final DeferredRegister<StatusEffect> EFFECTS = DeferredRegister.create(MODID, Registry.MOB_EFFECT_KEY);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(MODID, Registry.ENCHANTMENT_KEY);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(MODID, Registry.BLOCK_ENTITY_TYPE_KEY);

    public static NetworkChannel NETWORK;

    public static final Block CHORUS_CONDUIT = register("chorus_conduit", new ChorusConduitBlock(AbstractBlock.Settings.of(Material.GLASS).strength(3.0F, 3.0F).luminance(state -> 15)), ItemGroup.MISC);
    public static final Item FLIPPERS = register("flippers", new ArmorItem(ArmorMaterials.TURTLE, EquipmentSlot.FEET, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final Item PRISMARINE_ROD = register("prismarine_rod", new Item(new Item.Settings().group(ItemGroup.MISC)));
    public static final BlockEntityType<ChorusConduitBlockEntity> CHORUS_CONDUIT_BE = register("chorus_conduit", ChorusConduitBlockEntity::new, CHORUS_CONDUIT);
    public static final Tag<Block> CHORUS_CONDUIT_ACTIVATORS = TagHooks.getBlockOptional(new Identifier(MODID, "chorus_conduit_activators"));

    public static final StatusEffect ATLANTEAN = register("atlantean", new AquariusStatusEffect(StatusEffectType.BENEFICIAL, 0x1dd186));

    public static final Enchantment GUARDIAN_SIGHT = register("guardian_sight", new GuardianSightEnchantment());

    public static final RegistrySupplier<EntityType<TridentBeamEntity>> TRIDENT_BEAM = register("trident_beam", SpawnGroup.MISC, EntityDimensions.changing(0.5F, 0.5F), ((entityType, world) -> new TridentBeamEntity(world)));

    public static void onInitialize() {
        BLOCKS.register();
        ITEMS.register();
        ENTITIES.register();
        EFFECTS.register();
        ENCHANTMENTS.register();
        BLOCK_ENTITIES.register();

        NETWORK = NetworkChannel.create(new Identifier(MODID, "network_channel"));
        NETWORK.register(AirSwimmingC2SMsg.class,
            AirSwimmingC2SMsg::encode, AirSwimmingC2SMsg::decode,
            AirSwimmingC2SMsg::onMessageReceived);

        PLAYER_POST.register(playerEntity -> {
            ItemStack stackFeet = playerEntity.getEquippedStack(EquipmentSlot.FEET);
            if (stackFeet.getItem() == Aquarius.FLIPPERS) {
                if (playerEntity.isTouchingWaterOrRain()) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 20, 0, true, false, true));
                    if (!playerEntity.world.isClient) {
                        if (playerEntity.getRandom().nextInt(20) == 0) {
                            stackFeet.damage(1, playerEntity, (entity) -> entity.sendEquipmentBreakStatus(EquipmentSlot.FEET));
                        }
                    }
                } else {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 0, true, false, true));
                }
            }
        });
    }

    public static Block register(String name, Block block, ItemGroup tab) {
        BLOCKS.register(new Identifier(MODID, name), () -> block);
        BlockItem item = new BlockItem(block, new Item.Settings().group(tab));
        register(name, item);
        return block;
    }

    public static Item register(String name, Item item) {
        ITEMS.register(new Identifier(MODID, name), () -> item);
        return item;
    }

    public static StatusEffect register(String name, StatusEffect effect) {
        EFFECTS.register(new Identifier(MODID, name), () -> effect);
        return effect;
    }

    public static <T extends BlockEntity> BlockEntityType<T> register(String name, Supplier<T> be, Block... blocks) {
        BlockEntityType<T> build = BlockEntityType.Builder.create(be, blocks).build(null);
        BLOCK_ENTITIES.register(new Identifier(MODID, name), () -> build);
        return build;
    }

    public static Enchantment register(String name, Enchantment enchantment) {
        ENCHANTMENTS.register(new Identifier(MODID, name), () -> enchantment);
        return enchantment;
    }

    public static <T extends Entity> RegistrySupplier<EntityType<T>> register(String name, SpawnGroup category, EntityDimensions size, EntityType.EntityFactory<T> factory) {
        Identifier id = new Identifier(MODID, name);
        return ENTITIES.register(id, () -> EntityType.Builder.create(factory, category).setDimensions(size.width, size.height).disableSaving().build(id.toString()));
    }
}
