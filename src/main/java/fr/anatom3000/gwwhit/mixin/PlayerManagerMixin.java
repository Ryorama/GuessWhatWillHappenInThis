package fr.anatom3000.gwwhit.mixin;

import fr.anatom3000.gwwhit.GWWHIT;
import fr.anatom3000.gwwhit.config.ModConfig;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        ServerPlayNetworking.send(player, GWWHIT.CONFIG_SYNC_ID , ModConfig.getHolder().getConfig().toPacketByteBuf());
    }
}
