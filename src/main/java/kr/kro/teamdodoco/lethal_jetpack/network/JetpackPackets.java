package kr.kro.teamdodoco.lethal_jetpack.network;

import kr.kro.teamdodoco.lethal_jetpack.IPlayerJetpack;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3d;

public final class JetpackPackets
{
    static final Identifier JETPACK_ACCELERATION_START_CHANNEL = new Identifier("lethal_jetpack", "jetpack_acceleration_start");
    static final Identifier JETPACK_ACCELERATION_END_CHANNEL = new Identifier("lethal_jetpack", "jetpack_acceleration_end");
    static final Identifier JETPACK_USING_CHANNEL = new Identifier("lethal_jetpack", "jetpack_using");
    static final Identifier JETPACK_VELOCITY_CHANNEL = new Identifier("lethal_jetpack", "jetpack_velocity");
    static final Identifier JETPACK_ROTATION_CHANNEL = new Identifier("lethal_jetpack", "jetpack_rotation");



    public static void Register()
    {
        ServerPlayNetworking.registerGlobalReceiver(JETPACK_ACCELERATION_START_CHANNEL, (server, player, handler, buf, responseSender) ->
        {
            AccelerationStart(player);
        });

        ServerPlayNetworking.registerGlobalReceiver(JETPACK_ACCELERATION_END_CHANNEL, (server, player, handler, buf, responseSender) ->
        {
            AccelerationEnd(player);
        });

        ServerPlayNetworking.registerGlobalReceiver(JETPACK_USING_CHANNEL, (server, player, handler, buf, responseSender) ->
        {
            boolean using = buf.readBoolean();

            ((IPlayerJetpack)player).setJetpackUsing(using);
            Using(player, using);
        });

        ServerPlayNetworking.registerGlobalReceiver(JETPACK_VELOCITY_CHANNEL, (server, player, handler, buf, responseSender) ->
        {
            double velocity = buf.readDouble();

            ((IPlayerJetpack) player).setJetpackVelocity(velocity);
            Velocity(player, velocity);
        });

        ServerPlayNetworking.registerGlobalReceiver(JETPACK_ROTATION_CHANNEL, (server, player, handler, buf, responseSender) ->
        {
            Quaternionf rotation = new Quaternionf(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());

            ((IPlayerJetpack) player).setJetpackRotation(rotation);
            Rotation(player, rotation);
        });
    }



    public static void AccelerationStart(ServerPlayerEntity executioner)
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(executioner.getUuid());

        for (PlayerEntity player : executioner.getWorld().getPlayers())
        {
            if (player != executioner && player instanceof ServerPlayerEntity serverPlayer)
                ServerPlayNetworking.send(serverPlayer, JETPACK_ACCELERATION_START_CHANNEL, buf);
        }
    }

    public static void AccelerationEnd(ServerPlayerEntity executioner)
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(executioner.getUuid());

        for (PlayerEntity player : executioner.getWorld().getPlayers())
        {
            if (player != executioner && player instanceof ServerPlayerEntity serverPlayer)
                ServerPlayNetworking.send(serverPlayer, JETPACK_ACCELERATION_END_CHANNEL, buf);
        }
    }

    public static void Using(ServerPlayerEntity executioner, boolean using)
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(executioner.getUuid());
        buf.writeBoolean(using);

        for (PlayerEntity player : executioner.getWorld().getPlayers())
        {
            if (player != executioner && player instanceof ServerPlayerEntity serverPlayer)
                ServerPlayNetworking.send(serverPlayer, JETPACK_USING_CHANNEL, buf);
        }
    }

    public static void Velocity(ServerPlayerEntity executioner, double velocity)
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(executioner.getUuid());
        buf.writeDouble(velocity);

        for (PlayerEntity player : executioner.getWorld().getPlayers())
        {
            if (player != executioner && player instanceof ServerPlayerEntity serverPlayer)
                ServerPlayNetworking.send(serverPlayer, JETPACK_VELOCITY_CHANNEL, buf);
        }
    }

    public static void Rotation(ServerPlayerEntity executioner, Quaternionf rotation)
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(executioner.getUuid());

        buf.writeFloat(rotation.x);
        buf.writeFloat(rotation.y);
        buf.writeFloat(rotation.z);
        buf.writeFloat(rotation.w);

        for (PlayerEntity player : executioner.getWorld().getPlayers())
        {
            if (player != executioner && player instanceof ServerPlayerEntity serverPlayer)
                ServerPlayNetworking.send(serverPlayer, JETPACK_ROTATION_CHANNEL, buf);
        }
    }
}
