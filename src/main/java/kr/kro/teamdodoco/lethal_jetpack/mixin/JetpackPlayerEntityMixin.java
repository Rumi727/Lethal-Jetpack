package kr.kro.teamdodoco.lethal_jetpack.mixin;

import kr.kro.teamdodoco.lethal_jetpack.Debug;
import kr.kro.teamdodoco.lethal_jetpack.IPlayerJetpack;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class JetpackPlayerEntityMixin implements IPlayerJetpack
{
    public void onRender()
    {

    }

    public void onAccelerationStart() { }

    public void onAccelerationEnd() { }

    @Unique
    boolean using = false;

    public boolean getJetpackUsing() { return using; }
    public void setJetpackUsing(boolean using) { this.using = using; }

    @Unique
    double velocity = 0;

    public double getJetpackVelocity() { return velocity; }
    public void setJetpackVelocity(double velocity) { this.velocity = velocity; }

    @Unique Quaternionf rotation = new Quaternionf();

    public Quaternionf getJetpackRotation() { return rotation; }
    public void setJetpackRotation(Quaternionf rotation)  { this.rotation = rotation; }
}
