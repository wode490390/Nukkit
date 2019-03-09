package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class WhiteSmokeParticle extends GenericParticle {
    public WhiteSmokeParticle(Vector3 pos) {
        super(pos, Particle.TYPE_EVAPORATION);
    }
}
