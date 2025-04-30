package kr.kro.teamdodoco.lethal_jetpack;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public final class ModSounds
{
    public static void initialize() {}

    public static SoundEvent register(String id)
    {
        Identifier soundEventID = Identifier.of(LethalJetpack.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, soundEventID, SoundEvent.of(soundEventID));
    }

    public static final SoundEvent JETPACK_USE = register("jetpack.use");
    public static final SoundEvent JETPACK_IDLE = register("jetpack.idle");
    public static final SoundEvent JETPACK_WARNING = register("jetpack.warning");
    public static final SoundEvent JETPACK_BATTERY_WARNING = register("jetpack.battery_warning");
    public static final SoundEvent JETPACK_EXPLOSION = register("jetpack.explosion");

    public static final SoundEvent JETPACK_USE_MONO = register("jetpack.use_mono");
    public static final SoundEvent JETPACK_IDLE_MONO = register("jetpack.idle_mono");
    public static final SoundEvent JETPACK_WARNING_MONO = register("jetpack.warning_mono");
    public static final SoundEvent JETPACK_BATTERY_WARNING_MONO = register("jetpack.battery_warning_mono");
    public static final SoundEvent JETPACK_EXPLOSION_MONO = register("jetpack.explosion_mono");
}
