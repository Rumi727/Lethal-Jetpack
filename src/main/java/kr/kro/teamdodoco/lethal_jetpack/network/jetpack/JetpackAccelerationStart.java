package kr.kro.teamdodoco.lethal_jetpack.network.jetpack;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record JetpackAccelerationStart(UUID executioner) implements CustomPayload
{
    static final Identifier CHANNEL = Identifier.of("lethal_jetpack", "jetpack_acceleration_start");

    public static final Id<JetpackAccelerationStart> ID = new Id<>(CHANNEL);
    public static final PacketCodec<RegistryByteBuf, JetpackAccelerationStart> CODEC = PacketCodec.of
            (
                    (value, buf) -> buf.writeUuid(value.executioner),
                    (buf) -> new JetpackAccelerationStart(buf.readUuid())
            );

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
