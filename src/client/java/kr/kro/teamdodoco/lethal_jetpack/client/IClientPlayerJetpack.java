package kr.kro.teamdodoco.lethal_jetpack.client;

import kr.kro.teamdodoco.lethal_jetpack.IPlayerJetpack;

public interface IClientPlayerJetpack extends IPlayerJetpack
{
    boolean getAcceleration();
    void setAcceleration(boolean acceleration);
}
