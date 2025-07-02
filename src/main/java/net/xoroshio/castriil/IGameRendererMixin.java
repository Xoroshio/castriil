package net.xoroshio.castriil;

import net.minecraft.client.Camera;

public interface IGameRendererMixin {

    float getFov(Camera camera, float partialTicks);
}
