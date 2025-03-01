package kr.kro.teamdodoco.lethal_jetpack.client.mixin;

import kr.kro.teamdodoco.lethal_jetpack.IPlayerJetpack;
import kr.kro.teamdodoco.lethal_jetpack.client.ICameraJetpack;
import kr.kro.teamdodoco.lethal_jetpack.client.LethalJetpackClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class JetpackGameRendererMixin
{
    @Shadow @Final MinecraftClient client;

    @Shadow @Final private Camera camera;
    @Unique boolean using = false;

    @Unique Quaternionf lerpRotation = new Quaternionf();
    @Unique float lerp = 1;

    @Unique Random waveRandom = Random.create();

    @Inject(method = "renderWorld", at = @At("HEAD"))
    void renderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info)
    {
        Quaternionf rotation = new Quaternionf();
        boolean using = false;

        if (this.client.getCameraEntity() instanceof IPlayerJetpack playerJetpack)
        {
            using = playerJetpack.getJetpackUsing();
            rotation = playerJetpack.getJetpackRotation();

            if (this.using != using && using)
                lerpRotation = new Quaternionf(rotation);

            this.using = using;
        }

        lerpRotation = lerpRotation.slerp(rotation, 8 * LethalJetpackClient.deltaTime);

        float headPitch = (float)Math.toRadians(this.client.gameRenderer.getCamera().getPitch());
        float offsetYaw = 0;
        float waveMul = 0;
        if (camera instanceof ICameraJetpack cameraJetpack)
        {
            if (cameraJetpack.getInverseView())
                offsetYaw = (float) Math.PI;

            waveMul = cameraJetpack.getWaveMul();
        }

        matrices.translate((waveRandom.nextFloat() * 2 - 1) * waveMul, (waveRandom.nextFloat() * 2 - 1) * waveMul, 0);

        if (using)
        {
            lerp = 0;
            matrices.multiply(lerpRotation.rotateLocalY(offsetYaw, new Quaternionf()).rotateLocalX(headPitch));
        }
        else
        {
            lerp = MathHelper.lerp(8 * LethalJetpackClient.deltaTime, lerp, 1);
            if (lerp < 0.9995f)
                matrices.multiply(lerpRotation.rotateLocalY(offsetYaw * (1 - lerp), new Quaternionf()).rotateLocalX(headPitch * (1 - lerp)));
        }
    }

    @Redirect(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getPitch()F"))
    float getPitch(Camera instance) { return instance.getPitch() * lerp; }

    @Redirect(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getYaw()F"))
    float getYaw(Camera instance) { return instance.getYaw() * lerp; }
}
