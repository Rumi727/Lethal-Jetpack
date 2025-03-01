package kr.kro.teamdodoco.lethal_jetpack.network.jetpack;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record JetpackVelocityPayload(UUID executioner, double velocity) implements CustomPayload
{
    static final Identifier CHANNEL = Identifier.of("lethal_jetpack", "jetpack_velocity");

    public static final Id<JetpackVelocityPayload> ID = new Id<>(CHANNEL);
    public static final PacketCodec<RegistryByteBuf, JetpackVelocityPayload> CODEC = PacketCodec.of
            (
                    (value, buf) ->
                    {
                        buf.writeUuid(value.executioner);
                        buf.writeDouble(value.velocity);
                    },
                    (buf) -> new JetpackVelocityPayload(buf.readUuid(), buf.readDouble())
            );

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
