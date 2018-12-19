package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.Skin;

import java.util.UUID;

public class PlayerSkinPacket extends DataPacket {

    public UUID uuid;
    public Skin skin;
    public String newSkinName;
    public String oldSkinName;
    public boolean premium;

    @Override
    public int pid() {
        return ProtocolInfo.PLAYER_SKIN_PACKET;
    }

    @Override
    public void decode() {
        uuid = getUUID();
        skin = new Skin();
        skin.setSkinId(getString());
        newSkinName = getString();
        oldSkinName = getString();
        skin.setSkinData(getByteArray());
        skin.setCapeData(getByteArray());
        skin.setGeometryName(getString());
        skin.setGeometryData(getString());
        premium = getBoolean();
    }

    @Override
    public void encode() {
        reset();
        putUUID(uuid);
        putString(skin.getSkinId());
        putString(newSkinName);
        putString(oldSkinName);
        putByteArray(skin.getSkinData());
        putByteArray(skin.getCapeData());
        putString(skin.getGeometryName());
        putString(skin.getGeometryData());
        putBoolean(premium);
    }
}
