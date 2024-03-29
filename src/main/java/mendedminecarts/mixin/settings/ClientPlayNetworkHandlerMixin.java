package mendedminecarts.mixin.settings;

import mendedminecarts.settings.SettingSync;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.CustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(
            method = "onCustomPayload",
            at = @At(value = "HEAD"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void onCustomPayload(CustomPayload payload, CallbackInfo ci) {
        if (payload instanceof SettingSync.MendedMinecartsSettingPayload mmsp) {
            ci.cancel();
            SettingSync.handleData(mmsp);
        }
    }
}
