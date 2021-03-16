package space.bbkr.aquarius.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class TridentBeamEntity extends PersistentProjectileEntity {

	private int ticksExisted;
	private int sightLevel;

	protected TridentBeamEntity(World world) {
		super(Aquarius.TRIDENT_BEAM.get(), world);
		this.setNoGravity(true);
	}

	public TridentBeamEntity(World world, double x, double y, double z) {
		super(Aquarius.TRIDENT_BEAM.get(), x, y, z, world);
		this.setNoGravity(true);
	}

	public TridentBeamEntity(World world, LivingEntity owner, int sightLevel) {
		super(Aquarius.TRIDENT_BEAM.get(), owner, world);
		this.sightLevel = sightLevel;
		this.setNoGravity(true);
	}




	@Override
	public void tick() {
		if (!this.world.isClient) {
			ticksExisted++;
			if (this.ticksExisted >= 500) {
				this.remove();
				return;
			}
		}
		else {
			ticksExisted++;
		}

		super.tick();
	}


	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		entityHitResult.getEntity().damage(DamageSource.MAGIC, 1.5F*sightLevel);
	}

	@Override
	protected ItemStack asItemStack() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean collides() {
		return false;
	}



}
