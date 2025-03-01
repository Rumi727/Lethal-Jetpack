package kr.kro.teamdodoco.lethal_jetpack.client.network;

import kr.kro.teamdodoco.lethal_jetpack.IPlayerJetpack;
import kr.kro.teamdodoco.lethal_jetpack.network.jetpack.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public final class JetpackPackets
{
    @SuppressWarnings("resource")
    public static void Register()
    {
        ClientPlayNetworking.registerGlobalReceiver(JetpackAccelerationStart.ID, (payload, context) ->
        {
            MinecraftClient client = context.client();
            if (client.world != null)
            {
                PlayerEntity player = client.world.getPlayerByUuid(payload.executioner());
                if (player instanceof IPlayerJetpack playerJetpack)
                    playerJetpack.onAccelerationStart();
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(JetpackAccelerationEnd.ID, (payload, context) ->
        {
            MinecraftClient client = context.client();
            if (client.world != null)
            {
                PlayerEntity player = client.world.getPlayerByUuid(payload.executioner());
                if (player instanceof IPlayerJetpack playerJetpack)
                    playerJetpack.onAccelerationEnd();
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(JetpackUsingPayload.ID, (payload, context) ->
        {
            MinecraftClient client = context.client();
            if (client.world != null)
            {
                PlayerEntity player = client.world.getPlayerByUuid(payload.executioner());
                if (player instanceof IPlayerJetpack playerJetpack)
                {
                    boolean using = payload.using();
                    playerJetpack.setJetpackUsing(using);
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(JetpackVelocityPayload.ID, (payload, context) ->
        {
            MinecraftClient client = context.client();
            if (client.world != null)
            {
                PlayerEntity player = client.world.getPlayerByUuid(payload.executioner());
                if (player instanceof IPlayerJetpack playerJetpack)
                {
                    double velocity = payload.velocity();
                    playerJetpack.setJetpackVelocity(velocity);
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(JetpackRotationPayload.ID, (payload, context) ->
        {
            MinecraftClient client = context.client();
            if (client.world != null)
            {
                PlayerEntity player = client.world.getPlayerByUuid(payload.executioner());
                if (player instanceof IPlayerJetpack playerJetpack)
                {
                    Quaternionf rotation = payload.rotation();
                    playerJetpack.setJetpackRotation(rotation);
                }
            }
        });
    }



    public static void updateMotion(ClientPlayerEntity executioner, Vec3d motion) { ClientPlayNetworking.send(new JetpackUpdateMotionPayload(executioner.getUuid(), motion)); }



    public static void accelerationStart(ClientPlayerEntity executioner) { ClientPlayNetworking.send(new JetpackAccelerationStart(executioner.getUuid())); }

    public static void accelerationEnd(ClientPlayerEntity executioner) { ClientPlayNetworking.send(new JetpackAccelerationEnd(executioner.getUuid())); }

    public static void using(ClientPlayerEntity executioner, boolean using) { ClientPlayNetworking.send(new JetpackUsingPayload(executioner.getUuid(), using)); }

    public static void velocity(ClientPlayerEntity executioner, double velocity) { ClientPlayNetworking.send(new JetpackVelocityPayload(executioner.getUuid(), velocity)); }

    public static void rotation(ClientPlayerEntity executioner, Quaternionf rotation) { ClientPlayNetworking.send(new JetpackRotationPayload(executioner.getUuid(), rotation)); }
}
