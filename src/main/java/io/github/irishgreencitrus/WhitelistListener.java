package io.github.irishgreencitrus;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.util.UUID;

public final class WhitelistListener {
    private Whitelist whitelist;
    private final Logger logger;
    WhitelistListener(Logger logger, Whitelist whitelist) {
        this.logger = logger;
        this.whitelist = whitelist;
    }
    @Subscribe(order = PostOrder.EARLY)
    public void onPlayerLogin(LoginEvent event) {
        if (!whitelist.isEnabled()) return;
        if (!whitelist.contains(event.getPlayer())) {
            event.getPlayer().disconnect(Component.text("You are not on the whitelist!"));
        }
    }
}
