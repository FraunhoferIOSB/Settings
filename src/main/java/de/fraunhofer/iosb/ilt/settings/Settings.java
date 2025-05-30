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

import de.fraunhofer.iosb.ilt.settings.annotation.SensitiveValue;
import de.fraunhofer.iosb.ilt.settings.exceptions.PropertyMissingException;
import de.fraunhofer.iosb.ilt.settings.exceptions.PropertyTypeException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A settings-holder.
 */
public class Settings {

    private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);
    private static final String NOT_SET_USING_DEFAULT_VALUE = "Not set {}{}, using default value '{}'.";
    private static final String NOT_SET_NO_DEFAULT_VALUE = "Not set {}, and no default value!";
    private static final String ERROR_GETTING_SETTINGS_VALUE = "error getting settings value";
    private static final String SETTING_HAS_VALUE = "Setting {}{} has value '{}'.";
    private static final String HIDDEN_VALUE = "*****";

    private final Properties properties;
    private boolean logSensitiveData;
    private String prefix;

    private static Properties addEnvironment(Properties wrapped) {
        Map<String, String> environment = System.getenv();
        Properties wrapper = new Properties(wrapped);

        Map<String, String> sortedEnv = new TreeMap<>(environment);
        for (Map.Entry<String, String> entry : sortedEnv.entrySet()) {
            String key = entry.getKey().replace('_', '.');
            LOGGER.debug("Added environment variable: {}", key);
            wrapper.setProperty(key, entry.getValue());
        }
        return wrapper;
    }

    /**
     * Creates a new settings, containing only environment variables.
     */
    public Settings() {
        this(new Properties(), "", true, false);
    }

    /**
     * Creates a new settings, containing the given properties, and environment
     * variables, with no prefix.
     *
     * @param properties The properties to use. These can be overridden by
     * environment variables.
     */
    public Settings(Properties properties) {
        this(properties, "", true, false);
    }

    /**
     * Creates a new settings, containing the given properties, and environment
     * variables, with the given prefix.
     *
     * @param properties The properties to use.
     * @param prefix The prefix to use.
     * @param wrapInEnvironment Flag indicating if environment variables can
     * override the given properties.
     * @param logSensitiveData Flag indicating things like passwords should be
     * logged completely, not hidden.
     */
    public Settings(Properties properties, String prefix, boolean wrapInEnvironment, boolean logSensitiveData) {
        if (properties == null) {
            throw new IllegalArgumentException("properties must be non-null");
        }
        if (wrapInEnvironment) {
            this.properties = addEnvironment(properties);
        } else {
            this.properties = properties;
        }
        this.prefix = (prefix == null ? "" : prefix);
        this.logSensitiveData = logSensitiveData;
    }

    /**
     * Get the prefix used in this Settings.
     *
     * @return The prefix used in this Settings.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Get the properties used in this Settings. This is the properties
     * configured when creating this Settings, optionally wrapped in a
     * properties containing all environment variables.
     *
     * @return The properties used in this Settings.
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Create a sub-settings, based on this Settings, with the given prefix
     * appended to the prefix of this Settings.
     *
     * @param prefix The prefix to use for the new settings. This is appended to
     * the prefix of this Settings.
     * @return A new Settings, with the given prefix appended to the prefix of
     * this Settings.
     */
    public Settings getSubSettings(String prefix) {
        return new CachedSettings(this, prefix);
    }

    /**
     * Get the current setting if sensitive data is logged.
     *
     * @return The current setting if sensitive data is logged.
     */
    public boolean getLogSensitiveData() {
        return logSensitiveData;
    }

    /**
     * Change the setting if sensitive data is logged.
     *
     * @param logSensitiveData if true, sensitive data is logged.
     */
    public void setLogSensitiveData(boolean logSensitiveData) {
        this.logSensitiveData = logSensitiveData;
    }

    /**
     * Get the key as it is used in the config file or environment variables,
     * for the property with the given name. The key is the name, with the
     * prefix prepended to it.
     *
     * @param propertyName The name to get the key for.
     * @return prefix + propertyName
     */
    private String getPropertyKey(String propertyName) {
        return prefix + propertyName.replace('_', '.');
    }

    /**
     * Check if there is a property with the given name. The prefix is prepended
     * to the name before lookup.
     *
     * @param name The name to look up.
     * @return True if there is a property with the given name.
     */
    public boolean containsName(String name) {
        // properties.containsKey ignores properties defaults
        String val = properties.getProperty(getPropertyKey(name));
        return val != null;
    }

    /**
     * Check if the given key is present. Throws a PropertyMissingException if
     * not.
     *
     * @param key The key to look up.
     */
    private void checkExists(String key) {
        if (properties.getProperty(key) != null) {
            return;
        }
        LOGGER.error(NOT_SET_NO_DEFAULT_VALUE, key);
        throw new PropertyMissingException(key);
    }

    /**
     * Set the variable with the given name to the given (String) value.
     *
     * @param name The name of the variable to set.
     * @param value The value to set the variable to.
     */
    public void set(String name, String value) {
        properties.put(getPropertyKey(name), value);
    }

    /**
     * Set the variable with the given name to the given (boolean) value.
     *
     * @param name The name of the variable to set.
     * @param value The value to set the variable to.
     */
    public void set(String name, boolean value) {
        properties.put(getPropertyKey(name), Boolean.toString(value));
    }

    /**
     * Set the variable with the given name to the given (integer) value.
     *
     * @param name The name of the variable to set.
     * @param value The value to set the variable to.
     */
    public void set(String name, int value) {
        properties.put(getPropertyKey(name), Integer.toString(value));
    }

    /**
     * Get the (String) value of the property with the given name, prefixed with
     * the prefix of this properties. The value of the property will be logged.
     * Use {@link #getSensitive(String)} to fetch a sensitive value.
     *
     * @param name The name of the property to get. The prefix will be prepended
     * to this name.
     * @return The value of the requested property. Throws a
     * {@link PropertyMissingException} if the property is not found.
     */
    public String get(String name) {
        return get(name, false);
    }

    /**
     * Get the property with the given name, prefixed with the prefix of this
     * properties. The value of the property will NOT be logged.
     *
     * @param name The name of the property to get. The prefix will be prepended
     * to this name.
     * @return The value of the requested property. Throws a
     * PropertyMissingException if the property is not found.
     */
    public String getSensitive(String name) {
        return get(name, true);
    }

    private String get(String name, boolean sensitiveValue) {
        String key = getPropertyKey(name);
        checkExists(key);
        String value = properties.getProperty(key);
        logHasValue(name, value, sensitiveValue);
        return value;
    }

    /**
     * Get the (String) value of the property with the given name, prefixed with
     * the prefix of this properties. The value of the property will be logged.
     * Use {@link #getSensitive(String)} to fetch a sensitive value.
     *
     * @param name The name of the property to get. The prefix will be prepended
     * to this name.
     * @param defaultValue The default value to use when the property is not
     * set.
     * @return The value of the requested property.
     */
    public String get(String name, String defaultValue) {
        return get(name, defaultValue, false);
    }

    /**
     * Get the (String) value of the property with the given name, prefixed with
     * the prefix of this properties. The value of the property will NOT be
     * logged.
     *
     * @param name The name of the property to get. The prefix will be prepended
     * to this name.
     * @param defaultValue The default value to use when the property is not
     * set.
     * @return The value of the requested property.
     */
    public String getSensitive(String name, String defaultValue) {
        return get(name, defaultValue, true);
    }

    private String get(String name, String defaultValue, boolean sensitive) {
        String key = getPropertyKey(name);
        String value = properties.getProperty(key);
        if (value == null) {
            logDefaultValue(name, defaultValue, sensitive);
            return defaultValue;
        }
        logHasValue(name, value, sensitive);
        return value;
    }

    /**
     * Get the (String) value of the property with the given name, using the
     * given ConfigDefaults to provide a default value and the sensitivity flag.
     * The value of the property will be logged unless it is annotated with
     * {@link SensitiveValue}
     *
     * @param name The name of the property to fetch.
     * @param defaultsProvider The ConfigDefaults to use for supplying a default
     * value if the property is not set and for supplying the sensitivity flag.
     * @return The set value of the property, or the value provided by the
     * defaultsProvider if the property is not set.
     */
    public String get(String name, Class<? extends ConfigDefaults> defaultsProvider) {
        final String key = getPropertyKey(name);
        final String value = properties.getProperty(key);
        final boolean sensitive = ConfigUtils.isSensitive(defaultsProvider, name);
        if (value == null) {
            final String defaultValue = ConfigUtils.getDefaultValue(defaultsProvider, name);
            logDefaultValue(name, defaultValue, sensitive);
            return defaultValue;
        }
        logHasValue(name, value, sensitive);
        return value;
    }

    /**
     * Get the (int) value of the property with the given name, prefixed with
     * the prefix of this properties. The value of the property will be logged.
     * Use {@link #getSensitive(String)} to fetch a sensitive value.
     *
     * @param name The name of the property to get. The prefix will be prepended
     * to this name.
     * @return The value of the requested property. Throws a
     * {@link PropertyMissingException} if the property is not found.
     */
    public int getInt(String name) {
        return getInt(name, false);
    }

    private int getInt(String name, boolean sensitive) {
        try {
            return Integer.parseInt(get(name, sensitive));
        } catch (NumberFormatException ex) {
            throw new PropertyTypeException(name, Integer.class, ex);
        }
    }

    /**
     * Get the (int) value of the property with the given name, prefixed with
     * the prefix of this properties. The value of the property will be logged.
     * Use {@link #getSensitive(String)} to fetch a sensitive value.
     *
     * @param name The name of the property to get. The prefix will be prepended
     * to this name.
     * @param defaultValue The default value to use when the property is not
     * set.
     * @return The value of the requested property.
     */
    public int getInt(String name, int defaultValue) {
        if (containsName(name)) {
            try {
                return getInt(name);
            } catch (Exception ex) {
                LOGGER.trace(ERROR_GETTING_SETTINGS_VALUE, ex);
            }
        }
        LOGGER.info(NOT_SET_USING_DEFAULT_VALUE, prefix, name, defaultValue);
        return defaultValue;
    }

    /**
     * Get the (int) value of the property with the given name, using the given
     * ConfigDefaults to provide a default value and the sensitivity flag. The
     * value of the property will be logged unless it is annotated with
     * {@link SensitiveValue}
     *
     * @param name The name of the property to fetch.
     * @param defaultsProvider The ConfigDefaults to use for supplying a default
     * value if the property is not set and for supplying the sensitivity flag.
     * @return The set value of the property, or the value provided by the
     * defaultsProvider if the property is not set.
     */
    public int getInt(String name, Class<? extends ConfigDefaults> defaultsProvider) {
        final boolean sensitive = ConfigUtils.isSensitive(defaultsProvider, name);
        if (containsName(name)) {
            try {
                return getInt(name, sensitive);
            } catch (Exception ex) {
                LOGGER.trace(ERROR_GETTING_SETTINGS_VALUE, ex);
            }
        }
        int defaultValue = ConfigUtils.getDefaultValueInt(defaultsProvider, name);
        logDefaultValue(name, Integer.toString(defaultValue), sensitive);
        return defaultValue;
    }

    /**
     * Get the (long) value of the property with the given name, prefixed with
     * the prefix of this properties. The value of the property will be logged.
     * Use {@link #getSensitive(String)} to fetch a sensitive value.
     *
     * @param name The name of the property to get. The prefix will be prepended
     * to this name.
     * @return The value of the requested property. Throws a
     * {@link PropertyMissingException} if the property is not found.
     */
    public long getLong(String name) {
        return getLong(name, false);
    }

    private long getLong(String name, boolean sensitive) {
        try {
            return Long.parseLong(get(name, sensitive));
        } catch (NumberFormatException ex) {
            throw new PropertyTypeException(name, Long.class, ex);
        }
    }

    /**
     * Get the (long) value of the property with the given name, prefixed with
     * the prefix of this properties. The value of the property will be logged.
     * Use {@link #getSensitive(String)} to fetch a sensitive value.
     *
     * @param name The name of the property to get. The prefix will be prepended
     * to this name.
     * @param defaultValue The default value to use when the property is not
     * set.
     * @return The value of the requested property.
     */
    public long getLong(String name, long defaultValue) {
        if (containsName(name)) {
            try {
                return getLong(name);
            } catch (Exception ex) {
                LOGGER.trace(ERROR_GETTING_SETTINGS_VALUE, ex);
            }
        }
        LOGGER.info(NOT_SET_USING_DEFAULT_VALUE, prefix, name, defaultValue);
        return defaultValue;
    }

    /**
     * Get the (long) value of the property with the given name, using the given
     * ConfigDefaults to provide a default value and the sensitivity flag. The
     * value of the property will be logged unless it is annotated with
     * {@link SensitiveValue}
     *
     * @param name The name of the property to fetch.
     * @param defaultsProvider The ConfigDefaults to use for supplying a default
     * value if the property is not set and for supplying the sensitivity flag.
     * @return The set value of the property, or the value provided by the
     * defaultsProvider if the property is not set.
     */
    public long getLong(String name, Class<? extends ConfigDefaults> defaultsProvider) {
        final boolean sensitive = ConfigUtils.isSensitive(defaultsProvider, name);
        if (containsName(name)) {
            try {
                return getLong(name, sensitive);
            } catch (Exception ex) {
                LOGGER.trace(ERROR_GETTING_SETTINGS_VALUE, ex);
            }
        }
        int defaultValue = ConfigUtils.getDefaultValueInt(defaultsProvider, name);
        logDefaultValue(name, Long.toString(defaultValue), sensitive);
        return defaultValue;

    }

    /**
     * Get the (double) value of the property with the given name, prefixed with
     * the prefix of this properties. The value of the property will be logged.
     * Use {@link #getSensitive(String)} to fetch a sensitive value.
     *
     * @param name The name of the property to get. The prefix will be prepended
     * to this name.
     * @return The value of the requested property. Throws a
     * {@link PropertyMissingException} if the property is not found.
     */
    public double getDouble(String name) {
        return getDouble(name, false);
    }

    private double getDouble(String name, boolean sensitive) {
        try {
            return Double.parseDouble(get(name, sensitive));
        } catch (NumberFormatException ex) {
            throw new PropertyTypeException(name, Double.class, ex);
        }
    }

    /**
     * Get the (double) value of the property with the given name, prefixed with
     * the prefix of this properties. The value of the property will be logged.
     * Use {@link #getSensitive(String)} to fetch a sensitive value.
     *
     * @param name The name of the property to get. The prefix will be prepended
     * to this name.
     * @param defaultValue The default value to use when the property is not
     * set.
     * @return The value of the requested property.
     */
    public double getDouble(String name, double defaultValue) {
        if (containsName(name)) {
            try {
                return getDouble(name);
            } catch (Exception ex) {
                LOGGER.trace(ERROR_GETTING_SETTINGS_VALUE, ex);
            }
        }
        LOGGER.info(NOT_SET_USING_DEFAULT_VALUE, prefix, name, defaultValue);
        return defaultValue;
    }

    /**
     * Get the (double) value of the property with the given name, using the
     * given ConfigDefaults to provide a default value and the sensitivity flag.
     * The value of the property will be logged unless it is annotated with
     * {@link SensitiveValue}
     *
     * @param name The name of the property to fetch.
     * @param defaultsProvider The ConfigDefaults to use for supplying a default
     * value if the property is not set and for supplying the sensitivity flag.
     * @return The set value of the property, or the value provided by the
     * defaultsProvider if the property is not set.
     */
    public double getDouble(String name, Class<? extends ConfigDefaults> defaultsProvider) {
        final boolean sensitive = ConfigUtils.isSensitive(defaultsProvider, name);
        if (containsName(name)) {
            try {
                return getDouble(name, sensitive);
            } catch (Exception ex) {
                LOGGER.trace(ERROR_GETTING_SETTINGS_VALUE, ex);
            }
        }
        double defaultValue = ConfigUtils.getDefaultValueDouble(defaultsProvider, name);
        logDefaultValue(name, Double.toString(defaultValue), sensitive);
        return defaultValue;

    }

    /**
     * Get the (boolean) value of the property with the given name, prefixed
     * with the prefix of this properties. The value of the property will be
     * logged. Use {@link #getSensitive(String)} to fetch a sensitive value.
     *
     * @param name The name of the property to get. The prefix will be prepended
     * to this name.
     * @return The value of the requested property. Throws a
     * {@link PropertyMissingException} if the property is not found.
     */
    public boolean getBoolean(String name) {
        return getBooleanPriv(name, false);
    }

    private boolean getBooleanPriv(String name, boolean sensitive) {
        try {
            return Boolean.parseBoolean(get(name, sensitive));
        } catch (PropertyMissingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new PropertyTypeException(name, Boolean.class, ex);
        }
    }

    /**
     * Get the (boolean) value of the property with the given name, prefixed
     * with the prefix of this properties. The value of the property will be
     * logged. Use {@link #getSensitive(String)} to fetch a sensitive value.
     *
     * @param name The name of the property to get. The prefix will be prepended
     * to this name.
     * @param defaultValue The default value to use when the property is not
     * set.
     * @return The value of the requested property.
     */
    public boolean getBoolean(String name, boolean defaultValue) {
        if (containsName(name)) {
            try {
                return getBoolean(name);
            } catch (Exception ex) {
                LOGGER.trace(ERROR_GETTING_SETTINGS_VALUE, ex);
            }
        }
        LOGGER.info(NOT_SET_USING_DEFAULT_VALUE, prefix, name, defaultValue);
        return defaultValue;
    }

    /**
     * Get the (boolean) value of the property with the given name, using the
     * given ConfigDefaults to provide a default value and the sensitivity flag.
     * The value of the property will be logged unless it is annotated with
     * {@link SensitiveValue}
     *
     * @param name The name of the property to fetch.
     * @param defaultsProvider The ConfigDefaults to use for supplying a default
     * value if the property is not set and for supplying the sensitivity flag.
     * @return The set value of the property, or the value provided by the
     * defaultsProvider if the property is not set.
     */
    public boolean getBoolean(String name, Class<? extends ConfigDefaults> defaultsProvider) {
        final boolean sensitive = ConfigUtils.isSensitive(defaultsProvider, name);
        if (containsName(name)) {
            try {
                return getBoolean(name, sensitive);
            } catch (Exception ex) {
                LOGGER.trace(ERROR_GETTING_SETTINGS_VALUE, ex);
            }
        }
        boolean defaultValue = ConfigUtils.getDefaultValueBoolean(defaultsProvider, name);
        logDefaultValue(name, Boolean.toString(defaultValue), sensitive);
        return defaultValue;
    }

    private void logHasValue(String name, String value, boolean sensitive) {
        if (!sensitive || logSensitiveData) {
            LOGGER.info(SETTING_HAS_VALUE, prefix, name, value);
        } else {
            LOGGER.info(SETTING_HAS_VALUE, prefix, name, HIDDEN_VALUE);
        }
    }

    private void logDefaultValue(String name, String defaultValue, boolean sensitive) {
        if (!sensitive || logSensitiveData) {
            LOGGER.info(NOT_SET_USING_DEFAULT_VALUE, prefix, name, defaultValue);
        } else {
            LOGGER.info(NOT_SET_USING_DEFAULT_VALUE, prefix, name, HIDDEN_VALUE);
        }
    }

}
