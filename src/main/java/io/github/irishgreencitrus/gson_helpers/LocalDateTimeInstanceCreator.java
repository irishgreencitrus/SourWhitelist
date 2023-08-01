package io.github.irishgreencitrus.gson_helpers;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class LocalDateTimeInstanceCreator implements InstanceCreator<LocalDateTime> {
    @Override
    public LocalDateTime createInstance(Type type) {
        return LocalDateTime.now();
    }
}
