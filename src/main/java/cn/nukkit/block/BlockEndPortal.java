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
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.BlockColor;

public class BlockEndPortal extends BlockFlowable {

    public BlockEndPortal() {
        this(0);
    }

    public BlockEndPortal(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "End Portal Block";
    }

    @Override
    public int getId() {
        return END_PORTAL;
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
    public double getResistance() {
        return 18000000;
    }

    @Override
    public int getLightLevel() {
        return 15;
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
    public Item toItem() {
        return new ItemBlock(new BlockAir());
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (entity instanceof Player) {
            Player p = (Player) entity;
            EntityPortalEnterEvent ev = new EntityPortalEnterEvent(p, EntityPortalEnterEvent.PortalType.THE_END);
            p.getServer().getPluginManager().callEvent(ev);

            if (!ev.isCancelled()) {
                Position newPos = EnumLevel.moveToTheEnd(p);
                if (newPos != null) {
                    for (int x = -2; x < 3; x++) {
                        for (int z = -2; z < 3; z++) {
                            int chunkX = (newPos.getFloorX() >> 4) + x;
                            int chunkZ = (newPos.getFloorZ() >> 4) + z;
                            FullChunk chunk = newPos.getLevel().getChunk(chunkX, chunkZ, false);
                            if (chunk == null || !(chunk.isGenerated() || chunk.isPopulated())) {
                                newPos.getLevel().generateChunk(chunkX, chunkZ, true);
                            }
                        }
                    }
                    p.teleport(newPos.add(1.5, 1, 0.5));
                    p.getServer().getScheduler().scheduleDelayedTask(new Task() {
                        @Override
                        public void onRun(int currentTick) {
                            // dirty hack to make sure chunks are loaded and generated before spawning player
                            p.teleport(newPos.add(1.5, 1, 0.5));
                            spawnPlatform(newPos);
                        }
                    }, 20);
                }
            }
        }
    }

    public static void spawnPlatform(Position pos) {
        Level lvl = pos.getLevel();
        int x = pos.getFloorX();
        int y = pos.getFloorY();
        int z = pos.getFloorZ();

        for (int xx = x - 2; xx < x + 3; xx++) {
            for (int zz = z - 2; zz < z + 3; zz++)  {
                lvl.setBlockAt(xx, y - 1, zz, OBSIDIAN);
                for (int yy = y; yy < y + 4; yy++) {
                    lvl.setBlockAt(xx, yy, zz, AIR);
                }
            }
        }
    }
}
