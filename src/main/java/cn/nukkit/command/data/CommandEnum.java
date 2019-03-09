package cn.nukkit.command.data;

import java.util.List;

/**
 * @author CreeperFace
 */
public class CommandEnum {

    private final String name;
    private final List<String> values;

    public CommandEnum(String name, List<String> values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
