package io.github.irishgreencitrus.gson_helpers;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;

public class InetSocketAddressDeserializer implements JsonDeserializer<InetSocketAddress> {
    @Override
    public InetSocketAddress deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray a = json.getAsJsonArray();
        String host = a.get(0).getAsString();
        int port = a.get(1).getAsInt();
        return new InetSocketAddress(host,port);
    }
}
