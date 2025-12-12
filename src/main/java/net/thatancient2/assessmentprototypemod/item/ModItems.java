package net.thatancient2.assessmentprototypemod.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thatancient2.assessmentprototypemod.AssessmentMod;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AssessmentMod.MODID);

    public static final DeferredItem<Item> LUNARCOIN = ITEMS.register("lunarcoin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EGOCENTRISM = ITEMS.register("egocentrism", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> UKELELE = ITEMS.register("ukelele", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ATGMISSILEMKI = ITEMS.register("atgmissilemki", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NOXIOUSTHORN = ITEMS.register("noxiousthorn",() -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GESTUREOFTHEDROWNED = ITEMS.register("gestureofthedrowned", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POLYLUTE = ITEMS.register("polylute", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SHATTERINGJUSTICE = ITEMS.register("shatteringjustice", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> IRRADIANTPEARL = ITEMS.register("irradiantpearl", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLANULA = ITEMS.register("planula", () -> new Item(new Item.Properties()));


    public static void  register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
