package net.xoroshio.castriil.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("RETURN"), method = "hasEffect", cancellable = true)
    public void hasEffect(Holder<MobEffect> effect, CallbackInfoReturnable<Boolean> callback){
        callback.setReturnValue(callback.getReturnValue() || effect == MobEffects.NIGHT_VISION);
    }
}
