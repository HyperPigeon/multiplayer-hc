package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class HarpoonEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("HARPOOOOOON!");
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

    public void startEvent(MultiplayerHcGame game){
        game.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
            ItemStack tridentItem = new ItemStack(Items.TRIDENT);
            tridentItem.setDamage(249);
            tridentItem.addEnchantment(Enchantments.CHANNELING,3);
            tridentItem.addEnchantment(Enchantments.IMPALING, 3);
            tridentItem.addEnchantment(Enchantments.BINDING_CURSE,1);
            gamePlayer.giveItemStack(tridentItem);
            gamePlayer.giveItemStack(tridentItem);
            gamePlayer.giveItemStack(tridentItem);
            gamePlayer.giveItemStack(tridentItem);
            gamePlayer.giveItemStack(tridentItem);
        });
    }

}
