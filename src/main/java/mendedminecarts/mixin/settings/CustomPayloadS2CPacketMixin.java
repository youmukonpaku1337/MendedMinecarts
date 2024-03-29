package mendedminecarts.mixin.settings;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mendedminecarts.settings.SettingSync;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CustomPayloadS2CPacket.class)
public class CustomPayloadS2CPacketMixin {
    //Add the MendedMinecartsSetting to the Reader
    @WrapOperation(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    remap = false,
                    target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"
            )
    )
    private static ImmutableMap<Identifier, PacketByteBuf.PacketReader<? extends CustomPayload>> put_MendedMinecartsSettings(ImmutableMap.Builder<Identifier, PacketByteBuf.PacketReader<? extends CustomPayload>> instance, Operation<ImmutableMap<Identifier, PacketByteBuf.PacketReader<? extends CustomPayload>>> original) {
        instance.put(SettingSync.CHANNEL, SettingSync.MendedMinecartsSettingPayload::new);
        return original.call(instance);
    }
}
