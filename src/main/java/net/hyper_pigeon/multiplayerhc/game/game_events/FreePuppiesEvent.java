package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class FreePuppiesEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Free Puppies!");
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
        game.gameSpace.getPlayers().stream().forEach(
                gamePlayer -> {
                    ItemStack wolfEggs = new ItemStack(Items.WOLF_SPAWN_EGG, 3);
                    ItemStack bones = new ItemStack(Items.BONE, 32);
                    gamePlayer.giveItemStack(wolfEggs);
                    gamePlayer.giveItemStack(bones);
                }
        );
    }
}
