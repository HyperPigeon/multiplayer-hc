package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;

public class NoOneCanEscapeMySightEvent implements MultiplayerHcEvent{

    public boolean isPermanent = false;

    @Override
    public Text getName() {
        return Text.of("No one can escape my Sight");
    }

    @Override
    public boolean isPermanent() {
        return isPermanent;
    }

    @Override
    public void setPermanent(boolean isPer) {
        isPermanent = isPer;
    }

    @Override
    public long getDuration() {
        return 6000;
    }

    @Override
    public void startEvent(MultiplayerHcGame game) {
        game.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
            gamePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING,6000,0));
        });
    }
}
