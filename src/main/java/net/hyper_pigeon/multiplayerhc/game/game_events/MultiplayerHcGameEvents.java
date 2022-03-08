package net.hyper_pigeon.multiplayerhc.game.game_events;

import java.util.ArrayList;
import java.util.Arrays;

public class MultiplayerHcGameEvents {

    public static final BlindIdiotGodEvent BLIND_IDIOT_GOD_EVENT = new BlindIdiotGodEvent();
    public static final NoOneCanEscapeMySightEvent NO_ONE_CAN_ESCAPE_MY_SIGHT_EVENT = new NoOneCanEscapeMySightEvent();
    public static final PhantomMenaceEvent PHANTOM_MENACE_EVENT = new PhantomMenaceEvent();
    public static final SpeedDemonsEvent SPEED_DEMONS_EVENT = new SpeedDemonsEvent();
    public static final SwitchEvent SWITCH_EVENT = new SwitchEvent();
    public static final TotalMayhemEvent TOTAL_MAYHEM_EVENT = new TotalMayhemEvent();
    public static final WrathOfMekhaneEvent WRATH_OF_MEKHANE_EVENT = new WrathOfMekhaneEvent();
    public static final HarpoonEvent HARPOON_EVENT = new HarpoonEvent();
    public static final SocialDistancingEvent SOCIAL_DISTANCING_EVENT = new SocialDistancingEvent();
    public static final GiddyUpEvent GIDDY_UP_EVENT = new GiddyUpEvent();
    public static final ClippedWingsEvent CLIPPED_WINGS_EVENT = new ClippedWingsEvent();
    public static final BladeWolfEvent BLADE_WOLF_EVENT = new BladeWolfEvent();
    public static final ComboStarterEvent COMBO_STARTER_EVENT = new ComboStarterEvent();
    public static final LifestealEvent LIFESTEAL_EVENT = new LifestealEvent();
    public static final MoonGravityEvent MOON_GRAVITY_EVENT = new MoonGravityEvent();
    public static final TaxationWithoutRepresentationEvent TAXATION_WITHOUT_REPRESENTATION_EVENT = new TaxationWithoutRepresentationEvent();
    public static final PoisonedArrowsEvent POISONED_ARROWS_EVENT = new PoisonedArrowsEvent();
    public static final HealingArrowsEvent HEALING_ARROWS_EVENT = new HealingArrowsEvent();
    public static final DamageArrowsEvent DAMAGE_ARROWS_EVENT = new DamageArrowsEvent();
    public static final UnbrokenEvent UNBROKEN_EVENT = new UnbrokenEvent();
    public static final DragonBroEvent DRAGON_BRO_EVENT = new DragonBroEvent();

    public static final ArrayList<MultiplayerHcEvent> eventsList = new ArrayList<>(Arrays.asList(
            BLIND_IDIOT_GOD_EVENT,
            NO_ONE_CAN_ESCAPE_MY_SIGHT_EVENT,
            PHANTOM_MENACE_EVENT,
            SPEED_DEMONS_EVENT,
            SWITCH_EVENT,
            TOTAL_MAYHEM_EVENT,
            WRATH_OF_MEKHANE_EVENT,
            HARPOON_EVENT,
            SOCIAL_DISTANCING_EVENT,
            GIDDY_UP_EVENT,
            CLIPPED_WINGS_EVENT,
            BLADE_WOLF_EVENT,
            COMBO_STARTER_EVENT,
            LIFESTEAL_EVENT,
            MOON_GRAVITY_EVENT,
            TAXATION_WITHOUT_REPRESENTATION_EVENT,
            POISONED_ARROWS_EVENT,
            DAMAGE_ARROWS_EVENT,
            HEALING_ARROWS_EVENT,
            UNBROKEN_EVENT,
            DRAGON_BRO_EVENT));
//    public static final ArrayList<MultiplayerHcEvent> eventsList = new ArrayList<>(Arrays.asList(WRATH_OF_MEKHANE_EVENT));


}
