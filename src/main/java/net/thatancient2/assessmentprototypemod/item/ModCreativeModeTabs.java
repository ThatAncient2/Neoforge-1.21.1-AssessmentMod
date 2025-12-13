package net.thatancient2.assessmentprototypemod.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thatancient2.assessmentprototypemod.AssessmentMod;
import net.thatancient2.assessmentprototypemod.block.ModBlocks;
import org.checkerframework.checker.index.qual.PolyUpperBound;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AssessmentMod.MODID);

    //regustering items tab
    public static final Supplier<CreativeModeTab> ASSESSMENT_ITEMS_TAB = CREATIVE_MODE_TAB.register("assessment_items_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.UKELELE.get()))
            .title(Component.translatable("creativetab.assessmentmod.assessment_items"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModItems.UKELELE);
                output.accept(ModItems.ATGMISSILEMKI);
                output.accept(ModItems.EGOCENTRISM);
                output.accept(ModItems.LUNARCOIN);
                output.accept(ModItems.GESTUREOFTHEDROWNED);
                output.accept(ModItems.IRRADIANTPEARL);
                output.accept(ModItems.NOXIOUSTHORN);
                output.accept(ModItems.PLANULA);
                output.accept(ModItems.POLYLUTE);
                output.accept(ModItems.SHATTERINGJUSTICE);
            })

            //registering blocks tab
            .build());    public static final Supplier<CreativeModeTab> ASSESSMENT_BLOCKS_TAB = CREATIVE_MODE_TAB.register("assessment_blocks_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModBlocks.SIMULACRUM_GROUND))
            .withTabsBefore(ResourceLocation.fromNamespaceAndPath(AssessmentMod.MODID, "assessment_items_tab"))
            //items tab first then blocks tab
            .title(Component.translatable("creativetab.assessmentmod.assessment_blocks"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModBlocks.SIMULACRUM_GROUND);
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
