package cn.nukkit.level.generator.noise.bukkit;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.math.NukkitRandom;

public class PerlinNoiseGenerator extends NoiseGenerator {

    protected static final int[][] grad3 = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    private static final PerlinNoiseGenerator instance = new PerlinNoiseGenerator();

    protected PerlinNoiseGenerator() {
        int[] p = {151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};
        for (int i = 0; i < 512; ++i) {
            this.perm[i] = p[i & 0xFF];
        }
    }

    public PerlinNoiseGenerator(Level world) {
        this(new NukkitRandom(world.getSeed()));
    }

    public PerlinNoiseGenerator(ChunkManager level) {
        this(new NukkitRandom(level.getSeed()));
    }

    public PerlinNoiseGenerator(long seed) {
        this(new NukkitRandom(seed));
    }

    public PerlinNoiseGenerator(NukkitRandom rand) {
        this.offsetX = rand.nextDouble() * 256.0d;
        this.offsetY = rand.nextDouble() * 256.0d;
        this.offsetZ = rand.nextDouble() * 256.0d;
        for (int i = 0; i < 256; ++i) {
            this.perm[i] = rand.nextBoundedInt(256);
        }
        for (int i = 0; i < 256; ++i) {
            int pos = rand.nextBoundedInt(256 - i) + i;
            int old = this.perm[i];
            this.perm[i] = this.perm[pos];
            this.perm[pos] = old;
            this.perm[i + 256] = this.perm[i];
        }
    }

    public static double getNoise(double x) {
        return instance.noise(x);
    }

    public static double getNoise(double x, double y) {
        return instance.noise(x, y);
    }

    public static double getNoise(double x, double y, double z) {
        return instance.noise(x, y, z);
    }

    public static PerlinNoiseGenerator getInstance() {
        return instance;
    }

    @Override
    public double noise(double x, double y, double z) {
        x += this.offsetX;
        y += this.offsetY;
        z += this.offsetZ;

        int floorX = NoiseGenerator.floor(x);
        int floorY = NoiseGenerator.floor(y);
        int floorZ = NoiseGenerator.floor(z);

        int X = floorX & 0xFF;
        int Y = floorY & 0xFF;
        int Z = floorZ & 0xFF;

        x -= floorX;
        y -= floorY;
        z -= floorZ;

        double fX = NoiseGenerator.fade(x);
        double fY = NoiseGenerator.fade(y);
        double fZ = NoiseGenerator.fade(z);

        int A = this.perm[X] + Y;
        int AA = this.perm[A] + Z;
        int AB = this.perm[A + 1] + Z;
        int B = this.perm[X + 1] + Y;
        int BA = this.perm[B] + Z;
        int BB = this.perm[B + 1] + Z;

        return NoiseGenerator.lerp(fZ, NoiseGenerator.lerp(fY, NoiseGenerator.lerp(fX, NoiseGenerator.grad(this.perm[AA], x, y, z), NoiseGenerator.grad(this.perm[BA], x - 1.0d, y, z)), NoiseGenerator.lerp(fX, NoiseGenerator.grad(this.perm[AB], x, y - 1.0d, z), NoiseGenerator.grad(this.perm[BB], x - 1.0d, y - 1.0d, z))), NoiseGenerator.lerp(fY, NoiseGenerator.lerp(fX, NoiseGenerator.grad(this.perm[AA + 1], x, y, z - 1.0d), NoiseGenerator.grad(this.perm[BA + 1], x - 1.0d, y, z - 1.0d)), NoiseGenerator.lerp(fX, NoiseGenerator.grad(this.perm[AB + 1], x, y - 1.0d, z - 1.0d), NoiseGenerator.grad(this.perm[BB + 1], x - 1.0d, y - 1.0d, z - 1.0d))));
    }

    public static double getNoise(double x, int octaves, double frequency, double amplitude) {
        return instance.noise(x, octaves, frequency, amplitude);
    }

    public static double getNoise(double x, double y, int octaves, double frequency, double amplitude) {
        return instance.noise(x, y, octaves, frequency, amplitude);
    }

    public static double getNoise(double x, double y, double z, int octaves, double frequency, double amplitude) {
        return instance.noise(x, y, z, octaves, frequency, amplitude);
    }
}
