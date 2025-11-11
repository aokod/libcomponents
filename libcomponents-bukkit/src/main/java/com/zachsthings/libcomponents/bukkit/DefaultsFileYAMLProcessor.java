/*
 * libcomponents
 * Copyright (C) 2012 zml2008
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.zachsthings.libcomponents.bukkit;

import com.sk89q.util.yaml.YAMLProcessor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;

/**
 * A simple YAMLProcessor that loads YAML files from the jar's defaults/ folder
 */
public class DefaultsFileYAMLProcessor extends YAMLProcessor {
    private final String file;
    private final ClassLoader classLoader;
    
    /**
     * Creates a DefaultsFileYAMLProcessor using the plugin's classloader.
     * This constructor should be preferred as it works correctly with shadow JARs.
     * 
     * @param plugin The JavaPlugin instance to use for resource loading
     * @param file The filename in the defaults/ folder
     * @param writeDefaults Whether to write defaults
     */
    public DefaultsFileYAMLProcessor(JavaPlugin plugin, String file, boolean writeDefaults) {
        super(null, writeDefaults);
        this.file = file;
        this.classLoader = plugin.getClass().getClassLoader();
    }
    
    /**
     * Creates a DefaultsFileYAMLProcessor using a specific classloader.
     * 
     * @param classLoader The classloader to use for resource loading
     * @param file The filename in the defaults/ folder
     * @param writeDefaults Whether to write defaults
     */
    public DefaultsFileYAMLProcessor(ClassLoader classLoader, String file, boolean writeDefaults) {
        super(null, writeDefaults);
        this.file = file;
        this.classLoader = classLoader;
    }
    
    /**
     * Creates a DefaultsFileYAMLProcessor using the current thread's context classloader,
     * or falling back to this class's classloader.
     * 
     * @param file The filename in the defaults/ folder
     * @param writeDefaults Whether to write defaults
     * @deprecated Use {@link #DefaultsFileYAMLProcessor(JavaPlugin, String, boolean)} instead
     */
    @Deprecated
    public DefaultsFileYAMLProcessor(String file, boolean writeDefaults) {
        super(null, writeDefaults);
        this.file = file;
        // Try to use context classloader first, fall back to this class's classloader
        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        this.classLoader = contextLoader != null ? contextLoader : getClass().getClassLoader();
    }

    @Override
    public InputStream getInputStream() {
        // Use the provided classloader to load the resource
        // This ensures it works correctly with shadow JARs
        // Try with leading slash first (absolute path from classpath root)
        InputStream stream = classLoader.getResourceAsStream("/defaults/" + file);
        if (stream == null) {
            // Fallback: try without leading slash (relative path)
            stream = classLoader.getResourceAsStream("defaults/" + file);
        }
        return stream;
    }
}
