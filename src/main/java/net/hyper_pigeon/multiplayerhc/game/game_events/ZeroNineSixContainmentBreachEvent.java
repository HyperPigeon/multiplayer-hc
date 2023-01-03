package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.entity.SCPZeroNineSixEntity;
import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.hyper_pigeon.multiplayerhc.registry.MultiplayerHcEntities;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ZeroNineSixContainmentBreachEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("SCP-096 has breached containment!");
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
        List<ServerPlayerEntity> randomPlayerList = pickNRandom(game.gameSpace.getPlayers().stream().toList(),1);
        ServerPlayerEntity randomPlayer = randomPlayerList.get(0);

        BlockPos blockPos = new BlockPos(randomPlayer.getX() + MathHelper.nextDouble(Random.create(),-20,20),randomPlayer.getY(),randomPlayer.getZ()+ MathHelper.nextDouble(Random.create(),-20,20));
        SCPZeroNineSixEntity scpZeroNineSixEntity = MultiplayerHcEntities.SCP_ZERO_NINE_SIX_ENTITY.create(randomPlayer.getEntityWorld());
        scpZeroNineSixEntity.refreshPositionAndAngles(blockPos,0,0);
        randomPlayer.getEntityWorld().spawnEntity(scpZeroNineSixEntity);
    }

    public static List<ServerPlayerEntity> pickNRandom(List<ServerPlayerEntity> lst, int n) {
        List<ServerPlayerEntity> copy = new ArrayList<ServerPlayerEntity>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }
}
