package kr.kro.teamdodoco.lethal_jetpack.client.mixin;

import kr.kro.teamdodoco.lethal_jetpack.IPlayerJetpack;
import kr.kro.teamdodoco.lethal_jetpack.ModSounds;
import kr.kro.teamdodoco.lethal_jetpack.client.ICameraJetpack;
import kr.kro.teamdodoco.lethal_jetpack.client.IClientPlayerJetpack;
import kr.kro.teamdodoco.lethal_jetpack.client.LethalJetpackClient;
import kr.kro.teamdodoco.lethal_jetpack.client.sound_event.JetpackSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerEntity.class, priority = 2000)
public abstract class JetpackPlayerMixin implements IPlayerJetpack
{
    @Inject(method = "jump", at = @At("TAIL"))
    void jump(CallbackInfo ci)
    {
        if (this instanceof IClientPlayerJetpack playerJetpack)
        {
            if (playerJetpack.getJetpackUsing())
                playerJetpack.setJetpackVelocity(((PlayerEntity)(Object)this).getVelocity().y);
        }
    }

    @Unique Quaternionf lerpRotation = new Quaternionf();
    @Unique float lerp = 0;

    public Quaternionf getRenderRotation()
    {
        Quaternionf rotation = getJetpackRotation();
        boolean using = getJetpackUsing();

        if (using)
            lerp = 1;
        else
            lerp = MathHelper.lerp(8 * LethalJetpackClient.deltaTime, lerp, 0);

        rotation = rotation.rotateLocalY(MathHelper.lerp(lerp, 0, (float)Math.toRadians(-((PlayerEntity)(Object)this).getYaw())), new Quaternionf());

        lerpRotation = lerpRotation.slerp(rotation, 8 * LethalJetpackClient.deltaTime);
        return lerpRotation;
    }

    @Unique
    JetpackSounds idle = null;

    public void onAccelerationStart()
    {
        lerpRotation = new Quaternionf(getJetpackRotation());

        MinecraftClient client = MinecraftClient.getInstance();
        LivingEntity entity = (LivingEntity)(Object)this;
        JetpackSounds use = new JetpackSounds(entity, ModSounds.JETPACK_USE, SoundCategory.PLAYERS, 0.4f, false);
        client.getSoundManager().play(use);

        if (idle != null)
            client.getSoundManager().stop(idle);

        idle = new JetpackSounds(entity, ModSounds.JETPACK_IDLE, SoundCategory.PLAYERS, 0.4f, false);
        client.getSoundManager().play(idle);

        if (isCamera())
            ((ICameraJetpack)client.gameRenderer.getCamera()).setWaveMul(0.4f);
    }

    public void onAccelerationEnd()
    {
        MinecraftClient client = MinecraftClient.getInstance();

        if (idle != null)
        {
            client.getSoundManager().stop(idle);
            idle = null;
        }

        if (isCamera())
            ((ICameraJetpack)client.gameRenderer.getCamera()).setWaveMul(0.4f);
    }

    @Unique
    @SuppressWarnings("RedundantCast")
    boolean isCamera() { return MinecraftClient.getInstance().getCameraEntity() == ((PlayerEntity)(Object)this); }
}
