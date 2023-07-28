package io.github.irishgreencitrus;

import com.google.common.collect.Sets;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.UuidUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public final class Whitelist {
    private HashSet<UUID> allowedPlayerUUIDs = Sets.newHashSet();
    private boolean enabled = true;
    private Path dataDirectory;
    private File whitelistFile;

    public Whitelist(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
        whitelistFile = new File(dataDirectory.toFile(), "whitelist.txt");
        try {
            createWhitelist();
            loadWhitelist();
            saveWhitelist();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPlayer(UUID uuid) {
        allowedPlayerUUIDs.add(uuid);
    }
    public void addPlayer(Player player) {
        allowedPlayerUUIDs.add(player.getUniqueId());
    }

    public void removePlayer(UUID uuid) {
        allowedPlayerUUIDs.remove(uuid);
    }

    public void removePlayer(Player player) {
        allowedPlayerUUIDs.remove(player.getUniqueId());
    }

    public Set<UUID> getAllowedPlayers() {
        return allowedPlayerUUIDs;
    }

    public boolean contains(UUID uuid) {
        return allowedPlayerUUIDs.contains(uuid);
    }
    public boolean contains(Player player) {
        return allowedPlayerUUIDs.contains(player.getUniqueId());
    }

    public void enable() {
        enabled = true;
    }
    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void loadWhitelist() {
        try(BufferedReader br = new BufferedReader(new FileReader(whitelistFile))) {
            String line = br.readLine();
            while (line != null) {
                System.out.println(line);
                Pattern UUID_REGEX =
                        Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
                if (UUID_REGEX.matcher(line.strip()).matches()) {
                    UUID u = UUID.fromString(line.strip());
                    allowedPlayerUUIDs.add(u);
                } else {
                    System.out.println("WARN: '"+line.strip()+"' is not a valid uuid");
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void createWhitelist() throws IOException {
        dataDirectory.toFile().mkdirs();
        whitelistFile.createNewFile();
    }

    public void saveWhitelist() throws IOException {
        FileWriter w = new FileWriter(whitelistFile);
        for (UUID u: allowedPlayerUUIDs) {
            w.write(u.toString()+"\n");
        }
        w.close();
    }
}
