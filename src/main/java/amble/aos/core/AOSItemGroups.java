package amble.aos.core;

import amble.aos.AOS;
import dev.amble.lib.container.impl.ItemGroupContainer;
import dev.amble.lib.itemgroup.AItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class AOSItemGroups implements ItemGroupContainer {
    public static final AItemGroup MAIN = AItemGroup.builder(AOS.id("main"))
            .displayName(Component.translatable("itemGroup.aos.main"))
            .icon(() -> new ItemStack(AOSItems.MONDASIAN_CYBERMAN_SPAWN_EGG))
            .build();
}
