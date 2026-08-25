package amble.aos.core;

import dev.amble.lib.container.impl.ItemContainer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public class AOSItems extends ItemContainer {
    public static final Item MONDASIAN_CYBERMAN_SPAWN_EGG =
            new SpawnEggItem(AOSEntities.MONDASIAN_CYBERMAN, 0xBFC1C2, 0x3A3A3A, new Item.Properties());

    @Override
    public CreativeModeTab getDefaultGroup() {
        return AOSItemGroups.MAIN;
    }
}
