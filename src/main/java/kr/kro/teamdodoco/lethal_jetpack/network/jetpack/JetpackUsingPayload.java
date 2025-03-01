package kr.kro.teamdodoco.lethal_jetpack.network.jetpack;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record JetpackUsingPayload(UUID executioner, boolean using) implements CustomPayload
{
    static final Identifier CHANNEL = Identifier.of("lethal_jetpack", "jetpack_using");

    public static final Id<JetpackUsingPayload> ID = new Id<>(CHANNEL);
    public static final PacketCodec<RegistryByteBuf, JetpackUsingPayload> CODEC = PacketCodec.of
            (
                    (value, buf) ->
                    {
                        buf.writeUuid(value.executioner);
                        buf.writeBoolean(value.using);
                    },
                    (buf) -> new JetpackUsingPayload(buf.readUuid(), buf.readBoolean())
            );

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
