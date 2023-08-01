package io.github.irishgreencitrus;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(id = "sourwhitelist", name = "Sour Whitelist", version = "0.1.0", authors = {"irishgreencitrus"})
public final class SourWhitelist {
    private SourServerState serverState;
    private final ProxyServer proxyServer;
    private final Logger logger;

    @Inject
    public SourWhitelist(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.serverState = new SourServerState(dataDirectory);

    }

    @Subscribe
    public void onProxyInit(ProxyInitializeEvent event) {
        proxyServer.getEventManager().register(this, new SourListener(this.logger, this.serverState));
        CommandManager commandManager = proxyServer.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder("sourwhitelist")
                .aliases("swhitelist", "slist")
                .plugin(this).build();
        SimpleCommand command = new SourWhitelistCommand(this.proxyServer,this.serverState);
        commandManager.register(commandMeta, command);

    }
    @Subscribe
    public void onProxyClose(ProxyShutdownEvent event) {
        try {
            serverState.infoSaveToFile();
            serverState.settingsSaveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}