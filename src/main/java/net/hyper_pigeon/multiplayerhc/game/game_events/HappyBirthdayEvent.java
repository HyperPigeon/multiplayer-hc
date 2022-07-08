package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class HappyBirthdayEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Happy Birthday!");
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
        ItemStack cakeStack = new ItemStack(Items.CAKE);
        game.gameSpace.getPlayers().stream().forEach(serverPlayerEntity -> {
            serverPlayerEntity.giveItemStack(cakeStack);
        });
    }
}
