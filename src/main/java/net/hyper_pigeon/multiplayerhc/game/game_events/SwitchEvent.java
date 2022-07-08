package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.server.ServerTask;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwitchEvent implements MultiplayerHcEvent{
    private boolean isPermanent = false;
    private final long duration = 2400;
    private static final long COOLDOWN = 400;
    private long nextUseTime = 0;

    @Override
    public Text getName() {
        return Text.of("Let's switch things up.");
    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    @Override
    public void setPermanent(boolean isPer) {
        isPermanent = isPer;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public boolean isAbilityOffCooldown(MultiplayerHcGame game){
        return game.world.getTime() >= nextUseTime;
    }

    @Override
    public void tickEvent(MultiplayerHcGame game) {

        if(game.gameSpace.getPlayers().size() > 2 && isAbilityOffCooldown(game)){
            List<ServerPlayerEntity> swapList = pickNRandom(game.gameSpace.getPlayers().stream().toList(),2);

            ServerPlayerEntity player1 = swapList.get(0);
            ServerPlayerEntity player2 = swapList.get(1);

            if(player1.getEntityWorld().getDimension() == player2.getEntityWorld().getDimension()){
                BlockPos blockPos1 = player1.getBlockPos();
                BlockPos blockPos2 = player2.getBlockPos();


                if(player1.hasVehicle()){
                    player1.requestTeleportAndDismount(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
                }
                else {
                    player1.requestTeleport(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
                }

                if(player2.hasVehicle()){
                    player2.requestTeleportAndDismount(blockPos1.getX(),blockPos1.getY(),blockPos1.getZ());
                }
                else {
                    player2.requestTeleport(blockPos1.getX(),blockPos1.getY(),blockPos1.getZ());
                }

            }

            nextUseTime = game.world.getTime() + COOLDOWN;
        }

    }

    public static List<ServerPlayerEntity> pickNRandom(List<ServerPlayerEntity> lst, int n) {
        List<ServerPlayerEntity> copy = new ArrayList<ServerPlayerEntity>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }
}
