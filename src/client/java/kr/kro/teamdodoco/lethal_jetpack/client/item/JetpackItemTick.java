package kr.kro.teamdodoco.lethal_jetpack.client.item;

import kr.kro.teamdodoco.lethal_jetpack.ModItems;
import kr.kro.teamdodoco.lethal_jetpack.client.IClientPlayerJetpack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import org.joml.Quaternionf;

public final class JetpackItemTick
{
    @SuppressWarnings({"ConstantValue", "ReassignedVariable"})
    public static void onUpdate(MinecraftClient client)
    {
        if (client.player == null)
            return;

        ClientPlayerEntity player = client.player;
        if (!(client.player instanceof IClientPlayerJetpack playerJetpack))
            return;

        boolean selectedItem = client.player.getMainHandStack().getItem() == ModItems.JETPACK;

        {
            boolean using = playerJetpack.getJetpackUsing();
            Quaternionf rotation = playerJetpack.getJetpackRotation();
            double jetpackVelocity = playerJetpack.getJetpackVelocity();
            boolean acceleration = playerJetpack.getAcceleration();

            if (using)
            {
                {
                    if (client.options.forwardKey.isPressed())
                        rotation.rotateLocalX((float)Math.toRadians(9));

                    if (client.options.backKey.isPressed())
                        rotation.rotateLocalX((float)Math.toRadians(-9));
                }

                {
                    if (client.options.leftKey.isPressed())
                        rotation.rotateLocalZ((float)Math.toRadians(-9));

                    if (client.options.rightKey.isPressed())
                        rotation.rotateLocalZ((float)Math.toRadians(9));
                }

                playerJetpack.setJetpackRotation(rotation);

                if (!acceleration)
                {
                    jetpackVelocity -= 0.375;
                    if (jetpackVelocity <= 0)
                        jetpackVelocity = 0;

                    playerJetpack.setJetpackVelocity(jetpackVelocity);

                    if (player.isOnGround() || !selectedItem || player.isFallFlying() || player.isSpectator() || player.isRiding())
                    {
                        using = false;
                        playerJetpack.setJetpackUsing(using);
                    }
                }
            }

            if (selectedItem && !player.isFallFlying() && !player.isSpectator() && !player.isRiding() && client.options.attackKey.isPressed())
            {
                if (!using)
                {
                    using = true;
                    playerJetpack.setJetpackUsing(using);
                }

                {
                    if (player.isOnGround() && (jetpackVelocity <= 0))
                        jetpackVelocity = 0;

                    jetpackVelocity += 0.05;
                    playerJetpack.setJetpackVelocity(jetpackVelocity);
                }

                if (!acceleration)
                {
                    acceleration = true;
                    playerJetpack.setAcceleration(acceleration);
                }
            }
            else if (acceleration)
            {
                acceleration = false;
                playerJetpack.setAcceleration(acceleration);
            }
        }
    }

    public static void onHudRender(DrawContext context, RenderTickCounter delta)
    {
        
    }
}
