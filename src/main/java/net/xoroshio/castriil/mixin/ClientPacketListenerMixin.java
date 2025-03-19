package net.xoroshio.castriil.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xoroshio.castriil.Castriil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "handleSetEntityMotion", at = @At("HEAD"), cancellable = true)
    public void onHandleSetEntityMotion(ClientboundSetEntityMotionPacket packet, CallbackInfo callback){
        Player player = Minecraft.getInstance().player;
        if(player == null || packet.getId() != player.getId()){
            callback.cancel();
            return;
        }
        Vec3 motion = new Vec3(packet.getXa() / 8000.0D, packet.getYa() / 8000.0D, packet.getZa() / 8000.0D);
        if(Castriil.shouldCancelServerSetClientMotion(player, motion)){
            player.lerpMotion(0, motion.y, 0);
            callback.cancel();
        }
    }
}
