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

import de.fraunhofer.iosb.ilt.settings.ConfigDefaults;
import de.fraunhofer.iosb.ilt.settings.annotation.DefaultValue;
import de.fraunhofer.iosb.ilt.settings.annotation.DefaultValueBoolean;
import de.fraunhofer.iosb.ilt.settings.annotation.DefaultValueInt;

/**
 * A test ConfigDefaults provider.
 */
public class MockConfigProvider implements ConfigDefaults {

    @DefaultValueBoolean(true)
    public static final String TAG_ENABLED = "Enabled";
    @DefaultValueInt(2)
    public static final String TAG_SEND_WORKER_COUNT = "sendWorkerPoolSize";
    @DefaultValueInt(2)
    public static final String TAG_RECV_WORKER_COUNT = "recvWorkerPoolSize";
    @DefaultValueInt(100)
    public static final String TAG_SEND_QUEUE_SIZE = "sendQueueSize";
    @DefaultValueInt(100)
    public static final String TAG_RECV_QUEUE_SIZE = "recvQueueSize";
    @DefaultValue("tcp://127.0.0.1:1884")
    public static final String TAG_MQTT_BROKER = "mqttBroker";
    @DefaultValue("FROST-Bus")
    public static final String TAG_TOPIC_NAME = "topicName";
    @DefaultValueInt(2)
    public static final String TAG_QOS_LEVEL = "qosLevel";
    @DefaultValueInt(50)
    public static final String TAG_MAX_IN_FLIGHT = "maxInFlight";
    @DefaultValueBoolean(true)
    public static final String TAG_USE_ABSOLUTE_NAVIGATION_LINKS = "useAbsoluteNavigationLinks";
    @DefaultValueBoolean(false)
    public static final String TAG_AUTH_ALLOW_ANON_READ = "allowAnonymousRead";
    @DefaultValue("")
    public static final String TAG_SERVICE_ROOT_URL = "serviceRootUrl";
    @DefaultValueInt(10000)
    public static final String TAG_MAX_TOP = "maxTop";
    @DefaultValueBoolean(false)
    public static final String TAG_CORS_ENABLE = "cors.enable";

}
