package kr.kro.teamdodoco.lethal_jetpack.client.sound_event;

import net.minecraft.client.sound.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public final class JetpackSounds extends MovingSoundInstance
{
    private final LivingEntity entity;

    public JetpackSounds(LivingEntity entity, SoundEvent soundEvent, SoundCategory soundCategory, float volume, boolean repeat)
    {
        super(soundEvent, soundCategory, SoundInstance.createRandom());

        this.entity = entity;

        this.volume = volume;
        this.repeat = repeat;

        this.setPositionToEntity();
    }

    @Override
    public void tick()
    {
        if (this.entity == null || this.entity.isRemoved() || this.entity.isDead())
        {
            this.setDone();
            return;
        }

        this.setPositionToEntity();
    }

    @Override
    public boolean shouldAlwaysPlay() { return true; }

    void setPositionToEntity()
    {
        this.x = this.entity.getX();
        this.y = this.entity.getY();
        this.z = this.entity.getZ();
    }
}
