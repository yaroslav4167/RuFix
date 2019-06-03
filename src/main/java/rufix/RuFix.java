package rufix;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

import java.util.ArrayList;

import java.util.List;

@Plugin(id = "rufix", name = "RuFix", version = "1.0", description = "ruFix")
public class RuFix {
    private List<String> alias = new ArrayList<>();

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        alias.clear();
        for (CommandMapping commandMapping : Sponge.getCommandManager().getCommands()) {
            alias.add(commandMapping.getPrimaryAlias());
            alias.addAll(commandMapping.getAllAliases());
        }
    }

    @Listener(order = Order.LAST)
    public void onCommand(SendCommandEvent event) {
        String tCom = convert(event.getCommand());
        if(event.getCommand().equals(tCom))return;
        CommandSource src = event.getCause().first(CommandSource.class).orElse(null);
        if(src==null||!alias.contains(convert(event.getCommand())))return;
        Sponge.getCommandManager().process(src, tCom + " " +convert(event.getArguments()));
        event.setCancelled(true);
    }

    private String convert(String str){
        String s = str;
        String out = "`~@#$^&|qwertyuiop[]QWERTYUIOP{}asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?sS]}";
        String in = "ёЁ\"№;:?/йцукенгшщзхъЙЦУКЕНГШЩЗХЪфывапролджэФЫВАПРОЛДЖЭячсмитьбю.ЯЧСМИТЬБЮ,іІїЇ";
        for (int i = 0; i < in.length(); i++)
            s = s.replace(in.charAt(i), out.charAt(i));
        return s;
    }
}

