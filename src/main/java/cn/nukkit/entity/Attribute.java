package cn.nukkit.entity;
/**
 * Attribute
 *
 * @author Box, MagicDroidX(code), PeratX @ Nukkit Project
 * @since Nukkit 1.0 | Nukkit API 1.0.0
 */

import cn.nukkit.utils.ServerException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Attribute implements Cloneable {

    public static final String MC_PREFIX = "minecraft:";

    public static final String ABSORPTION = MC_PREFIX + "absorption";
    public static final String SATURATION = MC_PREFIX + "player.saturation";
    public static final String EXHAUSTION = MC_PREFIX + "player.exhaustion";
    public static final String KNOCKBACK_RESISTANCE = MC_PREFIX + "knockback_resistance";
    public static final String HEALTH = MC_PREFIX + "health";
    public static final String MOVEMENT_SPEED = MC_PREFIX + "movement";
    public static final String FOLLOW_RANGE = MC_PREFIX + "follow_range";
    public static final String HUNGER = MC_PREFIX + "player.hunger";
    public static final String FOOD = HUNGER;
    public static final String ATTACK_DAMAGE = MC_PREFIX + "attack_damage";
    public static final String EXPERIENCE_LEVEL = MC_PREFIX + "player.level";
    public static final String EXPERIENCE = MC_PREFIX + "player.experience";

    protected String id;
    protected float minValue;
    protected float maxValue;
    protected float defaultValue;
    protected float currentValue;
    protected boolean shouldSend;

    protected boolean desynchronized = true;

    protected static Map<String, Attribute> attributes = new HashMap<String, Attribute>();

    public static void init() {
        addAttribute(ABSORPTION, 0.00f, 340282346638528859811704183484516925440.00f, 0.00f);
        addAttribute(SATURATION, 0.00f, 20.00f, 20.00f);
        addAttribute(EXHAUSTION, 0.00f, 5.00f, 0.0f);
        addAttribute(KNOCKBACK_RESISTANCE, 0.00f, 1.00f, 0.00f);
        addAttribute(HEALTH, 0.00f, 20.00f, 20.00f);
        addAttribute(MOVEMENT_SPEED, 0.00f, 340282346638528859811704183484516925440.00f, 0.10f);
        addAttribute(FOLLOW_RANGE, 0.00f, 2048.00f, 16.00f, false);
        addAttribute(HUNGER, 0.00f, 20.00f, 20.00f);
        addAttribute(ATTACK_DAMAGE, 0.00f, 340282346638528859811704183484516925440.00f, 1.00f, false);
        addAttribute(EXPERIENCE_LEVEL, 0.00f, 24791.00f, 0.00f);
        addAttribute(EXPERIENCE, 0.00f, 1.00f, 0.00f);
        //TODO: minecraft:luck (for fishing?)
        //TODO: minecraft:fall_damage
    }

    public static Attribute addAttribute(String id, float minValue, float maxValue, float defaultValue) {
        return addAttribute(id, minValue, maxValue, defaultValue, true);
    }

    public static Attribute addAttribute(String id, float minValue, float maxValue, float defaultValue, boolean shouldSend) {
        if (minValue > maxValue || defaultValue > maxValue || defaultValue < minValue) {
            throw new IllegalArgumentException("Invalid ranges: min value: " + minValue + ", max value: " + maxValue + ", defaultValue: " + defaultValue);
        }

        return attributes.put(id, new Attribute(id, minValue, maxValue, defaultValue, shouldSend));
    }

    public static Attribute getAttribute(String id) {
        return attributes.containsKey(id) ? attributes.get(id).clone() : null;
    }

    private Attribute(String id, float minValue, float maxValue, float defaultValue) {
        this(id, minValue, maxValue, defaultValue, true);
    }

    private Attribute(String id, float minValue, float maxValue, float defaultValue, boolean shouldSend) {
        this.id = id;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        this.shouldSend = shouldSend;

        this.currentValue = this.defaultValue;
    }

    public float getMinValue() {
        return this.minValue;
    }

    public Attribute setMinValue(float minValue) {
        if (minValue > this.getMaxValue()) {
            throw new IllegalArgumentException("Value " + minValue + " is bigger than the maxValue!");
        }

        if (this.minValue != minValue) {
            this.desynchronized = true;
            this.minValue = minValue;
        }
        return this;
    }

    public float getMaxValue() {
        return this.maxValue;
    }

    public Attribute setMaxValue(float maxValue) {
        if (maxValue < this.getMinValue()) {
            throw new IllegalArgumentException("Value " + maxValue + " is bigger than the minValue!");
        }

        if (this.maxValue != maxValue) {
            this.desynchronized = true;
            this.maxValue = maxValue;
        }
        return this;
    }

    public float getDefaultValue() {
        return this.defaultValue;
    }

    public Attribute setDefaultValue(float defaultValue) {
        if (defaultValue > this.getMaxValue() || defaultValue < this.getMinValue()) {
            throw new IllegalArgumentException("Value " + defaultValue + " exceeds the range!");
        }

        if (this.defaultValue != defaultValue) {
            this.desynchronized = true;
            this.defaultValue = defaultValue;
        }
        return this;
    }

    public void resetToDefault() {
        this.setValue(this.getDefaultValue(), true);
    }

    public float getValue() {
        return this.currentValue;
    }

    public Attribute setValue(float value) {
        return this.setValue(value, false);
    }

    public Attribute setValue(float value, boolean fit) {
        return this.setValue(value, fit, false);
    }

    public Attribute setValue(float value, boolean fit, boolean forceSend) {
        if (value > this.getMaxValue() || value < this.getMinValue()) {
            if (!fit) {
                throw new IllegalArgumentException("Value " + value + " exceeds the range!");
            }
            value = Math.min(Math.max(value, this.getMinValue()), this.getMaxValue());
        }

        if (this.currentValue != value) {
            this.desynchronized = true;
            this.currentValue = value;
        } else if (forceSend) {
            this.desynchronized = true;
        }

        return this;
    }

    public String getId() {
        return this.id;
    }

    public boolean isSyncable() {
        return this.shouldSend;
    }

    public boolean isDesynchronized() {
        return this.shouldSend && this.desynchronized;
    }

    public void markSynchronized() {
        this.markSynchronized(true);
    }

    public void markSynchronized(boolean synced) {
        this.desynchronized = !synced;
    }

    @Override
    public Attribute clone() {
        try {
            return (Attribute) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
