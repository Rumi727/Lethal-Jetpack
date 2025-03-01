package kr.kro.teamdodoco.lethal_jetpack.client.mixin;

import kr.kro.teamdodoco.lethal_jetpack.client.IClientPlayerJetpack;
import kr.kro.teamdodoco.lethal_jetpack.client.LethalJetpackClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Mouse.class)
public abstract class JetpackMouseMixin
{
    @Shadow @Final private MinecraftClient client;

    @Shadow private double cursorDeltaY;
    @Unique float lerpYaw = 0;

    @Redirect(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    void updateMouse(ClientPlayerEntity instance, double cursorDeltaX, double cursorDeltaY)
    {
        instance.changeLookDirection(cursorDeltaX, cursorDeltaY);

        if (this.client.player instanceof IClientPlayerJetpack playerJetpack)
        {
            if (playerJetpack.getJetpackUsing())
            {
                float deltaX = (float)Math.toRadians(cursorDeltaX * 0.15f);

                Quaternionf rotation = playerJetpack.getJetpackRotation();
                rotation = rotation.rotateLocalY(deltaX);

                Vector3f eulerZXY = rotation.getEulerAnglesZXY(new Vector3f());

                /*float sqw = rotation.w * rotation.w;
                float sqx = rotation.x * rotation.x;
                float sqy = rotation.y * rotation.y;
                float sqz = rotation.z * rotation.z;

                float yaw = (float) Math.toDegrees(MathHelper.atan2(2 * (rotation.x * rotation.y + rotation.w * rotation.z), (sqw + sqx - sqy - sqz)));*/

                lerpYaw = MathHelper.lerpAngleDegrees(8f * LethalJetpackClient.deltaTime, lerpYaw, (float)Math.toDegrees(eulerZXY.y));
                this.client.player.setYaw(lerpYaw);

                playerJetpack.setJetpackRotation(rotation);
            }
            else
                lerpYaw = MathHelper.lerpAngleDegrees(8f * LethalJetpackClient.deltaTime, lerpYaw, 0);
        }
        else
            lerpYaw = MathHelper.lerpAngleDegrees(8f * LethalJetpackClient.deltaTime, lerpYaw, 0);
    }
}
