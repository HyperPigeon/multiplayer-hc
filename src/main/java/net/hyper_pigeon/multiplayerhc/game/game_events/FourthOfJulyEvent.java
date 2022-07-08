package net.hyper_pigeon.multiplayerhc.game.game_events;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.Text;

public class FourthOfJulyEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Fourth of July!");
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
        game.gameSpace.getPlayers().stream().forEach(gamePlayer ->{
            ItemStack fireworkStack1 = new ItemStack(Items.FIREWORK_ROCKET, 4);
            ItemStack fireworkStack2 = new ItemStack(Items.FIREWORK_ROCKET, 4);
            ItemStack fireworkStack3 = new ItemStack(Items.FIREWORK_ROCKET, 4);

            try {
                fireworkStack1.setSubNbt("Fireworks", StringNbtReader.parse("{Explosions:[{Type:1b,Flicker:1,Colors:[I;11743532,11743532,11743532],FadeColors:[I;5320730]}],Flight:3b}"));
                fireworkStack2.setSubNbt("Fireworks", StringNbtReader.parse("{Explosions:[{Type:1b,Flicker:1,Colors:[I;15790320,15790320,15790320],FadeColors:[I;15790320]}],Flight:3b}"));
                fireworkStack3.setSubNbt("Fireworks", StringNbtReader.parse("{Explosions:[{Type:1b,Flicker:1,Colors:[I;2437522,2437522,2437522],FadeColors:[I;2437522]}],Flight:3b}"));
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        });
    }
}
