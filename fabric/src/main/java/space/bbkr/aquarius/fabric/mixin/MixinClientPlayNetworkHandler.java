package space.bbkr.aquarius.fabric.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import space.bbkr.aquarius.common.Aquarius;
import space.bbkr.aquarius.common.TridentBeamEntity;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Shadow private ClientWorld world;

    @Inject(method = "onEntitySpawn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/network/packet/s2c/play/EntitySpawnS2CPacket;getEntityTypeId()Lnet/minecraft/entity/EntityType;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void spawnTridentBeam(EntitySpawnS2CPacket packet, CallbackInfo ci, double x, double y, double z, EntityType type) {
        if (type == Aquarius.TRIDENT_BEAM.getOrNull()) {
            Entity owner = world.getEntityById(packet.getEntityData());
            TridentBeamEntity toSpawn = new TridentBeamEntity(world, x, y, z);
            if (owner instanceof PlayerEntity) {
                toSpawn.setOwner(owner);
                int id = packet.getId();
                toSpawn.updateTrackedPosition(x, y, z);
                toSpawn.pitch = (float)(packet.getPitch() * 360) / 256.0F;
                toSpawn.yaw = (float)(packet.getYaw() * 360) / 256.0F;
                toSpawn.setEntityId(id);
                toSpawn.setUuid(packet.getUuid());
                this.world.addEntity(id, toSpawn);
                ci.cancel();
            }
        }
    }
}
