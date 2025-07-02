package net.xoroshio.castriil;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xoroshio.castriil.keys.KeyBindings;

@Mod.EventBusSubscriber(modid = Castriil.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegisterEvents {

    @SubscribeEvent
    public static void onKeyBindingRegister(RegisterKeyMappingsEvent registerKeyMappingsEvent){
        KeyBindings.register(registerKeyMappingsEvent::register);
    }
}
