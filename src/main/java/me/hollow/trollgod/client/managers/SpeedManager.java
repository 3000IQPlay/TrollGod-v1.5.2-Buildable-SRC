package me.hollow.trollgod.client.managers;

import me.hollow.trollgod.api.interfaces.Minecraftable;
import net.minecraft.util.math.MathHelper;

public class SpeedManager
implements Minecraftable {
    public double speedometerCurrentSpeed = 0.0;

    public void update() {
        double distTraveledLastTickX = SpeedManager.mc.player.posX - SpeedManager.mc.player.prevPosX;
        double distTraveledLastTickZ = SpeedManager.mc.player.posZ - SpeedManager.mc.player.prevPosZ;
        this.speedometerCurrentSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
    }

    public double turnIntoKpH(double input) {
        return (double)MathHelper.sqrt( input ) * 71.2729367892;
    }

    public double getKpH() {
        double speedometerkphdouble = this.turnIntoKpH(this.speedometerCurrentSpeed);
        speedometerkphdouble = (double)Math.round(10.0 * speedometerkphdouble) / 10.0;
        return speedometerkphdouble;
    }
}

