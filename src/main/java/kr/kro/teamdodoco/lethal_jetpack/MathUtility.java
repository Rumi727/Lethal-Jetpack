package kr.kro.teamdodoco.lethal_jetpack;

import net.minecraft.util.math.MathHelper;

public final class MathUtility
{
    public static float repeat(float t, float length) { return MathHelper.clamp(t - (MathHelper.floor(t / length) * length), 0, length); }

    public static float moveTowards(float current, float target, float maxDelta)
    {
        if (MathHelper.abs(target - current) <= maxDelta)
            return target;

        return current + (MathHelper.sign(target - current) * maxDelta);
    }
}
