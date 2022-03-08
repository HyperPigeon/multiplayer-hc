package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import xyz.nucleoid.plasmid.game.GameSpace;

import java.util.Random;

public interface MultiplayerHcEvent {
    Random random = new Random();
    public Text getName();
    public boolean isPermanent();
    public void setPermanent(boolean isPer);
    public long getDuration();
    default void startEvent(MultiplayerHcGame game){}
    default void tickEvent(MultiplayerHcGame game){}
    default void onEntitySpawn(Entity entity){}
    default void onEntityDeath(LivingEntity livingEntity, DamageSource source){}
    default void onDamage(LivingEntity livingEntity, DamageSource source, float v) {}
}
