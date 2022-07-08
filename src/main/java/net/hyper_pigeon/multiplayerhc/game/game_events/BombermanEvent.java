package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.text.Text;

public class BombermanEvent implements MultiplayerHcEvent{

    private final long duration = 1200;

    @Override
    public Text getName() {
        return Text.of("Bomberman");
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
        return duration;
    }

    @Override
    public void tickEvent(MultiplayerHcGame game) {
        game.gameSpace.getPlayers().stream().forEach(
                gamePlayer -> {
                    if(gamePlayer.isSneaking()) {
                        TntEntity tntEntity = EntityType.TNT.create(gamePlayer.getEntityWorld());
                        tntEntity.setFuse(60);
                        tntEntity.refreshPositionAndAngles(gamePlayer.getX() , gamePlayer.getY() , gamePlayer.getZ(), 0, 0);
                        gamePlayer.getEntityWorld().spawnEntity(tntEntity);
                    }
                }
        );
    }
}
