package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.text.Text;

public class FreeHealthcareEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Free Healthcare!");
    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    @Override
    public void setPermanent(boolean isPer) {

    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public void startEvent(MultiplayerHcGame game) {
        game.gameSpace.getPlayers().stream().forEach(gamePlayer ->
        {
            gamePlayer.heal(20.0f);
        });
    }
}
