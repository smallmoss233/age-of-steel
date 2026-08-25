package amble.aos.core;

import amble.aos.core.entities.LaserBolt;
import amble.aos.core.entities.MondasianCyberman;
import dev.amble.lib.container.impl.EntityContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class AOSEntities implements EntityContainer {
    public static EntityType<MondasianCyberman> MONDASIAN_CYBERMAN = EntityType.Builder
            .of(MondasianCyberman::new, MobCategory.MONSTER)
            .sized(0.7F, 2.3F)
            .fireImmune()
            .build("mondasian_cyberman");

    public static EntityType<LaserBolt> LASER_BOLT = EntityType.Builder
            .<LaserBolt>of(LaserBolt::new, MobCategory.MISC)
            .sized(0.3F, 0.3F)
            .clientTrackingRange(4)
            .updateInterval(10)
            .fireImmune()
            .build("laser_bolt");
}
