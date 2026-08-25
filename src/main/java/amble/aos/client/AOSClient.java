package amble.aos.client;

import amble.aos.client.renderers.CybermanRenderer;
import amble.aos.client.renderers.LaserBoltRenderer;
import amble.aos.core.AOSEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class AOSClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(AOSEntities.MONDASIAN_CYBERMAN, CybermanRenderer::new);
        EntityRendererRegistry.register(AOSEntities.LASER_BOLT, LaserBoltRenderer::new);
    }
}
