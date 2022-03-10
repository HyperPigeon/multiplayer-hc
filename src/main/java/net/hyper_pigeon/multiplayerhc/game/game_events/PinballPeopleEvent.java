package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class PinballPeopleEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Pinball People");
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
        return 3000;
    }

    @Override
    public void tickEvent(MultiplayerHcGame game) {
        game.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
            List<ServerPlayerEntity> players = gamePlayer.getEntityWorld().getEntitiesByClass(ServerPlayerEntity.class,gamePlayer.getBoundingBox(), playerEntity -> {return true;});
            if(players.size() >= 1){
                gamePlayer.setVelocity(players.get(0).getVelocity().multiply(7));
            }
        });
    }
}
