package kr.kro.teamdodoco.lethal_jetpack;

import org.joml.Quaternionf;

public interface IPlayerJetpack
{
    Quaternionf getRenderRotation();

    void onAccelerationStart();
    void onAccelerationEnd();

    boolean getJetpackUsing();
    void setJetpackUsing(boolean jetpackUsing);

    double getJetpackVelocity();
    void setJetpackVelocity(double velocity);

    Quaternionf getJetpackRotation();
    void setJetpackRotation(Quaternionf rotation);
}
