package io.github.irishgreencitrus;

import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

public class SourPlayer {
    public ArrayList<String> knownNames = new ArrayList<>();
    public HashSet<String> knownIPs = new HashSet<>();
    public boolean permitted = false;
    public boolean banned = false;

    @Nullable
    public LocalDateTime timedOutUntil = null;
    public LocalDateTime lastLogin = LocalDateTime.now();


}
