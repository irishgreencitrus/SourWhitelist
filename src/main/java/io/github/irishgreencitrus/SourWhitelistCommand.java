package io.github.irishgreencitrus;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class SourWhitelistCommand implements SimpleCommand {
    private ProxyServer server;
    private Whitelist whitelist;

    public SourWhitelistCommand(ProxyServer server, Whitelist whitelist) {
        this.server = server;
        this.whitelist = whitelist;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length == 0) {
            source.sendMessage(Component.text("A subcommand is required"));
            source.sendMessage(Component.text("Usage: /sourwhitelist <subcommand> [args...]"));
            return;
        }
        switch (args[0]) {
            case "on":
                whitelist.enable();
                source.sendMessage(Component.text("SourWhitelist Enabled"));
                break;
            case "off":
                whitelist.disable();
                source.sendMessage(Component.text("SourWhitelist Disabled"));
                break;
            case "add":
                if (args.length == 1) {
                    source.sendMessage(Component.text("Usage: /sourwhitelist add <players...>"));
                    return;
                }
                for (int i = 1; i < args.length; ++i) {
                    final String name = args[i];
                    Optional<Player> optionalPlayer = server.getPlayer(name);
                    if (optionalPlayer.isEmpty()) {
                        source.sendMessage(Component.text("Cannot find player '"+name+"'"));
                    } else {
                        Player player = optionalPlayer.get();
                        whitelist.addPlayer(player);
                    }
                }
                break;

            case "remove":
                if (args.length == 1) {
                    source.sendMessage(Component.text("Usage: /sourwhitelist remove <players...>"));
                    return;
                }
                for (int i = 1; i < args.length; ++i) {
                    final String name = args[i];
                    Optional<Player> optionalPlayer = server.getPlayer(name);
                    if (optionalPlayer.isEmpty()) {
                        source.sendMessage(Component.text("Cannot find player '"+name+"'"));
                    } else {
                        Player player = optionalPlayer.get();
                        whitelist.removePlayer(player);
                    }
                }
                break;
            case "list":
                source.sendMessage(Component.text("Players on SourWhitelist:"));
                for (UUID u: whitelist.getAllowedPlayers()) {
                    source.sendMessage(Component.text(u.toString()));
                }
                break;
            case "load":
                whitelist.loadWhitelist();
                source.sendMessage(Component.text("Reloaded 'whitelist.txt'"));
                break;
            case "save":
                try {
                    whitelist.saveWhitelist();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                source.sendMessage(Component.text("Reloaded 'whitelist.txt'"));
                break;
            default:
                source.sendMessage(Component.text("Subcommand is incorrect or incomplete"));
                source.sendMessage(Component.text("Usage: /sourwhitelist <subcommand> [args...]"));
                break;
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] currentArgs = invocation.arguments();
        if (currentArgs.length == 1) {
            return List.of("on", "off", "add", "remove", "list", "load","save");
        } else {
            return List.of();
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("sour.whitelist_command");
    }
}
