package com.zacharybarbanell.betterplanting.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;

import javax.swing.text.html.Option;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.client.event.InputEvent.MouseButton.Pre;

public class Config {
    private static Logger LOGGER = LogUtils.getLogger();
    private boolean isLoaded = false;
    private List<Entry<?>> entries = new ArrayList<>();
    private Set<String> keys = new HashSet<>();
    private Path path;
    private String comment;

    public Config(Path path, String comment) {
        this.path = path;
        this.comment = comment;
    }

    public <T> ConfigEntry<T> register(String name, T defaultValue) {
        if (isLoaded) {
            throw new IllegalStateException("Config has already been loaded");
        }
        if (keys.contains(name)) {
            throw new IllegalArgumentException("Entry %s is already defined".formatted(name));
        }

        Class<T> clazz = (Class<T>) defaultValue.getClass();
        
        keys.add(name);
        Entry<T> entry = new Entry<T>(name, clazz, defaultValue);
        entries.add(entry);
        
        return entry;
    }

    public void load() {
        if (isLoaded) {
            throw new IllegalStateException("Config has already been loaded");
        }
        isLoaded = true;
        Properties in = new Properties();
        boolean warnMissing = true;
        if (Files.exists(path)) {
            try (InputStream input = new FileInputStream(path.toFile())) {
                in.load(input);
            }
            catch (IOException e) {
                throw new RuntimeException(e);   
            }
        }
        else {
            LOGGER.warn("No config file found at {}, creating default", path);
            warnMissing = false;
        }
        for (Entry<?> entry : entries) {
            String val = in.getProperty(entry.name);
            if (val != null) {
                Optional<?> result = Parser.parse(entry.name, entry.clazz);
                if (result.isPresent()) {
                    Object resultVal = result.get();
                    if (entry.isValid(resultVal)) {
                        entry.value = resultVal;
                    }
                    else {
                        LOGGER.warn("{} is not a legal value for {}, replacing it with default", resultVal, entry.name);
                    }
                }
                else {
                    LOGGER.warn("Could not parse {} as a value for {}, replacing it with default", in.getProperty(entry.name), entry.name);
                }
            }
            else {
                if (warnMissing) {
                    LOGGER.warn("Config element {} not found, creating default", entry.name);
                }
            }
        }
        Properties out = new Properties();
        for (Entry<?> entry : entries) {
            out.setProperty(entry.name, entry.value.toString());
        }
        try (OutputStream output = new FileOutputStream(path.toFile())) {
            out.store(output, comment);
        }
        catch (IOException e) {
            throw new RuntimeException(e);   
        }
    }

    private class Entry<T> implements ConfigEntry<T> {
        private final String name;
        private final Class<T> clazz;
        private Object value;
        private Optional<String> comment;
        private Predicate<T> restriction;

        private Entry(String name, Class<T> clazz, T value) {
            this.name = name;
            this.clazz = clazz;
            this.value = value;
            this.comment = Optional.empty();
            this.restriction = (val) -> true;
        }

        @Override
        public T get() {
            if (!isLoaded) {
                throw new IllegalStateException("Config is not loaded");
            }
            return (T) value;
        }

        private boolean isValid(Object val) {
            return restriction.test((T) val);
        }

        @Override
        public ConfigEntry<T> comment(String comment) {
            this.comment = Optional.of(comment);
            return this;
        }

        @Override
        public ConfigEntry<T> addRestriction(Predicate<T> restriction) {
            this.restriction = this.restriction.and(restriction);
            return this;
        }
    }
}