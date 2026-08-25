package amble.aos;

import amble.aos.core.AOSEntities;
import amble.aos.core.AOSItemGroups;
import amble.aos.core.AOSItems;
import amble.aos.core.AOSSounds;
import amble.aos.core.entities.CybermanBase;
import dev.amble.lib.container.RegistryContainer;
import dev.amble.lib.register.AmbleRegistries;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AOS implements ModInitializer {
	public static final String MOD_ID = "aos";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		RegistryContainer.register(AOSEntities.class, MOD_ID);
		RegistryContainer.register(AOSItemGroups.class, MOD_ID);
		RegistryContainer.register(AOSItems.class, MOD_ID);
		registerEntityAttributes();
		AOSSounds.init();
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	public void registerEntityAttributes() {
		FabricDefaultAttributeRegistry.register(AOSEntities.MONDASIAN_CYBERMAN, CybermanBase.createAttributes());
	}
}
