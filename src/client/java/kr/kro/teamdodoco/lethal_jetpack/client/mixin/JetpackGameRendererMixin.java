package kr.kro.teamdodoco.lethal_jetpack.client.mixin;

import kr.kro.teamdodoco.lethal_jetpack.IPlayerJetpack;
import kr.kro.teamdodoco.lethal_jetpack.client.ICameraJetpack;
import kr.kro.teamdodoco.lethal_jetpack.client.LethalJetpackClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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

    @ModifyVariable(method = "renderWorld", at = @At(value = "STORE"), ordinal = 1)
    Matrix4f renderWorld(Matrix4f matrix4f2)
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
        float waveMul = 0;
        if (camera instanceof ICameraJetpack cameraJetpack)
            waveMul = cameraJetpack.getWaveMul();

        matrix4f2.translate((waveRandom.nextFloat() * 2 - 1) * waveMul, (waveRandom.nextFloat() * 2 - 1) * waveMul, 0);

        /*if (using)
        {
            lerp = 0;
            matrix4f2.rotateLocalX(headPitch);
        }
        else
        {
            lerp = MathHelper.lerp(8 * LethalJetpackClient.deltaTime, lerp, 1);
            if (lerp < 0.9995f)
                matrix4f2.rotateLocalX(headPitch * (1 - lerp));
        }*/

        return matrix4f2;
    }
}
