package kr.kro.teamdodoco.lethal_jetpack;

import kr.kro.teamdodoco.lethal_jetpack.item.JetpackItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModItems
{
    public static void initialize() {}

    public static Item register(Item item, String id)
    {
        Identifier itemID = new Identifier(LethalJetpack.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item);
    }

    public static final Item JETPACK = register(new JetpackItem(), "jetpack");
}
