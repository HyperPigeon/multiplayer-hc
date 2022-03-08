package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class TaxationWithoutRepresentationEvent implements  MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Taxation without Representation");
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
          if(gamePlayer.getInventory().contains(new ItemStack(Items.DIAMOND))){
              gamePlayer.getInventory().removeOne(new ItemStack(Items.DIAMOND));
          }
        });
    }
}
