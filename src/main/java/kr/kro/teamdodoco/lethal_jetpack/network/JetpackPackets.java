package kr.kro.teamdodoco.lethal_jetpack.network;

import kr.kro.teamdodoco.lethal_jetpack.IPlayerJetpack;
import kr.kro.teamdodoco.lethal_jetpack.network.jetpack.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.joml.Quaternionf;

public final class JetpackPackets
{
    public static void Register()
    {
        PayloadTypeRegistry.playC2S().register(JetpackUpdateMotionPayload.ID, JetpackUpdateMotionPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(JetpackAccelerationStart.ID, JetpackAccelerationStart.CODEC);
        PayloadTypeRegistry.playC2S().register(JetpackAccelerationEnd.ID, JetpackAccelerationEnd.CODEC);
        PayloadTypeRegistry.playC2S().register(JetpackUsingPayload.ID, JetpackUsingPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(JetpackVelocityPayload.ID, JetpackVelocityPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(JetpackRotationPayload.ID, JetpackRotationPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(JetpackAccelerationStart.ID, JetpackAccelerationStart.CODEC);
        PayloadTypeRegistry.playS2C().register(JetpackAccelerationEnd.ID, JetpackAccelerationEnd.CODEC);
        PayloadTypeRegistry.playS2C().register(JetpackUsingPayload.ID, JetpackUsingPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(JetpackVelocityPayload.ID, JetpackVelocityPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(JetpackRotationPayload.ID, JetpackRotationPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(JetpackUpdateMotionPayload.ID, (payload, context) ->
        {
            ServerPlayerEntity player = context.player();
            if (((IPlayerJetpack)player).getJetpackUsing())
            {
                player.setVelocity(payload.motion());
                player.fallDistance = 0;
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(JetpackAccelerationStart.ID, (payload, context) ->
                AccelerationStart(context.player()));

        ServerPlayNetworking.registerGlobalReceiver(JetpackAccelerationEnd.ID, (payload, context) ->
                AccelerationEnd(context.player()));

        ServerPlayNetworking.registerGlobalReceiver(JetpackUsingPayload.ID, (payload, context) ->
        {
            ServerPlayerEntity player = context.player();
            boolean using = payload.using();

            ((IPlayerJetpack)player).setJetpackUsing(using);
            Using(player, using);
        });

        ServerPlayNetworking.registerGlobalReceiver(JetpackVelocityPayload.ID, (payload, context) ->
        {
            ServerPlayerEntity player = context.player();
            double velocity = payload.velocity();

            ((IPlayerJetpack)player).setJetpackVelocity(velocity);
            Velocity(player, velocity);
        });

        ServerPlayNetworking.registerGlobalReceiver(JetpackRotationPayload.ID, (payload, context) ->
        {
            ServerPlayerEntity player = context.player();
            Quaternionf rotation = payload.rotation();

            ((IPlayerJetpack)player).setJetpackRotation(rotation);
            Rotation(player, rotation);
        });
    }



    public static void AccelerationStart(ServerPlayerEntity executioner)
    {
        for (PlayerEntity player : executioner.getWorld().getPlayers())
        {
            if (player != executioner && player instanceof ServerPlayerEntity serverPlayer)
                ServerPlayNetworking.send(serverPlayer, new JetpackAccelerationStart(executioner.getUuid()));
        }
    }

    public static void AccelerationEnd(ServerPlayerEntity executioner)
    {
        for (PlayerEntity player : executioner.getWorld().getPlayers())
        {
            if (player != executioner && player instanceof ServerPlayerEntity serverPlayer)
                ServerPlayNetworking.send(serverPlayer, new JetpackAccelerationEnd(executioner.getUuid()));
        }
    }

    public static void Using(ServerPlayerEntity executioner, boolean using)
    {
        for (PlayerEntity player : executioner.getWorld().getPlayers())
        {
            if (player != executioner && player instanceof ServerPlayerEntity serverPlayer)
                ServerPlayNetworking.send(serverPlayer, new JetpackUsingPayload(executioner.getUuid(), using));
        }
    }

    public static void Velocity(ServerPlayerEntity executioner, double velocity)
    {
        for (PlayerEntity player : executioner.getWorld().getPlayers())
        {
            if (player != executioner && player instanceof ServerPlayerEntity serverPlayer)
                ServerPlayNetworking.send(serverPlayer, new JetpackVelocityPayload(executioner.getUuid(), velocity));
        }
    }

    public static void Rotation(ServerPlayerEntity executioner, Quaternionf rotation)
    {
        for (PlayerEntity player : executioner.getWorld().getPlayers())
        {
            if (player != executioner && player instanceof ServerPlayerEntity serverPlayer)
                ServerPlayNetworking.send(serverPlayer, new JetpackRotationPayload(executioner.getUuid(), rotation));
        }
    }
}
