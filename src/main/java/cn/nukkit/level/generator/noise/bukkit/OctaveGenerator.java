package cn.nukkit.level.generator.noise.bukkit;

public abstract class OctaveGenerator {

    protected final NoiseGenerator[] octaves;
    protected double xScale = 1.0d;
    protected double yScale = 1.0d;
    protected double zScale = 1.0d;

    protected OctaveGenerator(NoiseGenerator[] octaves) {
        this.octaves = octaves;
    }

    public void setScale(double scale) {
        this.setXScale(scale);
        this.setYScale(scale);
        this.setZScale(scale);
    }

    public double getXScale() {
        return this.xScale;
    }

    public void setXScale(double scale) {
        this.xScale = scale;
    }

    public double getYScale() {
        return this.yScale;
    }

    public void setYScale(double scale) {
        this.yScale = scale;
    }

    public double getZScale() {
        return this.zScale;
    }

    public void setZScale(double scale) {
        this.zScale = scale;
    }

    public NoiseGenerator[] getOctaves() {
        return this.octaves.clone();
    }

    public double noise(double x, double frequency, double amplitude) {
        return this.noise(x, 0.0d, 0.0d, frequency, amplitude);
    }

    public double noise(double x, double frequency, double amplitude, boolean normalized) {
        return this.noise(x, 0.0d, 0.0d, frequency, amplitude, normalized);
    }

    public double noise(double x, double y, double frequency, double amplitude) {
        return this.noise(x, y, 0.0d, frequency, amplitude);
    }

    public double noise(double x, double y, double frequency, double amplitude, boolean normalized) {
        return this.noise(x, y, 0.0d, frequency, amplitude, normalized);
    }

    public double noise(double x, double y, double z, double frequency, double amplitude) {
        return this.noise(x, y, z, frequency, amplitude, false);
    }

    public double noise(double x, double y, double z, double frequency, double amplitude, boolean normalized) {
        double result = 0.0d;
        double amp = 1.0d;
        double freq = 1.0d;
        double max = 0.0d;

        x *= this.xScale;
        y *= this.yScale;
        z *= this.zScale;
        for (NoiseGenerator octave : this.octaves) {
            result += octave.noise(x * freq, y * freq, z * freq) * amp;
            max += amp;
            freq *= frequency;
            amp *= amplitude;
        }
        if (normalized) {
            result /= max;
        }
        return result;
    }
}
