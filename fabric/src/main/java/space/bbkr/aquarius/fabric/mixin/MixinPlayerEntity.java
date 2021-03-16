package space.bbkr.aquarius.fabric.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.bbkr.aquarius.common.Aquarius;
import space.bbkr.aquarius.common.network.AirSwimmingC2SMsg;
import space.bbkr.aquarius.common.network.IAirSwimming;

import static space.bbkr.aquarius.common.Aquarius.NETWORK;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements IAirSwimming {

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

//    private int maxSwimCooldown = 10;
//    private int swimCooldownTime = 0;
//    private int swimDamageTime = 0;
//
//    @Inject(method = "updateTurtleHelmet", at = @At("HEAD"))
//    private void updateTurtleHelmet(CallbackInfo ci) {
//        ItemStack stackFeet = this.getEquippedStack(EquipmentSlot.FEET);
//        if (stackFeet.getItem() == Aquarius.FLIPPERS) {
//            if (this.isTouchingWaterOrRain()) {
//                swimCooldownTime = 0;
//                this.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 20, 0, true, false, true));
//                if (!world.isClient) {
//                    swimDamageTime++;
//                    if (swimDamageTime % 20 == 0) {
//                        swimDamageTime = 0;
//                        stackFeet.damage(1, this, (entity) -> entity.sendEquipmentBreakStatus(EquipmentSlot.FEET));
//                    }
//                }
//            } else {
//                if (swimCooldownTime >= maxSwimCooldown) {
//                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 0, true, false, true));
//                } else swimCooldownTime++;
//            }
//        }
//    }



    private boolean lastSwimming = false;
    private int serverSwimming = 0;
//
    public void setServerSwimming(boolean swimming) {
        serverSwimming = swimming ? 200 : 0;
    }

    @Override
    public boolean getLastAirSwimming() {
        return lastSwimming;
    }
//
    @Inject(method = "updateSwimming", at = @At("TAIL"))
    private void updateAirSwimming(CallbackInfo ci) {
        if (this.hasStatusEffect(Aquarius.ATLANTEAN)) {
            boolean b = false;
            if (!this.world.isClient) {
                b = this.serverSwimming > 0 && this.serverSwimming-- > 1;
            } else {
                StatusEffectInstance statusEffect = this.getStatusEffect(Aquarius.ATLANTEAN);
                int duration = statusEffect == null ? 0 : statusEffect.getDuration();
                boolean start = this.jumping && (this.fallDistance > 2 || this.isSprinting()) && !this.hasVehicle() && duration > 10;
                boolean stop1 = !this.jumping
                    && this.forwardSpeed == 0
                    && this.sidewaysSpeed == 0;
                boolean stop2 = this.hasVehicle() || duration < 1;
                stop1 |= stop2;
                if (start) {
                    b = true;
                } else if (this.lastSwimming) {
                    if (stop1) {
                        b = false;
                    } else {
                        b = true;
                    }
                }
                if (b != this.lastSwimming) {
                    NETWORK.sendToServer(new AirSwimmingC2SMsg(b));
                } else if (this.serverSwimming-- < 10) {
                    this.serverSwimming = 100;
                    NETWORK.sendToServer(new AirSwimmingC2SMsg(b));
                }
            }
            this.setSwimming(b);
            this.submergedInWater = this.submergedInWater || b;
            this.touchingWater = this.touchingWater || b;
//            playerEntity.setNoGravity(playerEntity.isSwimming());
            if (b) {
                this.fallDistance = 0.0F;
                Vec3d look = this.getRotationVector();
                if (this.isSprinting()) {
                    this.move(MovementType.SELF, new Vec3d(look.x / 4, look.y / 4, look.z / 4));
                }
            }
        } else if (this.lastSwimming) {
            NETWORK.sendToServer(new AirSwimmingC2SMsg(false));
        }
        this.lastSwimming = this.isSwimming();
    }
//

    private static void airSwimming(MixinPlayerEntity playerEntity) {
    }

}
