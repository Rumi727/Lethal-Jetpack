package kr.kro.teamdodoco.lethal_jetpack.client.mixin;

import kr.kro.teamdodoco.lethal_jetpack.Debug;
import kr.kro.teamdodoco.lethal_jetpack.MathUtility;
import kr.kro.teamdodoco.lethal_jetpack.client.IClientPlayerJetpack;
import kr.kro.teamdodoco.lethal_jetpack.client.network.JetpackPackets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientPlayerEntity.class)
public abstract class JetpackClientPlayerMixin implements IClientPlayerJetpack
{
    @Shadow @Final protected MinecraftClient client;

    @Unique float gravityValue = 0.8f;

    @Unique double xSpeed = 0;
    @Unique double ySpeed = 0;
    @Unique double zSpeed = 0;

    @Unique public ClientPlayerEntity getThis() { return (ClientPlayerEntity)(Object)this; }

    @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
    Vec3d move(Vec3d movement)
    {
        if (using)
        {
            ClientPlayerEntity player = getThis();
            Vector3d accelerationVelocity = rotation.transformInverse(0, velocity, 0, new Vector3d());

            xSpeed = MathHelper.lerp(0.05, xSpeed, -accelerationVelocity.x);
            ySpeed = MathHelper.lerp(0.05, ySpeed, accelerationVelocity.y);
            zSpeed = MathHelper.lerp(0.05, zSpeed, -accelerationVelocity.z);

            movement = new Vec3d(xSpeed, ySpeed - gravityValue, zSpeed);
            player.fallDistance = 0;

            player.setVelocity(movement);
            JetpackPackets.updateMotion(getThis(), movement);
        }

        return movement;
    }

    @Unique
    boolean acceleration = false;

    public boolean getAcceleration() { return acceleration; }

    public void setAcceleration(boolean acceleration)
    {
        if (this.acceleration != acceleration)
        {
            if (acceleration)
            {
                onAccelerationStart();
                JetpackPackets.accelerationStart(getThis());
            }
            else
            {
                onAccelerationEnd();
                JetpackPackets.accelerationEnd(getThis());
            }
        }

        this.acceleration = acceleration;
    }

    @Unique
    boolean using = false;

    public boolean getJetpackUsing() { return using; }
    public void setJetpackUsing(boolean using)
    {
        this.using = using;
        JetpackPackets.using(getThis(), using);

        ClientPlayerEntity player = getThis();
        if (using)
        {
            Vec3d velocity = player.getVelocity();

            xSpeed = velocity.x;
            ySpeed = velocity.y + gravityValue;
            zSpeed = velocity.z;

            this.velocity = velocity.y;

            player.setYaw(MathUtility.repeat(player.getYaw() + 180, 360) - 180);
            rotation.rotationY((float)Math.toRadians(player.getYaw()));
        }
        else
        {
            player.setYaw(MathUtility.repeat(player.getYaw() + 180, 360) - 180);

            setJetpackVelocity(0);
            setJetpackRotation(new Quaternionf());
        }
    }

    @Unique
    double velocity;

    public double getJetpackVelocity() { return velocity; }
    public void setJetpackVelocity(double velocity)
    {
        this.velocity = velocity;
        JetpackPackets.velocity(getThis(), velocity);
    }

    @Unique
    Quaternionf rotation = new Quaternionf();

    public Quaternionf getJetpackRotation() { return rotation; }
    public void setJetpackRotation(Quaternionf rotation)
    {
        this.rotation = rotation;
        JetpackPackets.rotation(getThis(), rotation);
    }
}
