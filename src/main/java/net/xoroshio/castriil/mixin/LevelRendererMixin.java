package net.xoroshio.castriil.mixin;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(at = @At("RETURN"), method = "shouldShowEntityOutlines", cancellable = true)
    public void onShouldEntityAppearGlowing(CallbackInfoReturnable<Boolean> callback) {
        callback.setReturnValue(true);
    }
}
