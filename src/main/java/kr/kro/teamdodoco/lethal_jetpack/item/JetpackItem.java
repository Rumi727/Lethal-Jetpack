package kr.kro.teamdodoco.lethal_jetpack.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.base.SimpleEnergyItem;

public class JetpackItem extends Item implements SimpleEnergyItem
{
    public JetpackItem()
    {
        super(new FabricItemSettings().rarity(Rarity.EPIC).maxCount(1));
    }

    @Override
    public long getEnergyCapacity(ItemStack var1)
    {
        return 24000;
    }

    @Override
    public long getEnergyMaxInput(ItemStack var1)
    {
        return Long.MAX_VALUE;
    }

    @Override
    public long getEnergyMaxOutput(ItemStack var1)
    {
        return Long.MAX_VALUE;
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }
}
