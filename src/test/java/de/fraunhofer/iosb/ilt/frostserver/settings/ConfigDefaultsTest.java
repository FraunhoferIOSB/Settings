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
package de.fraunhofer.iosb.ilt.frostserver.settings;

import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_AUTH_ALLOW_ANON_READ;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_CORS_ENABLE;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_ENABLED;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_MAX_IN_FLIGHT;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_MAX_TOP;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_MQTT_BROKER;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_QOS_LEVEL;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_RECV_QUEUE_SIZE;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_RECV_WORKER_COUNT;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_SEND_QUEUE_SIZE;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_SEND_WORKER_COUNT;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_SERVICE_ROOT_URL;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_TOPIC_NAME;
import static de.fraunhofer.iosb.ilt.frostserver.settings.MockConfigProvider.TAG_USE_ABSOLUTE_NAVIGATION_LINKS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import de.fraunhofer.iosb.ilt.settings.ConfigUtils;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigDefaultsTest {

    public ConfigDefaultsTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    void testDefaultValueLookupInteger() {
        MockConfigProvider b = new MockConfigProvider();
        // Test valid integer properties
        assertEquals(2, b.defaultValueInt(TAG_SEND_WORKER_COUNT));
        assertEquals(2, b.defaultValueInt(TAG_RECV_WORKER_COUNT));
        assertEquals(100, b.defaultValueInt(TAG_SEND_QUEUE_SIZE));
        assertEquals(100, b.defaultValueInt(TAG_RECV_QUEUE_SIZE));
        assertEquals(2, b.defaultValueInt(TAG_QOS_LEVEL));
        assertEquals(50, b.defaultValueInt(TAG_MAX_IN_FLIGHT));
    }

    @Test
    void testDefaultValueLookupString() {
        MockConfigProvider b = new MockConfigProvider();
        // Test valid string properties
        assertEquals("tcp://127.0.0.1:1884", b.defaultValue(TAG_MQTT_BROKER));
        assertEquals("FROST-Bus", b.defaultValue(TAG_TOPIC_NAME));

    }

    @Test
    void testDefaultValueLookupBoolean() {
        // Test valid boolean properties
        MockConfigProvider b = new MockConfigProvider();
        assertEquals(true, b.defaultValueBoolean(TAG_USE_ABSOLUTE_NAVIGATION_LINKS));
        assertEquals(false, b.defaultValueBoolean(TAG_AUTH_ALLOW_ANON_READ));

    }

    @Test
    void testDefaultValueLookupIntegerString() {
        MockConfigProvider b = new MockConfigProvider();
        // Test reading integer properties as strings
        assertEquals("2", b.defaultValue(TAG_SEND_WORKER_COUNT));
        assertEquals("2", b.defaultValue(TAG_RECV_WORKER_COUNT));
        assertEquals("100", b.defaultValue(TAG_SEND_QUEUE_SIZE));
        assertEquals("100", b.defaultValue(TAG_RECV_QUEUE_SIZE));
        assertEquals("2", b.defaultValue(TAG_QOS_LEVEL));
        assertEquals("50", b.defaultValue(TAG_MAX_IN_FLIGHT));

    }

    @Test
    void testDefaultValueLookupBooleanString() {
        // Test reading boolean properties as strings
        MockConfigProvider b = new MockConfigProvider();
        assertEquals(Boolean.TRUE.toString(), b.defaultValue(TAG_USE_ABSOLUTE_NAVIGATION_LINKS));
        assertEquals(Boolean.FALSE.toString(), b.defaultValue(TAG_AUTH_ALLOW_ANON_READ));

    }

    @Test
    void testDefaultValueLookupInvalid() {
        MockConfigProvider b = new MockConfigProvider();
        // Test invalid properties
        try {
            b.defaultValueInt("NOT_A_VALID_INT_PROPERTY");
            fail("Should have thrown an exception for a non-existing default value.");
        } catch (IllegalArgumentException exc) {
            // This should happen.
        }
        try {
            b.defaultValue("NOT_A_VALID_STR_PROPERTY");
            fail("Should have thrown an exception for a non-existing default value.");
        } catch (IllegalArgumentException exc) {
            // This should happen.
        }
        try {
            b.defaultValueBoolean("NOT_A_VALID_BOOL_PROPERTY");
            fail("Should have thrown an exception for a non-existing default value.");
        } catch (IllegalArgumentException exc) {
            // This should happen.
        }

    }

    @Test
    void testDefaultValueLookupConfigTags() {
        MockConfigProvider b = new MockConfigProvider();
        // Test configTags
        Set<String> tags = new HashSet<>();
        tags.add(TAG_ENABLED);
        tags.add(TAG_SEND_WORKER_COUNT);
        tags.add(TAG_RECV_WORKER_COUNT);
        tags.add(TAG_SEND_QUEUE_SIZE);
        tags.add(TAG_RECV_QUEUE_SIZE);
        tags.add(TAG_MQTT_BROKER);
        tags.add(TAG_TOPIC_NAME);
        tags.add(TAG_QOS_LEVEL);
        tags.add(TAG_MAX_IN_FLIGHT);
        tags.add(TAG_USE_ABSOLUTE_NAVIGATION_LINKS);
        tags.add(TAG_AUTH_ALLOW_ANON_READ);
        tags.add(TAG_SERVICE_ROOT_URL);
        tags.add(TAG_MAX_TOP);
        tags.add(TAG_CORS_ENABLE);
        assertEquals(tags, b.configTags());

    }

    @Test
    void testDefaultValueLookupConfigDefaults() {
        MockConfigProvider b = new MockConfigProvider();
        // Test configDefaults
        Map<String, String> configDefaults = b.configDefaults();
        assertEquals("tcp://127.0.0.1:1884", configDefaults.get(TAG_MQTT_BROKER));
        assertEquals("FROST-Bus", configDefaults.get(TAG_TOPIC_NAME));
        assertEquals("2", configDefaults.get(TAG_SEND_WORKER_COUNT));
        assertEquals("2", configDefaults.get(TAG_RECV_WORKER_COUNT));
        assertEquals("100", configDefaults.get(TAG_SEND_QUEUE_SIZE));
        assertEquals("100", configDefaults.get(TAG_RECV_QUEUE_SIZE));
        assertEquals("2", configDefaults.get(TAG_QOS_LEVEL));
        assertEquals("50", configDefaults.get(TAG_MAX_IN_FLIGHT));
    }

    @Test
    void testDefaultValueLookupClassInteger() {
        Class c = MockConfigProvider.class;
        // Test valid integer properties
        assertEquals(2, ConfigUtils.getDefaultValueInt(c, TAG_SEND_WORKER_COUNT));
        assertEquals(2, ConfigUtils.getDefaultValueInt(c, TAG_RECV_WORKER_COUNT));
        assertEquals(100, ConfigUtils.getDefaultValueInt(c, TAG_SEND_QUEUE_SIZE));
        assertEquals(100, ConfigUtils.getDefaultValueInt(c, TAG_RECV_QUEUE_SIZE));
        assertEquals(2, ConfigUtils.getDefaultValueInt(c, TAG_QOS_LEVEL));
        assertEquals(50, ConfigUtils.getDefaultValueInt(c, TAG_MAX_IN_FLIGHT));
    }

    @Test
    void testDefaultValueLookupClassString() {
        Class c = MockConfigProvider.class;
        // Test valid string properties
        assertEquals("tcp://127.0.0.1:1884", ConfigUtils.getDefaultValue(c, TAG_MQTT_BROKER));
        assertEquals("FROST-Bus", ConfigUtils.getDefaultValue(c, TAG_TOPIC_NAME));
    }

    @Test
    void testDefaultValueLookupClassBoolean() {
        // Test valid boolean properties
        assertEquals(true, ConfigUtils.getDefaultValueBoolean(MockConfigProvider.class, TAG_USE_ABSOLUTE_NAVIGATION_LINKS));
        assertEquals(false, ConfigUtils.getDefaultValueBoolean(MockConfigProvider.class, TAG_AUTH_ALLOW_ANON_READ));

    }

    @Test
    void testDefaultValueLookupClassIntegerString() {
        Class c = MockConfigProvider.class;
        // Test reading integer properties as strings
        assertEquals("2", ConfigUtils.getDefaultValue(c, TAG_SEND_WORKER_COUNT));
        assertEquals("2", ConfigUtils.getDefaultValue(c, TAG_RECV_WORKER_COUNT));
        assertEquals("100", ConfigUtils.getDefaultValue(c, TAG_SEND_QUEUE_SIZE));
        assertEquals("100", ConfigUtils.getDefaultValue(c, TAG_RECV_QUEUE_SIZE));
        assertEquals("2", ConfigUtils.getDefaultValue(c, TAG_QOS_LEVEL));
        assertEquals("50", ConfigUtils.getDefaultValue(c, TAG_MAX_IN_FLIGHT));

    }

    @Test
    void testDefaultValueLookupClassBooleanString() {
        // Test reading boolean properties as strings
        assertEquals(Boolean.TRUE.toString(), ConfigUtils.getDefaultValue(MockConfigProvider.class, TAG_USE_ABSOLUTE_NAVIGATION_LINKS));
        assertEquals(Boolean.FALSE.toString(), ConfigUtils.getDefaultValue(MockConfigProvider.class, TAG_AUTH_ALLOW_ANON_READ));

    }

    @Test
    void testDefaultValueLookupClassInvalid() {
        Class c = MockConfigProvider.class;
        // Test invalid properties
        try {
            ConfigUtils.getDefaultValueInt(c, "NOT_A_VALID_INT_PROPERTY");
            fail("Should have thrown an exception for a non-existing default value.");
        } catch (IllegalArgumentException exc) {
            // This should happen.
        }
        try {
            ConfigUtils.getDefaultValue(c, "NOT_A_VALID_STR_PROPERTY");
            fail("Should have thrown an exception for a non-existing default value.");
        } catch (IllegalArgumentException exc) {
            // This should happen.
        }
        try {
            ConfigUtils.getDefaultValue(c, "NOT_A_VALID_STR_PROPERTY");
            fail("Should have thrown an exception for a non-existing default value.");
        } catch (IllegalArgumentException exc) {
            // This should happen.
        }
    }

    @Test
    void testDefaultValueLookupClassConfigTags() {
        Class c = MockConfigProvider.class;
        // Test configTags
        Set<String> tags = new HashSet<>();
        tags.add(TAG_ENABLED);
        tags.add(TAG_SEND_WORKER_COUNT);
        tags.add(TAG_RECV_WORKER_COUNT);
        tags.add(TAG_SEND_QUEUE_SIZE);
        tags.add(TAG_RECV_QUEUE_SIZE);
        tags.add(TAG_MQTT_BROKER);
        tags.add(TAG_TOPIC_NAME);
        tags.add(TAG_QOS_LEVEL);
        tags.add(TAG_MAX_IN_FLIGHT);
        tags.add(TAG_USE_ABSOLUTE_NAVIGATION_LINKS);
        tags.add(TAG_AUTH_ALLOW_ANON_READ);
        tags.add(TAG_SERVICE_ROOT_URL);
        tags.add(TAG_MAX_TOP);
        tags.add(TAG_CORS_ENABLE);
        assertEquals(tags, ConfigUtils.getConfigTags(c));
    }

    @Test
    void testDefaultValueLookupClassConfigDefaults() {
        Class c = MockConfigProvider.class;
        // Test configDefaults
        Map<String, String> configDefaults = ConfigUtils.getConfigDefaults(c);
        assertEquals("tcp://127.0.0.1:1884", configDefaults.get(TAG_MQTT_BROKER));
        assertEquals("FROST-Bus", configDefaults.get(TAG_TOPIC_NAME));
        assertEquals("2", configDefaults.get(TAG_SEND_WORKER_COUNT));
        assertEquals("2", configDefaults.get(TAG_RECV_WORKER_COUNT));
        assertEquals("100", configDefaults.get(TAG_SEND_QUEUE_SIZE));
        assertEquals("100", configDefaults.get(TAG_RECV_QUEUE_SIZE));
        assertEquals("2", configDefaults.get(TAG_QOS_LEVEL));
        assertEquals("50", configDefaults.get(TAG_MAX_IN_FLIGHT));
    }
}
