package kr.kro.teamdodoco.lethal_jetpack.client.mixin;

import kr.kro.teamdodoco.lethal_jetpack.IPlayerJetpack;
import kr.kro.teamdodoco.lethal_jetpack.client.ICameraJetpack;
import kr.kro.teamdodoco.lethal_jetpack.client.LethalJetpackClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Camera.class)
public abstract class JetpackCameraMixin implements ICameraJetpack
{
    @Shadow @Final private Vector3f horizontalPlane;
    @Shadow @Final private Vector3f verticalPlane;
    @Shadow @Final private Vector3f diagonalPlane;
    @Shadow @Final private Quaternionf rotation;

    @Shadow @Final private static Vector3f HORIZONTAL;
    @Shadow @Final private static Vector3f VERTICAL;
    @Shadow @Final private static Vector3f DIAGONAL;
    @Unique boolean using = false;

    @Unique Quaternionf lerpRotation = new Quaternionf();
    @Unique float lerp = 0;

    @Unique boolean inverseView = false;

    @Unique float waveMul = 0;
    public float getWaveMul() { return waveMul; }
    public void setWaveMul(float waveMul) { this.waveMul = waveMul; }

    @Inject(method = "update", at = @At("HEAD"))
    void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info)
    {
        this.inverseView = inverseView;

        Quaternionf rotation = new Quaternionf();
        boolean using = false;

        if (focusedEntity instanceof IPlayerJetpack playerJetpack)
        {
            using = playerJetpack.getJetpackUsing();
            rotation = playerJetpack.getJetpackRotation();

            if (this.using != using && using)
                lerpRotation = new Quaternionf(rotation);

            this.using = using;
        }

        lerpRotation = lerpRotation.slerp(rotation, 8 * LethalJetpackClient.deltaTime);
        if (using)
            lerp = MathHelper.lerp(8 * LethalJetpackClient.deltaTime, lerp, 1);
        else
            lerp = MathHelper.lerp(8 * LethalJetpackClient.deltaTime, lerp, 0);

        waveMul = MathHelper.lerp(6 * LethalJetpackClient.deltaTime, waveMul, 0);

        if (waveMul <= 0.01f)
            waveMul = 0;
    }

    /*@Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;moveBy(FFF)V"))
    void updateMoveBy(Camera instance, float x, float y, float z)
    {
        Vec3d pos = this.pos;
        Vec3d dir = getCalculateCameraPos(z, y, x);

        moveBy(x, y, z);

        setPos(this.pos.lerp(new Vec3d(pos.x + dir.x, pos.y + dir.y, pos.z + dir.z), lerp));
    }

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(F)F"))
    float updateClipToSpace(Camera instance, float desiredCameraDistance)
    {
        return MathHelper.lerp(lerp, clipToSpace(desiredCameraDistance), desiredCameraDistance);
    }

    @Unique
    Vec3d getCalculateCameraPos(double x, double y, double z)
    {
        if (inverseView)
            z = -z;

        float headPitch = (float)Math.toRadians(pitch);
        if (inverseView)
            headPitch = -headPitch;

        Quaternionf lerpRotation = this.lerpRotation.rotateLocalX(headPitch, new Quaternionf());
        Vector3d dir = lerpRotation.transformInverse(x, y, z, new Vector3d());

        return new Vec3d(dir.x, -dir.y, dir.z);
    }*/

    @Inject(method = "setRotation", at = @At("RETURN"))
    void updateRotation(float yaw, float pitch, CallbackInfo ci)
    {
        float offsetYaw = (float)Math.PI;
        if (inverseView)
            offsetYaw = 0;

        Quaternionf resultRotation = new Quaternionf(lerpRotation);
        if (inverseView)
        {
            Vector3f euler = resultRotation.getEulerAnglesZXY(new Vector3f());
            resultRotation.rotateLocalX(-euler.x * 2);
        }

        this.rotation.slerp(resultRotation.rotateLocalX((float)Math.toRadians(pitch)).conjugate().rotateLocalY(offsetYaw), lerp);

        HORIZONTAL.rotate(this.rotation, this.horizontalPlane);
        VERTICAL.rotate(this.rotation, this.verticalPlane);
        DIAGONAL.rotate(this.rotation, this.diagonalPlane);
    }
}
