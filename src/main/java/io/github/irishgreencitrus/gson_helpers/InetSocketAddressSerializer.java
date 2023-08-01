package io.github.irishgreencitrus.gson_helpers;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;

public class InetSocketAddressSerializer implements JsonSerializer<InetSocketAddress> {
    @Override
    public JsonElement serialize(InetSocketAddress src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray a = new JsonArray();
        a.add(src.getHostString());
        a.add(src.getPort());
        return a;
    }
}
