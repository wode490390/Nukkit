package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityPortalEnterEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.EnumLevel;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.BlockColor;

/**
 * Created on 2016/1/5 by xtypr.
 * Package cn.nukkit.block in project nukkit .
 * The name NetherPortalBlock comes from minecraft wiki.
 */
public class BlockNetherPortal extends BlockFlowableMeta {

    public BlockNetherPortal() {
        this(0);
    }

    public BlockNetherPortal(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Nether Portal Block";
    }

    @Override
    public int getId() {
        return NETHER_PORTAL;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public int getLightLevel() {
        return 11;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(new BlockAir());
    }

    @Override
    public boolean onBreak(Item item) {
        boolean result = super.onBreak(item);
        for (BlockFace face : BlockFace.values()) {
            Block b = this.getSide(face);
            if (b != null) {
                if (b instanceof BlockNetherPortal) {
                    result &= b.onBreak(item);
                }
            }
        }
        return result;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return this;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (entity instanceof Player) {
            Player p = (Player) entity;
            if (p.isCreative()) {
                EntityPortalEnterEvent ev = new EntityPortalEnterEvent(p, EntityPortalEnterEvent.PortalType.NETHER);
                p.getServer().getPluginManager().callEvent(ev);

                if (!ev.isCancelled()) {
                    Position newPos = EnumLevel.moveToNether(p);
                    if (newPos != null) {
                        for (int x = -1; x < 2; x++) {
                            for (int z = -1; z < 2; z++) {
                                int chunkX = (newPos.getFloorX() >> 4) + x, chunkZ = (newPos.getFloorZ() >> 4) + z;
                                FullChunk chunk = newPos.level.getChunk(chunkX, chunkZ, false);
                                if (chunk == null || !(chunk.isGenerated() || chunk.isPopulated())) {
                                    newPos.level.generateChunk(chunkX, chunkZ, true);
                                }
                            }
                        }
                        p.teleport(newPos.add(1.5, 1, 0.5));
                        p.getServer().getScheduler().scheduleDelayedTask(new Task() {
                            @Override
                            public void onRun(int currentTick) {
                                // dirty hack to make sure chunks are loaded and generated before spawning player
                                p.teleport(newPos.add(1.5, 1, 0.5));
                                spawnPortal(newPos);
                            }
                        }, 20);
                    }
                }
            }
        }
    }

    public static void spawnPortal(Position pos)   {
        Level lvl = pos.level;
        int x = pos.getFloorX(), y = pos.getFloorY(), z = pos.getFloorZ();

        for (int xx = -1; xx < 4; xx++) {
            for (int yy = 1; yy < 4; yy++)  {
                for (int zz = -1; zz < 3; zz++) {
                    lvl.setBlockAt(x + xx, y + yy, z + zz, AIR);
                }
            }
        }

        lvl.setBlockAt(x + 1, y, z, OBSIDIAN);
        lvl.setBlockAt(x + 2, y, z, OBSIDIAN);

        z += 1;
        lvl.setBlockAt(x, y, z, OBSIDIAN);
        lvl.setBlockAt(x + 1, y, z, OBSIDIAN);
        lvl.setBlockAt(x + 2, y, z, OBSIDIAN);
        lvl.setBlockAt(x + 3, y, z, OBSIDIAN);

        z += 1;
        lvl.setBlockAt(x + 1, y, z, OBSIDIAN);
        lvl.setBlockAt(x + 2, y, z, OBSIDIAN);

        z -= 1;
        y += 1;
        lvl.setBlockAt(x, y, z, OBSIDIAN);
        lvl.setBlockAt(x + 1, y, z, NETHER_PORTAL);
        lvl.setBlockAt(x + 2, y, z, NETHER_PORTAL);
        lvl.setBlockAt(x + 3, y, z, OBSIDIAN);

        y += 1;
        lvl.setBlockAt(x, y, z, OBSIDIAN);
        lvl.setBlockAt(x + 1, y, z, NETHER_PORTAL);
        lvl.setBlockAt(x + 2, y, z, NETHER_PORTAL);
        lvl.setBlockAt(x + 3, y, z, OBSIDIAN);

        y += 1;
        lvl.setBlockAt(x, y, z, OBSIDIAN);
        lvl.setBlockAt(x + 1, y, z, NETHER_PORTAL);
        lvl.setBlockAt(x + 2, y, z, NETHER_PORTAL);
        lvl.setBlockAt(x + 3, y, z, OBSIDIAN);

        y += 1;
        lvl.setBlockAt(x, y, z, OBSIDIAN);
        lvl.setBlockAt(x + 1, y, z, OBSIDIAN);
        lvl.setBlockAt(x + 2, y, z, OBSIDIAN);
        lvl.setBlockAt(x + 3, y, z, OBSIDIAN);
    }
}
