package kr.kro.teamdodoco.lethal_jetpack.client.network;

import kr.kro.teamdodoco.lethal_jetpack.Debug;
import kr.kro.teamdodoco.lethal_jetpack.IPlayerJetpack;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;

public final class JetpackPackets
{
    static final Identifier JETPACK_ACCELERATION_START_CHANNEL = new Identifier("lethal_jetpack", "jetpack_acceleration_start");
    static final Identifier JETPACK_ACCELERATION_END_CHANNEL = new Identifier("lethal_jetpack", "jetpack_acceleration_end");
    static final Identifier JETPACK_USING_CHANNEL = new Identifier("lethal_jetpack", "jetpack_using");
    static final Identifier JETPACK_VELOCITY_CHANNEL = new Identifier("lethal_jetpack", "jetpack_velocity");
    static final Identifier JETPACK_ROTATION_CHANNEL = new Identifier("lethal_jetpack", "jetpack_rotation");



    public static void Register()
    {
        ClientPlayNetworking.registerGlobalReceiver(JETPACK_ACCELERATION_START_CHANNEL, (client, handler, buf, responseSender) ->
        {
            if (client.world != null)
            {
                PlayerEntity player = client.world.getPlayerByUuid(buf.readUuid());
                if (player instanceof IPlayerJetpack playerJetpack)
                    playerJetpack.onAccelerationStart();
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(JETPACK_ACCELERATION_END_CHANNEL, (client, handler, buf, responseSender) ->
        {
            if (client.world != null)
            {
                PlayerEntity player = client.world.getPlayerByUuid(buf.readUuid());
                if (player instanceof IPlayerJetpack playerJetpack)
                    playerJetpack.onAccelerationEnd();
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(JETPACK_USING_CHANNEL, (client, handler, buf, responseSender) ->
        {
            if (client.world != null)
            {
                PlayerEntity player = client.world.getPlayerByUuid(buf.readUuid());
                if (player instanceof IPlayerJetpack playerJetpack)
                {
                    boolean using = buf.readBoolean();
                    playerJetpack.setJetpackUsing(using);
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(JETPACK_VELOCITY_CHANNEL, (client, handler, buf, responseSender) ->
        {
            if (client.world != null)
            {
                PlayerEntity player = client.world.getPlayerByUuid(buf.readUuid());
                if (player instanceof IPlayerJetpack playerJetpack)
                {
                    double velocity = buf.readDouble();
                    playerJetpack.setJetpackVelocity(velocity);
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(JETPACK_ROTATION_CHANNEL, (client, handler, buf, responseSender) ->
        {
            if (client.world != null)
            {
                PlayerEntity player = client.world.getPlayerByUuid(buf.readUuid());
                if (player instanceof IPlayerJetpack playerJetpack)
                {
                    Quaternionf rotation = new Quaternionf(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
                    playerJetpack.setJetpackRotation(rotation);
                }
            }
        });
    }



    public static void AccelerationStart()
    {
        ClientPlayNetworking.send(JETPACK_ACCELERATION_START_CHANNEL, PacketByteBufs.empty());
    }

    public static void AccelerationEnd()
    {
        ClientPlayNetworking.send(JETPACK_ACCELERATION_END_CHANNEL, PacketByteBufs.empty());
    }

    public static void Using(boolean using)
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(using);

        ClientPlayNetworking.send(JETPACK_USING_CHANNEL, buf);
    }

    public static void Velocity(double velocity)
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(velocity);

        ClientPlayNetworking.send(JETPACK_VELOCITY_CHANNEL, buf);
    }

    public static void Rotation(Quaternionf rotation)
    {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeFloat(rotation.x);
        buf.writeFloat(rotation.y);
        buf.writeFloat(rotation.z);
        buf.writeFloat(rotation.w);

        ClientPlayNetworking.send(JETPACK_ROTATION_CHANNEL, buf);
    }
}
