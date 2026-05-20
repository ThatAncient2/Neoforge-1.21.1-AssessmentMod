package net.thatancient2.assessmentprototypemod.client.rendering.entity;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.thatancient2.assessmentprototypemod.AssessmentMod;
import net.thatancient2.assessmentprototypemod.entity.GupEntity;
import net.thatancient2.assessmentprototypemod.entity.ModEntities;

public class GupEntityRenderer extends AzEntityRenderer<GupEntity> {
    private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
            AssessmentMod.MODID,
            "geo/entity/gup.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
            AssessmentMod.MODID,
            "textures/entity/gupentity.png"
    );
    public GupEntityRenderer(EntityRendererProvider.Context context) {
        super(AzEntityRendererConfig.<GupEntity>builder(GEO, TEX)
                .setScale(2.5f)
                //.setAnimatorProvider(GupEntityAnimator::new) this is for later i guess, don't wanna do it now
                .build(),
                context);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.GUP.get(), GupEntityRenderer::new);
    }
}
