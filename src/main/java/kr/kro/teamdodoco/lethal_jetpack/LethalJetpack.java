package kr.kro.teamdodoco.lethal_jetpack;

import kr.kro.teamdodoco.lethal_jetpack.network.JetpackPackets;
import kr.kro.teamdodoco.lethal_jetpack.network.Packets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;

public final class LethalJetpack implements ModInitializer
{
    public static final String MOD_ID = "lethal_jetpack";

    @Override
    public void onInitialize()
    {
        Debug.log("Items Register...");
        ModItems.initialize();

        Debug.log("Sounds Register...");
        ModSounds.initialize();

        Debug.log("Group Register...");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> itemGroup.add(ModItems.JETPACK));

        Debug.log("Packet Register...");
        JetpackPackets.Register();
        Packets.Register();
    }
}
