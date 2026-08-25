package amble.aos.client.renderers;

import amble.aos.AOS;
import amble.aos.client.models.entity.MondasianCybermanModel;
import amble.aos.core.entities.CybermanBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;

@Environment(EnvType.CLIENT)
public class CybermanGlowingLayer<T extends CybermanBase, M extends MondasianCybermanModel<T>> extends EyesLayer<T, M> {
    private static final RenderType MONDASIAN_EMISSION = RenderType.eyes(AOS.id("textures/entity/cybermen/mondasian_emission.png"));

    public CybermanGlowingLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    public RenderType renderType() {
        return MONDASIAN_EMISSION;
    }
}
