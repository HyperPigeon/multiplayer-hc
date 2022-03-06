package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class GiddyUpEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Giddy up!");
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
        game.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
           gamePlayer.giveItemStack(new ItemStack(Items.HORSE_SPAWN_EGG));
           gamePlayer.giveItemStack(new ItemStack(Items.SADDLE));
           gamePlayer.giveItemStack(new ItemStack((Items.HAY_BLOCK)));
        });
    }
}
