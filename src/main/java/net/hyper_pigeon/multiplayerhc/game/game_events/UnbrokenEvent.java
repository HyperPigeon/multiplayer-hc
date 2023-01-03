package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class UnbrokenEvent implements MultiplayerHcEvent {
    @Override
    public Text getName() {
        return Text.of("Auto Repair");
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
            ItemStack HELMET = gamePlayer.getEquippedStack(EquipmentSlot.HEAD);
            ItemStack CHESTPLATE = gamePlayer.getEquippedStack(EquipmentSlot.CHEST);
            ItemStack LEGGINGS = gamePlayer.getEquippedStack(EquipmentSlot.LEGS);
            ItemStack FEET = gamePlayer.getEquippedStack(EquipmentSlot.FEET);

            if(HELMET != null){
                HELMET.setDamage(0);
            }
            if(CHESTPLATE != null){
                CHESTPLATE.setDamage(0);
            }
            if(LEGGINGS != null){
                LEGGINGS.setDamage(0);
            }
            if(FEET != null){
                FEET.setDamage(0);
            }
        });
    }
}
