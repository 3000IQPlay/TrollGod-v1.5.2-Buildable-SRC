package me.hollow.trollgod.client.modules.combat;

import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.api.property.Setting;
import me.hollow.trollgod.api.util.EntityUtil;
import me.hollow.trollgod.api.util.ItemUtil;
import me.hollow.trollgod.client.events.UpdateEvent;
import me.hollow.trollgod.client.managers.TPSManager;
import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.ModuleManifest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import tcb.bces.listener.Subscribe;

import java.util.Comparator;

@ModuleManifest(label="KillAura", color=34, category=Module.Category.COMBAT)
public class KillAura
extends Module {
    private final Setting<Boolean> rotate = this.register( new Setting <> ( "Rotate" , false ));
    private final Setting<Boolean> tpsSync = this.register( new Setting <> ( "TPS-Sync" , true ));
    private final Setting<TargetSortingMode> sortMode = this.register( new Setting <> ( "Sort by" , TargetSortingMode.DISTANCE ));
    private final Setting<Float> range = this.register( new Setting <> ( "Range" , 5.0f , 1.0f , 6.0f ));
    private final Setting<SwordMode> sword = this.register( new Setting <> ( "Sword" , SwordMode.REQUIRE ));
    private final Setting<Boolean> players = this.register( new Setting <> ( "Players" , true ));
    private final Setting<Boolean> pigs = this.register( new Setting <> ( "Pigs" , true ));
    public static KillAura INSTANCE;

    public KillAura() {
        INSTANCE = this;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        EntityLivingBase target = this.getTarget();
        if (target != null) {
            switch (this.sword.getValue()) {
                case SWITCH: {
                    int swordSlot = ItemUtil.getItemFromHotbar(ItemSword.class);
                    if (swordSlot == -1) {
                        return;
                    }
                    this.mc.player.inventory.currentItem = swordSlot;
                    this.mc.playerController.updateController();
                    break;
                }
                case REQUIRE: {
                    if (this.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) break;
                    return;
                }
            }
            float ticks = 20.0f - TPSManager.TPS;
            float f = this.tpsSync.getValue ( ) ? -ticks : 0.0f;
            if (this.mc.player.getCooledAttackStrength(f) >= 1.0f) {
                if ( this.rotate.getValue ( ) ) {
                    float[] rotations = this.getRotations( target );
                    this.mc.player.rotationYaw = rotations[0];
                    this.mc.player.rotationPitch = rotations[1];
                }
                this.mc.playerController.attackEntity( this.mc.player , target );
                this.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }

    public EntityLivingBase getTarget() {
        return (EntityLivingBase) this.mc.world.loadedEntityList.stream().filter(this::isValid).min(Comparator.comparingDouble( e -> this.sortMode.getValue() == TargetSortingMode.DISTANCE ? this.mc.player.getDistanceSq(e) : (double)EntityUtil.getHealth((EntityLivingBase)e))).orElse(null);
    }

    private boolean isValid(Entity entity) {
        if (!(entity != this.mc.player && (entity instanceof EntityPlayer && this.players.getValue ( ) || entity instanceof EntityPig && this.pigs.getValue ( ) ))) {
            return false;
        }
        return !(this.mc.player.getDistance(entity) > this.range.getValue ( ) ) && !entity.isDead && !(((EntityLivingBase)entity).getHealth() <= 0.0f) && !TrollGod.INSTANCE.getFriendManager().isFriend(entity.getName());
    }

    public float[] getRotations(Entity ent) {
        return this.getRotationFromPosition(ent.posX, ent.getEntityBoundingBox().maxY - 4.0, ent.posZ);
    }

    public float[] getRotationFromPosition(double x, double y, double z) {
        double xDiff = x - this.mc.player.posX;
        double zDiff = z - this.mc.player.posZ;
        double yDiff = y - this.mc.player.posY;
        double hypotenuse = Math.hypot(xDiff, zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(yDiff, hypotenuse) * 180.0 / Math.PI);
        return new float[]{yaw, pitch};
    }

    public
    enum TargetSortingMode {
        HEALTH,
        DISTANCE

    }

    public
    enum SwordMode {
        REQUIRE,
        SWITCH,
        NONE

    }
}

