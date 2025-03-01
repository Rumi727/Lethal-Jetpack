package kr.kro.teamdodoco.lethal_jetpack.network.jetpack;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record JetpackAccelerationEnd(UUID executioner) implements CustomPayload
{
    static final Identifier CHANNEL = Identifier.of("lethal_jetpack", "jetpack_acceleration_end");

    public static final Id<JetpackAccelerationEnd> ID = new Id<>(CHANNEL);
    public static final PacketCodec<RegistryByteBuf, JetpackAccelerationEnd> CODEC = PacketCodec.of
            (
                    (value, buf) -> buf.writeUuid(value.executioner),
                    (buf) -> new JetpackAccelerationEnd(buf.readUuid())
            );

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
