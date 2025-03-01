package kr.kro.teamdodoco.lethal_jetpack.network.jetpack;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public record JetpackUpdateMotionPayload(UUID executioner, Vec3d motion) implements CustomPayload
{
    static final Identifier CHANNEL = Identifier.of("lethal_jetpack", "jetpack_update_motion");

    public static final Id<JetpackUpdateMotionPayload> ID = new Id<>(CHANNEL);
    public static final PacketCodec<RegistryByteBuf, JetpackUpdateMotionPayload> CODEC = PacketCodec.of
            (
                    (value, buf) ->
                    {
                        buf.writeUuid(value.executioner);

                        buf.writeDouble(value.motion.x);
                        buf.writeDouble(value.motion.y);
                        buf.writeDouble(value.motion.z);
                    },
                    (buf) ->
                    {
                        UUID executioner = buf.readUuid();
                        Vec3d motion = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());

                        return new JetpackUpdateMotionPayload(executioner, motion);
                    }
            );

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
