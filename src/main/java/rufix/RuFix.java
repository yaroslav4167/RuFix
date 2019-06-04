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
    private static final String RUSSIAN_LAYOUT =
            "ёЁ\"№;:?/йцукенгшщзхъЙЦУКЕНГШЩЗХЪфывапролджэФЫВАПРОЛДЖЭячсмитьбю.ЯЧСМИТЬБЮ,іІїЇ";

    private static final String ENGLISH_LAYOUT =
            "`~@#$^&|qwertyuiop[]QWERTYUIOP{}asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?sS]}";

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
        String convertedCommand = convert(event.getCommand());
        String convertedArguments = convert(event.getArguments());

        if (event.getCommand().equals(convertedCommand)) return;

        CommandSource src = event.getCause().first(CommandSource.class).orElse(null);
        if (src == null || !alias.contains(convertedCommand)) return;

        String command = convertedCommand + " " + convertedArguments;
        Sponge.getCommandManager().process(src, command);

        event.setCancelled(true);
    }

    private String convert(String str) {
        String s = str;

        for (int i = 0; i < RUSSIAN_LAYOUT.length(); i++) {
            s = s.replace(RUSSIAN_LAYOUT.charAt(i), ENGLISH_LAYOUT.charAt(i));
        }

        return s;
    }
}

