package space.bbkr.aquarius.common.network;

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Supplier;

public class AirSwimmingC2SMsg {
    public AirSwimmingC2SMsg(boolean value) {
        this.value = value;
    }

    public boolean value;

    /**
     * Deserialize the Message
     *
     * @param buffer Packet Buffer
     * @return Message
     */
    public static AirSwimmingC2SMsg decode(PacketByteBuf buffer) {
        return new AirSwimmingC2SMsg(buffer.readBoolean());
    }

    /**
     * Serialize the Message
     *
     * @param buffer Packet Buffer
     */
    public void encode(PacketByteBuf buffer) {
        buffer.writeBoolean(this.value);
    }

    /**
     * Handle messages
     *
     * @param message     Message
     * @param ctxSupplier Context Supplier
     */
    public static void onMessageReceived(final AirSwimmingC2SMsg message, Supplier<NetworkManager.PacketContext> ctxSupplier) {
        NetworkManager.PacketContext context = ctxSupplier.get();
        final PlayerEntity playerEntity = context.getPlayer();

        if (!(playerEntity instanceof ServerPlayerEntity)) {
            return;
        }

        if (playerEntity == null) {
            return;
        }

        context.queue(() -> processMessage(message, playerEntity));
    }

    /**
     * Process the Message
     *
     * @param message      Message
     * @param playerEntity Player
     */
    public static void processMessage(AirSwimmingC2SMsg message, PlayerEntity playerEntity) {
        ((((IAirSwimming)playerEntity))).setServerSwimming(message.value);
    }

}
