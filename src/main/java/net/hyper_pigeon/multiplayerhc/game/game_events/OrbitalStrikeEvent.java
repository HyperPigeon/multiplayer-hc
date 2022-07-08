package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrbitalStrikeEvent implements MultiplayerHcEvent{

    public long strikeTime;
    public final long duration = 1200;

    @Override
    public Text getName() {
        return Text.of("Orbital Strike");
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
    public void startEvent(MultiplayerHcGame game) {
        strikeTime = game.world.getTime() + getDuration();
        game.gameSpace.getPlayers().sendMessage(Text.of("Orbital strike incoming in 1 minute!"));
    }

    @Override
    public void tickEvent(MultiplayerHcGame game) {
        if (game.world.getTime() > strikeTime){

            ServerPlayerEntity randomPlayer = pickNRandom(game.gameSpace.getPlayers().stream().toList(),1).get(0);
            BlockPos blockPos = randomPlayer.getBlockPos();

            game.gameSpace.getPlayers().sendMessage(Text.of("Incoming orbital strike targeting " + randomPlayer.getName().toString() + "!"));

            FireballEntity fireballEntity = new FireballEntity(randomPlayer.getEntityWorld(), randomPlayer, 0, 15,0,10);
            fireballEntity.updatePosition(blockPos.getX(),blockPos.getY()+50,blockPos.getZ());
            randomPlayer.getEntityWorld().spawnEntity(fireballEntity);

        }
    }

    public static List<ServerPlayerEntity> pickNRandom(List<ServerPlayerEntity> lst, int n) {
        List<ServerPlayerEntity> copy = new ArrayList<ServerPlayerEntity>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }
}
