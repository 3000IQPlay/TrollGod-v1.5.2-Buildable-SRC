package me.hollow.trollgod.client.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.hollow.trollgod.api.property.Setting;
import me.hollow.trollgod.api.util.Timer;
import me.hollow.trollgod.api.util.*;
import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.ModuleManifest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.*;

@ModuleManifest(label="AutoTrap", listen=false, category=Module.Category.COMBAT, color=14204927)
public final class AutoTrap
extends Module {
    private final Setting<Type> type = this.register( new Setting <> ( "Type" , Type.PRESERVE ));
    private final Setting<Integer> delay = this.register( new Setting <> ( "Delay/Place" , 50 , 0 , 250 ));
    private final Setting<Integer> blocksPerPlace = this.register( new Setting <> ( "Block/Place" , 8 , 1 , 30 ));
    private final Setting<Double> targetRange = this.register( new Setting <> ( "Target Range" , 10.0 , 0.0 , 20.0 ));
    private final Setting<Double> range = this.register( new Setting <> ( "Place Range" , 6.0 , 0.0 , 10.0 ));
    private final Setting<Boolean> antiSelf = this.register( new Setting <> ( "Anti Self" , false ));
    private final Setting<Boolean> retry = this.register( new Setting <> ( "Retry" , false ));
    private final Setting<Boolean> feet = this.register( new Setting <> ( "Feet" , true ));
    private final Setting<Integer> retryer = this.register(new Setting<Object>("Retries", 4, 1, 15, v -> this.retry.getValue()));
    private final Map<BlockPos, Integer> retries = new HashMap <> ( );
    private final Timer retryTimer = new Timer();
    private final Timer timer = new Timer();
    private boolean didPlace = false;
    private int lastHotbarSlot;
    private int placements = 0;
    public EntityPlayer target;
    public static boolean placing;

    @Override
    public void onEnable() {
        if (this.isNull()) {
            this.disable();
            return;
        }
        this.retries.clear();
    }

    @Override
    public void onDisable() {
        placing = false;
    }

    @Override
    public void onUpdate() {
        if (this.check()) {
            return;
        }
        if (this.type.getValue() == Type.PRESERVE) {
            this.doPreserve(this.target);
        } else {
            this.doNormal(BlockUtil.targets(this.target.getPositionVector(), this.feet.getValue()));
        }
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private List<BlockPos> getPreserve(EntityPlayer player) {
        ArrayList<BlockPos> positions = new ArrayList <> ( );
        positions.add(new BlockPos(player.posX, player.posY + 2.0, player.posZ));
        int placeability = BlockUtil.isPositionPlaceable( positions.get(0) , false);
        switch (placeability) {
            case 0: {
                return new ArrayList <> ( );
            }
            case 3: {
                return positions;
            }
            case 1: {
                if (BlockUtil.isPositionPlaceable( positions.get(0) , false) == 3) {
                    return positions;
                }
            }
            case 2: {
                positions.add(new BlockPos(player.posX + 1.0, player.posY + 1.0, player.posZ));
                positions.add(new BlockPos(player.posX + 1.0, player.posY + 2.0, player.posZ));
            }
        }
        positions.sort(Comparator.comparingDouble(Vec3i::getY));
        return positions;
    }

    private void doPreserve(EntityPlayer player) {
        for (BlockPos position : this.getPreserve(player)) {
            if (BlockUtil.isPositionPlaceable(position, false) != 3) continue;
            this.placeBlock(position);
        }
    }

    private void doNormal(List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(this.mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), this.mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        this.lastHotbarSlot = this.mc.player.inventory.currentItem;
        int obbySlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock( Blocks.OBSIDIAN ));
        Objects.requireNonNull ( this.mc.getConnection ( ) ).sendPacket( new CPacketHeldItemChange(obbySlot) );
        int size = list.size();
        for (Vec3d vec3d3 : list) {
            BlockPos position = new BlockPos ( vec3d3 );
            int placeability = BlockUtil.isPositionPlaceable ( position , true );
            if ( this.retry.getValue ( ) && placeability == 1 && ( this.retries.get ( position ) == null || this.retries.get ( position ) < this.retryer.getValue ( ) ) ) {
                this.placeBlock ( position );
                this.retries.put ( position , this.retries.get ( position ) == null ? 1 : this.retries.get ( position ) + 1 );
                this.retryTimer.reset ( );
                continue;
            }
            if ( placeability != 3 ) continue;
            this.placeBlock ( position );
        }
        this.mc.getConnection().sendPacket( new CPacketHeldItemChange(this.lastHotbarSlot) );
    }

    private boolean check() {
        if (this.isNull()) {
            return true;
        }
        this.didPlace = false;
        this.placements = 0;
        placing = false;
        int obbySlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock( Blocks.OBSIDIAN ));
        if (!this.isEnabled()) {
            return true;
        }
        if (this.retryTimer.hasReached(2000L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (obbySlot == -1) {
            MessageUtil.sendClientMessage(ChatFormatting.RED + "<AutoTrap> No obsidian, toggling.", -3232);
            this.disable();
            return true;
        }
        this.lastHotbarSlot = this.mc.player.inventory.currentItem;
        this.target = this.getTarget(this.targetRange.getValue());
        return this.target == null || !this.timer.hasReached( this.delay.getValue ( ) );
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        int size = this.mc.world.playerEntities.size();
        for (int i = 0; i < size; ++i) {
            EntityPlayer player = this.mc.world.playerEntities.get(i);
            if (EntityUtil.isntValid(player, range) || BlockUtil.getRoundedBlockPos( this.mc.player ).equals( BlockUtil.getRoundedBlockPos( player ) ) && this.antiSelf.getValue ( ) ) continue;
            if (target == null) {
                target = player;
                distance = this.mc.player.getDistanceSq( player );
                continue;
            }
            if (!(this.mc.player.getDistanceSq( player ) < distance)) continue;
            target = player;
            distance = this.mc.player.getDistanceSq( player );
        }
        return target;
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < this.blocksPerPlace.getValue() && this.mc.player.getDistanceSq(pos) <= this.range.getValue() * this.range.getValue()) {
            placing = true;
            BlockUtil.placeBlock(pos);
            this.didPlace = true;
            ++this.placements;
        }
    }

    public
    enum Type {
        PRESERVE,
        NORMAL

    }
}

