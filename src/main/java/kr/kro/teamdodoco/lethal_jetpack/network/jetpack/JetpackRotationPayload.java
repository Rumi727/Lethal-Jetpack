package kr.kro.teamdodoco.lethal_jetpack.network.jetpack;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

import java.util.UUID;

public record JetpackRotationPayload(UUID executioner, Quaternionf rotation) implements CustomPayload
{
    static final Identifier CHANNEL = Identifier.of("lethal_jetpack", "jetpack_rotation");

    public static final Id<JetpackRotationPayload> ID = new Id<>(CHANNEL);
    public static final PacketCodec<RegistryByteBuf, JetpackRotationPayload> CODEC = PacketCodec.of
            (
                    (value, buf) ->
                    {
                        buf.writeUuid(value.executioner);

                        buf.writeFloat(value.rotation.x);
                        buf.writeFloat(value.rotation.y);
                        buf.writeFloat(value.rotation.z);
                        buf.writeFloat(value.rotation.w);
                    },
                    (buf) ->
                    {
                        UUID executioner = buf.readUuid();
                        Quaternionf rotation = new Quaternionf(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());

                        return new JetpackRotationPayload(executioner, rotation);
                    }
            );

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
