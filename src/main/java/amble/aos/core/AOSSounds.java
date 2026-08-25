package amble.aos.core;

import amble.aos.AOS;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.List;

public class AOSSounds {
    public static final SoundEvent MONDAS_STOMP = register("entity/mondas_stomp");
    public static final SoundEvent MONDAS_STOMP1 = register("entity/mondas_stomp1");
    public static final SoundEvent MONDAS_STOMP2 = register("entity/mondas_stomp2");
    public static final SoundEvent MONDAS_STOMP3 = register("entity/mondas_stomp3");
    public static final SoundEvent MONDAS_STOMP4 = register("entity/mondas_stomp4");

    public static final SoundEvent SHOOT = register("entity/shoot");

    public static void init() {
    }

    private static SoundEvent register(String name) {
        return register(AOS.id(name));
    }

    private static SoundEvent register(ResourceLocation id) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static List<SoundEvent> getSounds(String modid) {
        return BuiltInRegistries.SOUND_EVENT.stream()
                .filter(sound -> sound.getLocation().getNamespace().equals(modid))
                .toList();
    }
}