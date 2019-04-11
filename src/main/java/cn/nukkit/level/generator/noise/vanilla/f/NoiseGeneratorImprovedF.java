package cn.nukkit.level.generator.noise.vanilla.f;

import cn.nukkit.math.NukkitRandom;

public class NoiseGeneratorImprovedF {
    private static final float[] GRAD_X = new float[]{1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f};
    private static final float[] GRAD_Y = new float[]{1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f};
    private static final float[] GRAD_Z = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, -1.0f};
    private static final float[] GRAD_2X = new float[]{1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f};
    private static final float[] GRAD_2Z = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, -1.0f};
    private final int[] permutations;
    public float xCoord;
    public float yCoord;
    public float zCoord;

    public NoiseGeneratorImprovedF() {
        this(new NukkitRandom(System.currentTimeMillis()));
    }

    public NoiseGeneratorImprovedF(NukkitRandom p_i45469_1_) {
        this.permutations = new int[512];
        this.xCoord = p_i45469_1_.nextFloat() * 256.0f;
        this.yCoord = p_i45469_1_.nextFloat() * 256.0f;
        this.zCoord = p_i45469_1_.nextFloat() * 256.0f;

        for (int i = 0; i < 256; this.permutations[i] = i++) {
            ;
        }

        for (int l = 0; l < 256; ++l) {
            int j = p_i45469_1_.nextBoundedInt(256 - l) + l;
            int k = this.permutations[l];
            this.permutations[l] = this.permutations[j];
            this.permutations[j] = k;
            this.permutations[l + 256] = this.permutations[l];
        }
    }

    public final float lerp(float p_76311_1_, float p_76311_3_, float p_76311_5_) {
        return p_76311_3_ + p_76311_1_ * (p_76311_5_ - p_76311_3_);
    }

    public final float grad2(int p_76309_1_, float p_76309_2_, float p_76309_4_) {
        int i = p_76309_1_ & 15;
        return GRAD_2X[i] * p_76309_2_ + GRAD_2Z[i] * p_76309_4_;
    }

    public final float grad(int p_76310_1_, float p_76310_2_, float p_76310_4_, float p_76310_6_) {
        int i = p_76310_1_ & 15;
        return GRAD_X[i] * p_76310_2_ + GRAD_Y[i] * p_76310_4_ + GRAD_Z[i] * p_76310_6_;
    }

    /*
     * noiseArray should be xSize*ySize*zSize in size
     */
    public void populateNoiseArray(float[] noiseArray, float xOffset, float yOffset, float zOffset, int xSize, int ySize, int zSize, float xScale, float yScale, float zScale, float noiseScale) {
        if (ySize == 1) {
            int i5;
            int j5;
            int j;
            int k5;
            float d14;
            float d15;
            int l5 = 0;
            float d16 = 1 / noiseScale;

            for (int j2 = 0; j2 < xSize; ++j2) {
                float d17 = xOffset + j2 * xScale + this.xCoord;
                int i6 = (int) d17;

                if (d17 < i6) {
                    --i6;
                }

                int k2 = i6 & 255;
                d17 -= i6;
                float d18 = d17 * d17 * d17 * (d17 * (d17 * 6.0f - 15.0f) + 10.0f);

                for (int j6 = 0; j6 < zSize; ++j6) {
                    float d19 = zOffset + j6 * zScale + this.zCoord;
                    int k6 = (int) d19;

                    if (d19 < k6) {
                        --k6;
                    }

                    int l6 = k6 & 255;
                    d19 -= k6;
                    float d20 = d19 * d19 * d19 * (d19 * (d19 * 6.0f - 15.0f) + 10.0f);
                    i5 = this.permutations[k2] + 0;
                    j5 = this.permutations[i5] + l6;
                    j = this.permutations[k2 + 1] + 0;
                    k5 = this.permutations[j] + l6;
                    d14 = this.lerp(d18, this.grad2(this.permutations[j5], d17, d19), this.grad(this.permutations[k5], d17 - 1.0f, 0.0f, d19));
                    d15 = this.lerp(d18, this.grad(this.permutations[j5 + 1], d17, 0.0f, d19 - 1.0f), this.grad(this.permutations[k5 + 1], d17 - 1.0f, 0.0f, d19 - 1.0f));
                    float d21 = this.lerp(d20, d14, d15);
                    int i7 = l5++;
                    noiseArray[i7] += d21 * d16;
                }
            }
        } else {
            int i = 0;
            float d0 = 1.0f / noiseScale;
            int k = -1;
            int l;
            int i1;
            int j1;
            int k1;
            int l1;
            int i2;
            float d1 = 0;
            float d2 = 0;
            float d3 = 0;
            float d4 = 0;

            for (int l2 = 0; l2 < xSize; ++l2) {
                float d5 = xOffset + l2 * xScale + this.xCoord;
                int i3 = (int) d5;

                if (d5 < i3) {
                    --i3;
                }

                int j3 = i3 & 255;
                d5 -= i3;
                float d6 = d5 * d5 * d5 * (d5 * (d5 * 6.0f - 15.0f) + 10.0f);

                for (int k3 = 0; k3 < zSize; ++k3) {
                    float d7 = zOffset + k3 * zScale + this.zCoord;
                    int l3 = (int) d7;

                    if (d7 < l3) {
                        --l3;
                    }

                    int i4 = l3 & 255;
                    d7 -= l3;
                    float d8 = d7 * d7 * d7 * (d7 * (d7 * 6.0f - 15.0f) + 10.0f);

                    for (int j4 = 0; j4 < ySize; ++j4) {
                        float d9 = yOffset + j4 * yScale + this.yCoord;
                        int k4 = (int) d9;

                        if (d9 < k4) {
                            --k4;
                        }

                        int l4 = k4 & 255;
                        d9 -= k4;
                        float d10 = d9 * d9 * d9 * (d9 * (d9 * 6.0f - 15.0f) + 10.0f);

                        if (j4 == 0 || l4 != k) {
                            k = l4;
                            l = this.permutations[j3] + l4;
                            i1 = this.permutations[l] + i4;
                            j1 = this.permutations[l + 1] + i4;
                            k1 = this.permutations[j3 + 1] + l4;
                            l1 = this.permutations[k1] + i4;
                            i2 = this.permutations[k1 + 1] + i4;
                            d1 = this.lerp(d6, this.grad(this.permutations[i1], d5, d9, d7), this.grad(this.permutations[l1], d5 - 1.0f, d9, d7));
                            d2 = this.lerp(d6, this.grad(this.permutations[j1], d5, d9 - 1.0f, d7), this.grad(this.permutations[i2], d5 - 1.0f, d9 - 1.0f, d7));
                            d3 = this.lerp(d6, this.grad(this.permutations[i1 + 1], d5, d9, d7 - 1.0f), this.grad(this.permutations[l1 + 1], d5 - 1.0f, d9, d7 - 1.0f));
                            d4 = this.lerp(d6, this.grad(this.permutations[j1 + 1], d5, d9 - 1.0f, d7 - 1.0f), this.grad(this.permutations[i2 + 1], d5 - 1.0f, d9 - 1.0f, d7 - 1.0f));
                        }

                        float d11 = this.lerp(d10, d1, d2);
                        float d12 = this.lerp(d10, d3, d4);
                        float d13 = this.lerp(d8, d11, d12);
                        int j7 = i++;
                        noiseArray[j7] += d13 * d0;
                    }
                }
            }
        }
    }
}
