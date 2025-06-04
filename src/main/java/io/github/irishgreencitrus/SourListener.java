package io.github.irishgreencitrus;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

public final class SourListener {
    private final SourServerState serverState;

    SourListener(Logger logger, SourServerState serverState) {
        this.serverState = serverState;
    }

    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        serverState.infoRegister(event.getPlayer());
        if (!serverState.whitelistIsEnabled()) return;
        if (!serverState.whitelistHas(event.getPlayer())) {
            event.getPlayer().disconnect(Component.text(serverState.settings.whitelistDisallowMessage));
        }
        if (serverState.getPlayer(event.getPlayer()).orElseThrow().banned) {
            event.getPlayer().disconnect(Component.text(serverState.settings.bannedMessage));
        }
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        try {
            serverState.settingsSaveToFile();
            serverState.infoSaveToFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
