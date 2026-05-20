package net.thatancient2.assessmentprototypemod.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.thatancient2.assessmentprototypemod.AssessmentMod;
import net.thatancient2.assessmentprototypemod.entity.GupEntity;
import net.thatancient2.assessmentprototypemod.entity.ModEntities;

@EventBusSubscriber(modid = AssessmentMod.MODID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent e) {
        e.put(ModEntities.GUP.get(), GupEntity.createAttributes().build());
    }
}
