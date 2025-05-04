/*
 * Copyright (C) 2025 Fraunhofer Institute of Optronics, System Technologies and
 * Image Exploitation IOSB, Fraunhoferstr. 1, 76131 Karlsruhe, Germany.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.fraunhofer.iosb.ilt.settings;

/**
 * An abstract class that provides default configuration values, and access to
 * them.
 *
 * @param <T> The implementing class.
 */
public abstract class ConfigProvider<T> implements ConfigDefaults {

    private Settings settings;

    public ConfigProvider() {
    }

    public ConfigProvider(Settings settings) {
        this.settings = settings;
    }

    public T setSettings(Settings settings) {
        this.settings = settings;
        return getThis();
    }

    public Settings getSettings() {
        return settings;
    }

    public Settings getSubSettings(String prefix) {
        return settings.getSubSettings(prefix);
    }

    public String get(String name) {
        return settings.get(name, getClass());
    }

    public boolean getBoolean(String name) {
        return settings.getBoolean(name, getClass());
    }

    public int getInt(String name) {
        return settings.getInt(name, getClass());
    }

    public long getLong(String name) {
        return settings.getLong(name, getClass());
    }

    public double getDouble(String name) {
        return settings.getDouble(name, getClass());
    }

    public abstract T getThis();
}
