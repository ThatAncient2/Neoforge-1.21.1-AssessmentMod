package net.thatancient2.assessmentprototypemod.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thatancient2.assessmentprototypemod.AssessmentMod;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, AssessmentMod.MODID);

    public static final Supplier<EntityType<GupEntity>> GUP =
            ENTITY_TYPES.register("gup", () -> EntityType.Builder.of(GupEntity::new, MobCategory.MISC)
                    .sized(2.5f,1.5f)
                    .build("gup"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}

