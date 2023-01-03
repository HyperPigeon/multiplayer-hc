package net.hyper_pigeon.multiplayerhc.game;

import net.hyper_pigeon.multiplayerhc.game.game_events.MultiplayerHcEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.util.ArrayList;

//handles random events in multiplayer hardcore
public class MultiplayerHcEventManager {


    public final ArrayList<Pair<MultiplayerHcEvent,Long>> events;

    public MultiplayerHcEventManager(){
        events = new ArrayList<>();
    }

    public void tickMultiplayerHcEventManager(MultiplayerHcGame game){
        ArrayList<Pair<MultiplayerHcEvent,Long>> removeTheseEvents = new ArrayList<>();
        for(Pair<MultiplayerHcEvent, Long> event : events){
            if(event.getRight() < game.world.getTime()){
                removeTheseEvents.add(event);
                if(event.getLeft().getDuration() > 0) {
                    game.gameSpace.getPlayers().sendMessage(Text.of(event.getLeft().getName().getString() + " has ended!"));
                }
            }
            else {
                event.getLeft().tickEvent(game);
            }
        }
        events.removeAll(removeTheseEvents);
    }

    public void onEntitySpawn(MultiplayerHcGame game, Entity entity) {
        for(Pair<MultiplayerHcEvent, Long> event : events){
            event.getLeft().onEntitySpawn(entity);
        }

    }

    public void onEntityDeath(MultiplayerHcGame game, LivingEntity livingEntity, DamageSource source) {
        for(Pair<MultiplayerHcEvent, Long> event : events){
            event.getLeft().onEntityDeath(livingEntity, source);
        }
    }

    public void onDamage(MultiplayerHcGame game, LivingEntity livingEntity, DamageSource source, float v){
        for(Pair<MultiplayerHcEvent, Long> event : events){
            event.getLeft().onDamage(livingEntity, source, v);
        }
    }

    public void addEvent(MultiplayerHcGame game, MultiplayerHcEvent event, ServerWorld world){
        long time = world.getTime()+event.getDuration();
        if(!containsEvent(event)){
            game.gameSpace.getPlayers().sendMessage(Text.of("EVENT: " + event.getName().getString()));
            events.add(new Pair<MultiplayerHcEvent,Long>(event,time));
            event.startEvent(game);
        }
    }

    public boolean isEmpty(){
        return events.isEmpty();
    }

    public boolean containsEvent(MultiplayerHcEvent event){
        for (Pair<MultiplayerHcEvent, Long> pair : events){
            if(pair.getLeft().equals(event)){
                return true;
            }
        }
        return false;
    }


}
