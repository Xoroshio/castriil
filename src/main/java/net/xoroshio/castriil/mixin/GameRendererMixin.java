package net.xoroshio.castriil.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(at = @At("HEAD"), method = "getNightVisionScale", cancellable = true)
    public static void getNightVisionScale(LivingEntity entity, float nanoTime, CallbackInfoReturnable<Float> callback){
        callback.setReturnValue(1.0f);
    }
}
