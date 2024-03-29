package mendedminecarts.settings;

import mendedminecarts.MendedMinecartsMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SettingSync {
    public static final Identifier CHANNEL = new Identifier("mendedminecarts:settings");
    public static PlayerManager PLAYER_MANAGER = null;

    public static void handleData(MendedMinecartsSettingPayload payload) {
        try {
            payload.setting.setFromStringValue(payload.value);
        } catch (Exception ignored) {

        }
    }

    public record MendedMinecartsSettingPayload(Setting setting, String value) implements CustomPayload {

        public MendedMinecartsSettingPayload(Setting setting) {
            this(setting, setting.getStringValue());
        }

        public MendedMinecartsSettingPayload(PacketByteBuf buf) {
            this(checkVersionAndGetSetting(buf), buf.readString());
        }

        private static Setting checkVersionAndGetSetting(PacketByteBuf buf) {
            if (buf.readInt() != MendedMinecartsMod.SETTING_VERSION) {
                throw new IllegalArgumentException("Invalid setting version");
            }
            return MendedMinecartsMod.FLAT_SETTINGS.get(buf.readInt());
        }

        @Override
        public void write(PacketByteBuf buf) {
            buf.writeInt(MendedMinecartsMod.SETTING_VERSION);
            buf.writeInt(MendedMinecartsMod.FLAT_SETTINGS.indexOf(setting));
            buf.writeString(value);
        }

        @Override
        public Identifier id() {
            return CHANNEL;
        }
    }

    public static MendedMinecartsSettingPayload makeSettingPacket(Setting setting) {
        return new MendedMinecartsSettingPayload(setting);
    }

    public static void updateAllToPlayer(ServerPlayerEntity player) {
        for (Setting setting : MendedMinecartsMod.FLAT_SETTINGS) {
            MendedMinecartsSettingPayload payload = makeSettingPacket(setting);
            player.networkHandler.sendPacket(new CustomPayloadS2CPacket(payload));
        }
    }

    public static void updateToClients(Setting setting) {
        PlayerManager playerManager = PLAYER_MANAGER;
        if (playerManager == null) {
            return;
        }
        MendedMinecartsSettingPayload payload = makeSettingPacket(setting);
        playerManager.sendToAll(new CustomPayloadS2CPacket(payload));
    }
}
