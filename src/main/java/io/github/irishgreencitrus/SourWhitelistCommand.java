package io.github.irishgreencitrus;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class SourWhitelistCommand implements SimpleCommand {
    private final ProxyServer server;
    private final SourServerState serverState;

    public SourWhitelistCommand(ProxyServer server, SourServerState serverState) {
        this.server = server;
        this.serverState = serverState;
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
            case "on" -> {
                serverState.whitelistEnable();
                source.sendMessage(Component.text("SourWhitelist Enabled"));
            }
            case "off" -> {
                serverState.whitelistDisable();
                source.sendMessage(Component.text("SourWhitelist Disabled"));
            }
            case "add" -> {
                if (args.length == 1) {
                    source.sendMessage(Component.text("Usage: /sourwhitelist add <players...>"));
                    return;
                }
                for (int i = 1; i < args.length; ++i) {
                    final String name = args[i];
                    Optional<Player> optionalPlayer = server.getPlayer(name);
                    if (optionalPlayer.isEmpty()) {
                        source.sendMessage(Component.text("Cannot find player '" + name + "'"));
                    } else {
                        Player player = optionalPlayer.get();
                        serverState.whitelistAdd(player);
                        source.sendMessage(Component.text("Added player '" + name + "' to whitelist"));
                    }
                }
            }
            case "remove" -> {
                if (args.length == 1) {
                    source.sendMessage(Component.text("Usage: /sourwhitelist remove <players...>"));
                    return;
                }
                for (int i = 1; i < args.length; ++i) {
                    final String name = args[i];
                    Optional<Player> optionalPlayer = server.getPlayer(name);
                    if (optionalPlayer.isEmpty()) {
                        source.sendMessage(Component.text("Cannot find player '" + name + "'"));
                    } else {
                        Player player = optionalPlayer.get();
                        serverState.whitelistRemove(player);
                    }
                }
            }
            case "list" -> {
                source.sendMessage(Component.text("Players on SourWhitelist:"));
                for (SourPlayer p : serverState.whitelistGetAllPermitted()) {
                    source.sendMessage(Component.text(p.knownNames.get(p.knownNames.size() - 1)));
                }
            }
            case "load" -> {
                try {
                    serverState.infoLoadFromFile();
                    serverState.settingsLoadFromFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                source.sendMessage(Component.text("Loaded 'whitelist.txt'"));
            }
            case "save" -> {
                try {
                    serverState.infoSaveToFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                source.sendMessage(Component.text("Saved 'whitelist.txt'"));
            }
            case "help" -> {
                source.sendMessage(
                    Component.text("""
                        SourWhitelist help.
                        \ton\tTurn on the whitelist
                        \toff\tTurn off the whitelist
                        \tadd <player>\tAdd a player to the whitelist
                        \tremove <player>\tRemove a player from the whitelist
                        \tlist\tList all players on the whitelist
                        \tload\tLoad the whitelist from whitelist.txt
                        \tsave\tSave the current whitelist to whitelist.txt
                        \thelp\tPrint this message"""
                    )
                );
            }
            default -> {
                source.sendMessage(Component.text("Subcommand is incorrect or incomplete"));
                source.sendMessage(Component.text("Usage: /sourwhitelist <subcommand> [args...]"));
            }
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
