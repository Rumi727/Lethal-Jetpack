package kr.kro.teamdodoco.lethal_jetpack.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public final class Packets
{
    static final Identifier UPDATE_MOTION_CHANNEL = new Identifier("lethal_jetpack", "update_motion");

    public static void UpdateMotion(Vec3d motion)
    {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeDouble(motion.x);
        buf.writeDouble(motion.y);
        buf.writeDouble(motion.z);

        ClientPlayNetworking.send(UPDATE_MOTION_CHANNEL, buf);
    }
}
