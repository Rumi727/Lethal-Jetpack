package kr.kro.teamdodoco.lethal_jetpack;

import kr.kro.teamdodoco.lethal_jetpack.network.JetpackPackets;
import kr.kro.teamdodoco.lethal_jetpack.network.jetpack.JetpackUpdateMotionPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.item.ItemGroups;

public final class LethalJetpack implements ModInitializer
{
    public static final String MOD_ID = "lethal_jetpack";

    @Override
    public void onInitialize()
    {
        PayloadTypeRegistry.playS2C().register(JetpackUpdateMotionPayload.ID, JetpackUpdateMotionPayload.CODEC);

        Debug.log("Items Register...");
        ModItems.initialize();

        Debug.log("Sounds Register...");
        ModSounds.initialize();

        Debug.log("Group Register...");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> itemGroup.add(ModItems.JETPACK));

        Debug.log("Packet Register...");
        JetpackPackets.Register();
    }
}
