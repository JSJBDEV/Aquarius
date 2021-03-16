package space.bbkr.aquarius.common;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import space.bbkr.aquarius.common.network.IAirSwimming;

import static me.shedaniel.architectury.event.events.TickEvent.PLAYER_PRE;

//import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
//import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
//import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;

public class AquariusClient {

    private static void tick(PlayerEntity playerEntity) {
        if (playerEntity.hasStatusEffect(Aquarius.ATLANTEAN)) {
            if (playerEntity instanceof ClientPlayerEntity) {
                ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity) playerEntity;
                boolean hasFood = (float) clientPlayerEntity.getHungerManager().getFoodLevel() > 6.0F || clientPlayerEntity.abilities.allowFlying;

                if (((IAirSwimming) playerEntity).getLastAirSwimming()
                    && clientPlayerEntity.input.hasForwardMovement()
                    && hasFood
                    && !clientPlayerEntity.isUsingItem()
                    && !clientPlayerEntity.hasStatusEffect(StatusEffects.BLINDNESS)
                    && MinecraftClient.getInstance().options.keySprint.isPressed()) {
                    playerEntity.setSprinting(true);
                }

            }
        }
    }

    public static void onInitializeClient() {
        PLAYER_PRE.register(AquariusClient::tick);
    }
}
