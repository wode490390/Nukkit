package cn.nukkit.network.protocol.types;

import java.awt.Color;
import lombok.ToString;

@ToString
public class MapDecorator {

    public byte img;
    public byte rot;
    public byte offsetX;
    public byte offsetZ;
    public String label = "";
    public Color color;
}
