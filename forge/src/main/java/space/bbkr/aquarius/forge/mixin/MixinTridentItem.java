package space.bbkr.aquarius.forge.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.bbkr.aquarius.common.Aquarius;
import space.bbkr.aquarius.common.TridentBeamEntity;

@Mixin(TridentItem.class)
public abstract class MixinTridentItem extends Item {

	@Shadow public abstract int getMaxUseTime(ItemStack itemStack_1);

	public MixinTridentItem(Settings settings) {
		super(settings);
	}

	@Inject(method = "onStoppedUsing", at = @At(value = "HEAD"), cancellable = true)
	public void makeTridentPiercing(ItemStack stack, World world, LivingEntity entity, int usedTicks, CallbackInfo ci) {
		if (EnchantmentHelper.getLevel(Aquarius.GUARDIAN_SIGHT, stack) > 0) {
			world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.PLAYERS, 0.5f, 0.95f);
			ci.cancel();
		}
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int ticksLeft) {
		int sightLevel = EnchantmentHelper.getLevel(Aquarius.GUARDIAN_SIGHT, stack);
		if (sightLevel <= 0) return;
		if (ticksLeft < getMaxUseTime(stack) && ticksLeft % 20 == 0) {
			TridentBeamEntity beam = new TridentBeamEntity(world, user, sightLevel);
			beam.setProperties(user, user.pitch, user.yaw, 0.0F, 2.5F, 1.0F);
			beam.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
			world.spawnEntity(beam);
			if (ticksLeft == getMaxUseTime(stack) - 20 && user instanceof PlayerEntity) {
				world.playSoundFromEntity((PlayerEntity)user, beam, SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.PLAYERS, 0.8F, 1.0F);
			}
			stack.damage(sightLevel, user, (entity) -> entity.getStackInHand(user.getActiveHand()));
		}
	}

	@Override
	public boolean canRepair(ItemStack target, ItemStack repairMat) {
		return repairMat.getItem() == Aquarius.PRISMARINE_ROD;
	}
}
