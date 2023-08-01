package io.github.irishgreencitrus.gson_helpers;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;

public class InetSocketAddressInstanceCreator implements InstanceCreator<InetSocketAddress> {
    @Override
    public InetSocketAddress createInstance(Type type) {
        return new InetSocketAddress(0);
    }
}
