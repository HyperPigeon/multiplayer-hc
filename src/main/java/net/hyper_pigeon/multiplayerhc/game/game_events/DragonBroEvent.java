package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragonBroEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return null;
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
        List<ServerPlayerEntity> dragonBro = pickNRandom(game.gameSpace.getPlayers().stream().toList(),1);
        ItemStack dragonHead = new ItemStack(Items.DRAGON_HEAD);
        dragonHead.addEnchantment(Enchantments.BINDING_CURSE,0);
        dragonBro.get(0).equipStack(EquipmentSlot.HEAD, dragonHead);
    }

    public static List<ServerPlayerEntity> pickNRandom(List<ServerPlayerEntity> lst, int n) {
        List<ServerPlayerEntity> copy = new ArrayList<ServerPlayerEntity>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }
}
