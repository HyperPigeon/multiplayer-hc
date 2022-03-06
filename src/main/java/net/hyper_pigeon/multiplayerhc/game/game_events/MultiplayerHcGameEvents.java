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

    public static final ArrayList<MultiplayerHcEvent> eventsList = new ArrayList<>(Arrays.asList(BLIND_IDIOT_GOD_EVENT,NO_ONE_CAN_ESCAPE_MY_SIGHT_EVENT,
            PHANTOM_MENACE_EVENT,SPEED_DEMONS_EVENT,
            SWITCH_EVENT,TOTAL_MAYHEM_EVENT,WRATH_OF_MEKHANE_EVENT,
            HARPOON_EVENT,
            SOCIAL_DISTANCING_EVENT));
//    public static final ArrayList<MultiplayerHcEvent> eventsList = new ArrayList<>(Arrays.asList(WRATH_OF_MEKHANE_EVENT));


}
