package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class BladeWolfEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Blade " + '"' + "Wolf" + '"');
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
            BlockPos blockPos = gamePlayer.getBlockPos();
            FoxEntity foxEntity = EntityType.FOX.create(gamePlayer.getWorld());

            assert foxEntity != null;
            foxEntity.refreshPositionAndAngles(blockPos,0,0);
            gamePlayer.getWorld().spawnEntity(foxEntity);

            ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
            sword.addEnchantment(Enchantments.SHARPNESS,5);
            foxEntity.equipStack(EquipmentSlot.MAINHAND,sword);

            foxEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,6000,2));
            foxEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,6000,2));
        });
    }
}
