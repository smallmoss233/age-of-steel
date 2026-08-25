package amble.aos.client.renderers;

import amble.aos.AOS;
import amble.aos.client.models.entity.MondasianCybermanModel;
import amble.aos.core.entities.CybermanBase;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CybermanRenderer<T extends CybermanBase> extends MobRenderer<T, MondasianCybermanModel<T>> {

    public CybermanRenderer(EntityRendererProvider.Context context) {
        super(context, new MondasianCybermanModel<T>(MondasianCybermanModel.createBodyLayer().bakeRoot()), 0.5f);
        this.addLayer(new CybermanGlowingLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(CybermanBase entity) {
        return AOS.id("textures/entity/cybermen/" + entity.getLoc() + ".png");
    }
}
