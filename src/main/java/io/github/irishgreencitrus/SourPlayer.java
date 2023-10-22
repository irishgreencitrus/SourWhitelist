package io.github.irishgreencitrus;

import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class SourPlayer {
    public ArrayList<String> knownNames = new ArrayList<>();
    public HashSet<String> knownIPs = new HashSet<>();
    public boolean permitted = false;
    public boolean banned = false;

    public UUID uuid = new UUID(0x0,0x0);

    @Nullable
    public LocalDateTime timedOutUntil = null;
    public LocalDateTime lastLogin = LocalDateTime.now();


}
