package kr.kro.teamdodoco.lethal_jetpack.network;

import kr.kro.teamdodoco.lethal_jetpack.IPlayerJetpack;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public final class Packets
{
    static final Identifier UPDATE_MOTION_CHANNEL = new Identifier("lethal_jetpack", "update_motion");

    public static void Register()
    {
        ServerPlayNetworking.registerGlobalReceiver(UPDATE_MOTION_CHANNEL, (server, player, handler, buf, responseSender) ->
        {
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();

            if (((IPlayerJetpack)player).getJetpackUsing())
            {
                player.setVelocity(x, y, z);
                player.fallDistance = 0;
            }
        });
    }
}
