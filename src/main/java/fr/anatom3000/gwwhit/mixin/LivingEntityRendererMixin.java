package fr.anatom3000.gwwhit.mixin;

import fr.anatom3000.gwwhit.config.ModConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "setupTransforms", at = @At(value = "TAIL"))
    private void dinnerboneEntities(LivingEntity entity, MatrixStack matrices, float _animationProgress, float _bodyYaw, float _tickDelta, CallbackInfo _info) {
        if ((!(entity instanceof PlayerEntity)) && ModConfig.getLoadedConfig().rendering.other.dinnerboneEntities) {
            matrices.translate(0.0D, entity.getHeight() + 0.1F, 0.0D);
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
        }
    }

}
