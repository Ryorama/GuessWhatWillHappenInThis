package fr.anatom3000.gwwhit.mixin;

import fr.anatom3000.gwwhit.config.ModConfig;
import fr.anatom3000.gwwhit.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MatrixStack.class)
public abstract class MatrixStackMixin {

    private static float increment = 0;

    @Inject(method = "translate(DDD)V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void translate(double x, double y, double z, CallbackInfo ci, MatrixStack.Entry entry) {
        if (ModConfig.getLoadedConfig().rendering.matrices.smallBlocks) {
            entry.getModel().multiply(Matrix4f.translate(iv(x), iv(y), iv(z)));
        }
        if (ModConfig.getLoadedConfig().rendering.matrices.spin) {
            increment = (increment + MinecraftClient.getInstance().getTickDelta() * 0.00001f) % 360;
            entry.getModel().multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(increment));
            entry.getModel().multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(increment));
            entry.getModel().multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(increment));
        }
        entry.getModel().multiply(ModConfig.getLoadedConfig().rendering.matrices.matrixScale);
    }
    
    private static float iv(double v) {
        return MathUtil.BoxedInvert((float)v, -0.1f, 0.1f);
    }

}
