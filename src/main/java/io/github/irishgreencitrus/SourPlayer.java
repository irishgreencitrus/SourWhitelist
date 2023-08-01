package io.github.irishgreencitrus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

public class SourPlayer {
    public ArrayList<String> knownNames = new ArrayList<>();
    public HashSet<InetSocketAddress> knownIPs = new HashSet<>();
    public boolean permitted = false;
    public boolean banned = false;

    @Nullable
    public LocalDateTime timedOutUntil = null;
    public LocalDateTime lastLogin = LocalDateTime.now();


}
