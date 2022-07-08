package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DontDoItEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Don't do it.");
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
        ServerPlayerEntity randomPlayer = pickNRandom(game.gameSpace.getPlayers().stream().toList(), 1).get(0);
        ItemStack witherSkeleSkullsStack = new ItemStack(Items.WITHER_SKELETON_SKULL,3);
        ItemStack soulSandStack = new ItemStack(Items.SOUL_SAND, 4);
        randomPlayer.giveItemStack(witherSkeleSkullsStack);
        randomPlayer.giveItemStack(soulSandStack);
    }

    public static List<ServerPlayerEntity> pickNRandom(List<ServerPlayerEntity> lst, int n) {
        List<ServerPlayerEntity> copy = new ArrayList<ServerPlayerEntity>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }
}
