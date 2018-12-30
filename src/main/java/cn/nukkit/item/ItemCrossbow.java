package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityShootCrossbowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

import java.util.Random;

public class ItemCrossbow extends ItemTool {

    public ItemCrossbow() {
        this(0, 1);
    }

    public ItemCrossbow(Integer meta) {
        this(meta, 1);
    }

    public ItemCrossbow(Integer meta, int count) {
        super(CROSSBOW, meta, count, "Crossbow");
    }

    @Override
    public int getMaxDurability() {
        return ItemTool.DURABILITY_CROSSBOW;
    }

    @Override
    public int getEnchantAbility() {
        return 1;
    }

    public boolean onReleaseUsing(Player player) {
        Item itemArrow = Item.get(Item.ARROW, 0, 1);

        if (player.isSurvival() && !player.getInventory().contains(itemArrow)) {
            player.getInventory().sendContents(player);
            return false;
        }

        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", player.x))
                        .add(new DoubleTag("", player.y + player.getEyeHeight()))
                        .add(new DoubleTag("", player.z)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
                        .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
                        .add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", (player.yaw > 180 ? 360 : 0) - (float) player.yaw))
                        .add(new FloatTag("", (float) -player.pitch)))
                .putDouble("damage", 6);

        int diff = (Server.getInstance().getTick() - player.getStartActionTick());
        double p = (double) diff / 20;

        double f = Math.min((p * p + p * 2) / 3, 1) * 2;
        EntityShootCrossbowEvent entityShootCrossbowEvent = new EntityShootCrossbowEvent(player, this, new EntityArrow(player.chunk, nbt, player, f == 2), f);

        if (f < 0.1 || diff < 5) {
            entityShootCrossbowEvent.setCancelled();
        }

        Server.getInstance().getPluginManager().callEvent(entityShootCrossbowEvent);
        if (entityShootCrossbowEvent.isCancelled()) {
            entityShootCrossbowEvent.getProjectile().kill();
            player.getInventory().sendContents(player);
        } else {
            entityShootCrossbowEvent.getProjectile().setMotion(entityShootCrossbowEvent.getProjectile().getMotion().multiply(entityShootCrossbowEvent.getForce()));
            if (player.isSurvival()) {
                player.getInventory().removeItem(itemArrow);
                if (!this.isUnbreakable()) {
                    Enchantment durability = this.getEnchantment(Enchantment.ID_DURABILITY);
                    if (!(durability != null && durability.getLevel() > 0 && (100 / (durability.getLevel() + 1)) <= new Random().nextInt(100))) {
                        this.setDamage(this.getDamage() + 1);
                        if (this.getDamage() >= getMaxDurability()) {
                            this.count--;
                        }
                    }
                }
            }
            if (entityShootCrossbowEvent.getProjectile() instanceof EntityProjectile) {
                ProjectileLaunchEvent projectev = new ProjectileLaunchEvent(entityShootCrossbowEvent.getProjectile());
                Server.getInstance().getPluginManager().callEvent(projectev);
                if (projectev.isCancelled()) {
                    entityShootCrossbowEvent.getProjectile().kill();
                } else {
                    entityShootCrossbowEvent.getProjectile().spawnToAll();
                    player.level.addSound(player, Sound.CROSSBOW_SHOOT, 1, 1, player.getViewers().values());
                }
            } else {
                entityShootCrossbowEvent.getProjectile().spawnToAll();
            }
        }

        return true;
    }

    @Override
    public boolean isExperimental() {
        return true;
    }
}
