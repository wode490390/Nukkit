package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;

/**
 * Created on 2015/11/21 by xtypr.
 * Package cn.nukkit.level.particle in project Nukkit .
 */
public class MobSpawnParticle extends Particle {

    protected final int width;
    protected final int height;

    public MobSpawnParticle(Vector3 pos, float width, float height) {
        super(pos.x, pos.y, pos.z);
        this.width = (int) width;
        this.height = (int) height;
    }

    @Override
    public DataPacket[] encode() {
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_PARTICLE_SPAWN;
        pk.position = this.asVector3f();
        pk.data = (this.width & 0xff) + ((this.height & 0xff) << 8);

        return new DataPacket[]{pk};
    }
}
