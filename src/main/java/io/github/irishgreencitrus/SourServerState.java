package io.github.irishgreencitrus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.velocitypowered.api.proxy.Player;
import io.github.irishgreencitrus.gson_helpers.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

public class SourServerState {
    public static final String PLAYERINFO_FILENAME = "playerinfo.json";
    public static final String SETTINGS_FILENAME = "settings.json";
    public SourSettings settings = new SourSettings();
    public HashMap<UUID, SourPlayer> playerInfo = new HashMap<>();

    private Path dataDirectory;
    private File playerInfoFile;
    private File settingsFile;

    SourServerState(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
        playerInfoFile = new File(dataDirectory.toFile(), PLAYERINFO_FILENAME);
        settingsFile = new File(dataDirectory.toFile(), SETTINGS_FILENAME);
        try {
            if (filesExist()) {
                infoLoadFromFile();
                settingsLoadFromFile();
            } else {
                createFiles();
                infoSaveToFile();
                settingsSaveToFile();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean whitelistIsEnabled() {
        return settings.whitelistEnabled;
    }

    public void whitelistEnable() {
        settings.whitelistEnabled = true;
    }

    public void whitelistDisable() {
        settings.whitelistEnabled = false;
    }

    public boolean whitelistHas(Player player) {
        return whitelistHas(player.getUniqueId());
    }

    public boolean whitelistHas(UUID uuid) {
        if (!playerInfo.containsKey(uuid)) {
            return false;
        }
        return playerInfo.get(uuid).permitted;
    }

    public boolean whitelistAdd(Player player) {
        return whitelistAdd(player.getUniqueId());
    }
    public boolean whitelistAdd(UUID uuid) {
        Optional<SourPlayer> sourPlayerOptional = getPlayer(uuid);
        if (sourPlayerOptional.isEmpty()) {
            return false;
        }
        SourPlayer sourPlayer = sourPlayerOptional.get();
        sourPlayer.permitted = true;
        return true;
    }

    public boolean whitelistRemove(Player player) {
        return whitelistRemove(player.getUniqueId());
    }

    public boolean whitelistRemove(UUID uuid) {
        Optional<SourPlayer> sourPlayerOptional = getPlayer(uuid);
        if (sourPlayerOptional.isEmpty()) {
            return false;
        }
        SourPlayer sourPlayer = sourPlayerOptional.get();
        sourPlayer.permitted = false;
        return true;
    }

    public List<SourPlayer> whitelistGetAllPermitted() {
        ArrayList<SourPlayer> sourPlayerArrayList = new ArrayList<>();
        for (SourPlayer sp: playerInfo.values()) {
            if (sp.permitted) sourPlayerArrayList.add(sp);
        }
        return sourPlayerArrayList;
    }

    @NotNull
    public Optional<SourPlayer> getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    @NotNull
    public Optional<SourPlayer> getPlayer(UUID uuid) {
        if (!playerInfo.containsKey(uuid)) {
            return Optional.empty();
        } else {
            return Optional.of(playerInfo.get(uuid));
        }
    }

    public boolean timeoutsEnabled() {
        return settings.timeoutsEnabled;
    }

    public void enableTimeouts() {
        settings.timeoutsEnabled = true;
    }

    public void disableTimeouts() {
        settings.timeoutsEnabled = false;
    }

    public void infoRegister(@NotNull Player player) {
        if (playerInfo.containsKey(player.getUniqueId())) {
            SourPlayer player1 = playerInfo.get(player.getUniqueId());
            player1.knownIPs.add(player.getRemoteAddress());
            if (!Objects.equals(
                    player1.knownNames.get(player1.knownNames.size() - 1),
                    player.getUsername()))
            {
                player1.knownNames.add(player.getUsername());
            }
            player1.lastLogin = LocalDateTime.now();
        } else {
            SourPlayer player1 = new SourPlayer();
            player1.knownIPs.add(player.getRemoteAddress());
            player1.knownNames.add(player.getUsername());
            player1.lastLogin = LocalDateTime.now();

            playerInfo.put(
                    player.getUniqueId(),
                    player1
            );
        }
    }
    public boolean infoIsRegistered(@NotNull Player player) {
        return infoIsRegistered(player.getUniqueId());
    }
    public boolean infoIsRegistered(@NotNull UUID uuid) {
        return playerInfo.containsKey(uuid);
    }

    public void infoSaveToFile() throws IOException {
        Gson g = getCustomGson();
        String info = g.toJson(playerInfo);
        FileWriter w = new FileWriter(playerInfoFile);
        w.write(info);
        w.close();
    }
    public void infoLoadFromFile() throws IOException {
        Gson g = getCustomGson();
        FileReader reader = new FileReader(playerInfoFile);
        TypeToken<HashMap<UUID, SourPlayer>> token = new TypeToken<>(){};
        playerInfo = g.fromJson(reader, token);
        reader.close();
    }

    public void settingsSaveToFile() throws IOException {
        Gson g = getCustomGson();
        String info = g.toJson(settings);
        FileWriter w = new FileWriter(settingsFile);
        w.write(info);
        w.close();
    }

    public void settingsLoadFromFile() throws IOException {
        Gson g = getCustomGson();
        FileReader reader = new FileReader(settingsFile);
        settings = g.fromJson(reader, SourSettings.class);
        reader.close();
    }

    public void createFiles() throws IOException {
        dataDirectory.toFile().mkdirs();
        settingsFile.createNewFile();
        playerInfoFile.createNewFile();
    }
    public boolean filesExist() {
        return settingsFile.exists() && playerInfoFile.exists();
    }

    private Gson getCustomGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeInstanceCreator());
        gsonBuilder.registerTypeAdapter(InetSocketAddress.class, new InetSocketAddressDeserializer());
        gsonBuilder.registerTypeAdapter(InetSocketAddress.class, new InetSocketAddressSerializer());
        gsonBuilder.registerTypeAdapter(InetSocketAddress.class, new InetSocketAddressInstanceCreator());
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create();
    }

}
