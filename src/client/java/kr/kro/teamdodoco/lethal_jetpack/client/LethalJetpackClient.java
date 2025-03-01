package kr.kro.teamdodoco.lethal_jetpack.client;

import kr.kro.teamdodoco.lethal_jetpack.Debug;
import kr.kro.teamdodoco.lethal_jetpack.client.item.JetpackItemTick;
import kr.kro.teamdodoco.lethal_jetpack.client.network.JetpackPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.Util;

public final class LethalJetpackClient implements ClientModInitializer
{
    static long lastTime = 0;
    public static float deltaTime = 0;

    @Override
    public void onInitializeClient()
    {
        Debug.log("Initialize Client...");

        ClientTickEvents.START_CLIENT_TICK.register(JetpackItemTick::onUpdate);
        WorldRenderEvents.START.register((context) ->
        {
            long time = Util.getMeasuringTimeNano();
            deltaTime = (time - lastTime) / 1000000000f;
            lastTime = time;
        });

        HudRenderCallback.EVENT.register(JetpackItemTick::onHudRender);

        Debug.log("Packet Register...");
        JetpackPackets.Register();
    }
}
