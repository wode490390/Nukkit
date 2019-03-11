package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.MapDecorator;
import cn.nukkit.network.protocol.types.MapTrackedObject;
import cn.nukkit.utils.Utils;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Created by CreeperFace on 5.3.2017.
 */
public class ClientboundMapItemDataPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET;

    public static final int TEXTURE_UPDATE = 0x2;
    public static final int DECORATIONS_UPDATE = 0x4;
    public static final int ENTITIES_UPDATE = 0x8;

    public long mapId;
    public int update;
    public byte scale;
    public int width;
    public int height;
    public int offsetX;
    public int offsetZ;

    public byte dimensionId;

    public int[] colors = new int[0];
    public BufferedImage image;

    public MapDecorator[] decorators = new MapDecorator[0];
    public MapTrackedObject[] objects = new MapTrackedObject[0];
    public long[] eids = new long[0];

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(mapId);

        int update = 0;
        if (this.colors != null && this.colors.length > 0 || image != null) {
            update |= TEXTURE_UPDATE;
        }
        if (this.decorators != null && this.decorators.length > 0 && this.objects != null && this.objects.length > 0) {
            update |= DECORATIONS_UPDATE;
        }
        if (this.eids != null && eids.length > 0) {
            update |= ENTITIES_UPDATE;
        }

        this.putUnsignedVarInt(update);
        this.putByte(this.dimensionId);

        if ((update & ENTITIES_UPDATE) != 0) {
            this.putUnsignedVarInt(eids.length);
            for (long eid : eids) {
                this.putEntityUniqueId(eid);
            }
        }

        if ((update & 0xe) != 0) {
            this.putByte(this.scale);
        }

        if ((update & DECORATIONS_UPDATE) != 0) {
            this.putUnsignedVarInt(this.objects.length);
            for (MapTrackedObject object : this.objects) {
                switch (object.type) {
                    case MapTrackedObject.TYPE_BLOCK:
                        this.putLInt(object.type);
                        this.putBlockPosition(object.x, object.y, object.z);
                        break;
                    case MapTrackedObject.TYPE_ENTITY:
                        this.putLInt(object.type);
                        this.putEntityUniqueId(object.entityUniqueId);
                        break;
                }
            }

            this.putUnsignedVarInt(this.decorators.length);
            for (MapDecorator decorator : this.decorators) {
                this.putByte(decorator.img);
                this.putByte(decorator.rot);
                this.putByte(decorator.offsetX);
                this.putByte(decorator.offsetZ);
                this.putString(decorator.label != null ? decorator.label : "");
                this.putUnsignedVarInt(Utils.toRGB((byte) decorator.color.getRed(), (byte) decorator.color.getGreen(), (byte) decorator.color.getBlue(), (byte) 0xff));
            }
        }

        if ((update & TEXTURE_UPDATE) != 0) {
            this.putVarInt(this.width);
            this.putVarInt(this.height);
            this.putVarInt(this.offsetX);
            this.putVarInt(this.offsetZ);

            if (this.image != null) {
                this.putUnsignedVarInt(this.width * this.height);
                for (int y = 0; y < this.width; y++) {
                    for (int x = 0; x < this.height; x++) {
                        Color color = new Color(this.image.getRGB(x, y), true);
                        this.putUnsignedVarInt(Utils.toRGB((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) 0xff));
                    }
                }
                this.image.flush();
            } else if (this.colors.length > 0) {
                this.putUnsignedVarInt(this.colors.length);
                for (int color : this.colors) {
                    this.putUnsignedVarInt(color);
                }
            }
        }
    }
}
