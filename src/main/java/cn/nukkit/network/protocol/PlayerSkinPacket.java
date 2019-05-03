package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.Skin;
import java.util.UUID;
import lombok.ToString;

@ToString
public class PlayerSkinPacket extends DataPacket implements ClientboundPacket, ServerboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.PLAYER_SKIN_PACKET;

    public UUID uuid;
    public String oldSkinName = "";
    public String newSkinName = "";
    public Skin skin;
    public boolean premiumSkin = false;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.uuid = this.getUUID();

        this.skin = new Skin();

        this.skin.setSkinId(this.getString());
        this.newSkinName = this.getString();
        this.oldSkinName = this.getString();
        this.skin.setSkinData(this.getByteArray());
        this.skin.setCapeData(this.getByteArray());
        this.skin.setGeometryName(this.getString());
        this.skin.setGeometryData(this.getString());

        this.premiumSkin = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUUID(this.uuid);

        this.putString(this.skin.getGeometryName());
        this.putString(this.newSkinName);
        this.putString(this.oldSkinName);
        this.putByteArray(this.skin.getSkinData());
        this.putByteArray(this.skin.getCapeData());
        this.putString(this.skin.getGeometryName());
        this.putString(this.skin.getGeometryData());

        this.putBoolean(this.premiumSkin);
    }
}
