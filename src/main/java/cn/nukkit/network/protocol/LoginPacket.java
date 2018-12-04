package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.Skin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by on 15-10-13.
 */
public class LoginPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.LOGIN_PACKET;

    public static final int EDITION_POCKET = 0;

    public String username;
    public int protocol;
    public UUID clientUUID;
    public long clientId;
    public String xuid = "";
    public String identityPublicKey = "";
    public String serverAddress = "";
    public String locale = "en_US";
    public Skin skin = new Skin();

    public Map<String, List<String>> chainData; //the "chain" index contains one or more JWTs
    public String clientDataJwt = "";
    public JsonObject clientData; //decoded payload of the clientData JWT

    /**
     * This field may be used by plugins to bypass keychain verification. It should only be used for plugins such as
     * Specter where passing verification would take too much time and not be worth it.
     */
    public boolean skipVerification = false;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public boolean canBeSentBeforeLogin() {
        return true;
    }

    @Override
    public boolean mayHaveUnreadBytes() {
        return this.protocol != ProtocolInfo.CURRENT_PROTOCOL;
    }

    @Override
    public void decode() {
        this.protocol = this.getInt();
        if (this.protocol == 0) {
            this.setOffset(this.getOffset() + 2);
            this.protocol = this.getInt();
        }
        this.setBuffer(this.getByteArray(), 0);
        this.decodeConnectionRequest();
    }

    @Override
    public void encode() {

    }

    public int getProtocol() {
        return this.protocol;
    }

    protected void decodeConnectionRequest() {
        this.chainData = new Gson().fromJson(new String(this.get(this.getLInt()), StandardCharsets.UTF_8),
                new TypeToken<Map<String, List<String>>>() {
                }.getType());
        if (this.chainData.isEmpty() || !this.chainData.containsKey("chain") || this.chainData.get("chain").isEmpty()) return;
        for (String chain : this.chainData.get("chain")) {
            JsonObject webtoken = this.decodeToken(chain);
            if (webtoken == null) continue;
            if (webtoken.has("extraData")) {
                JsonObject extra = webtoken.get("extraData").getAsJsonObject();
                if (extra.has("displayName")) this.username = extra.get("displayName").getAsString();
                if (extra.has("identity")) this.clientUUID = UUID.fromString(extra.get("identity").getAsString());
                if (extra.has("XUID")) this.xuid = extra.get("XUID").getAsString();
            }

            if (webtoken.has("identityPublicKey")) this.identityPublicKey = webtoken.get("identityPublicKey").getAsString();
        }

        this.clientDataJwt = new String(this.get(this.getLInt()));
        this.clientData = this.decodeToken(this.clientDataJwt);

        if (this.clientData.has("ClientRandomId")) this.clientId = this.clientData.get("ClientRandomId").getAsLong();
        if (this.clientData.has("ServerAddress")) this.serverAddress = this.clientData.get("ServerAddress").getAsString();

        if (this.clientData.has("LanguageCode")) this.locale = this.clientData.get("LanguageCode").getAsString();

        this.skin = new Skin();

        if (this.clientData.has("SkinId")) this.skin.setSkinId(this.clientData.get("SkinId").getAsString());
        if (this.clientData.has("SkinData")) this.skin.setSkinData(Base64.getDecoder().decode(this.clientData.get("SkinData").getAsString()));
        if (this.clientData.has("CapeData")) this.skin.setCapeData(Base64.getDecoder().decode(this.clientData.get("CapeData").getAsString()));
        if (this.clientData.has("SkinGeometryName")) this.skin.setGeometryName(this.clientData.get("SkinGeometryName").getAsString());
        if (this.clientData.has("SkinGeometry")) this.skin.setGeometryData(new String(Base64.getDecoder().decode(this.clientData.get("SkinGeometry").getAsString()), StandardCharsets.UTF_8));
    }

    private JsonObject decodeToken(String token) {
        String[] base = token.split("\\.");
        if (base.length < 2) return null;
        return new Gson().fromJson(new String(Base64.getDecoder().decode(base[1]), StandardCharsets.UTF_8), JsonObject.class);
    }
}
