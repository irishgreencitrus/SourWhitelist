package io.github.irishgreencitrus;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

public final class SourListener {
    private final SourServerState serverState;
    private final Logger logger;
    SourListener(Logger logger, SourServerState serverState) {
        this.logger = logger;
        this.serverState = serverState;
    }
    @Subscribe(order = PostOrder.EARLY)
    public void onPlayerLogin(LoginEvent event) {
        serverState.infoRegister(event.getPlayer());
        if (!serverState.whitelistIsEnabled()) return;
        if (!serverState.whitelistHas(event.getPlayer())) {
            event.getPlayer().disconnect(Component.text(serverState.settings.whitelistDisallowMessage));
        }
    }
    @Subscribe(order = PostOrder.EARLY)
    public void onPlayerDisconnect(DisconnectEvent event) {
        try {
            serverState.settingsSaveToFile();
            serverState.infoSaveToFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
