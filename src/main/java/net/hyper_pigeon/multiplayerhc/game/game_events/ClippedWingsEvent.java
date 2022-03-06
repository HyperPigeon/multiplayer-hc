package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class ClippedWingsEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Clipped Wings");
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
            ItemStack elytra = new ItemStack(Items.ELYTRA);
            elytra.setDamage(382);
            ItemStack fireworks = new ItemStack(Items.FIREWORK_ROCKET,8);
            gamePlayer.giveItemStack(elytra);
            gamePlayer.giveItemStack(fireworks);
        });
    }
}
