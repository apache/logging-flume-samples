/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.flume.samples;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.flume.api.FlumeClientException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlumeClient {
    private static final Logger LOGGER = LogManager.getLogger(FlumeClient.class);

    private RpcClient client;
    private String hostname;
    private int port;

    public static void main(String[] args) {
        FlumeClient client = new FlumeClient();
        String event1 = getEvent("/sampleEvent.json");
        // Initialize client with the remote Flume agent's host and port
        client.init("localhost", 4141);
        client.sendDataToFlume(event1);
        client.cleanUp();
    }

    private static String getEvent(String filePath) {
        try (InputStream is = FlumeClient.class.getResourceAsStream(filePath)) {
            if (is == null) {
                throw new IllegalArgumentException("File not found on classpath");
            }
            // Read all bytes directly into a String
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to access event file", ioe);
        }
    }

    public void init(String hostname, int port) {
        Properties properties = new Properties();
        properties.put("compression-type", "deflate");
        properties.put("hosts", "host1");
        properties.put("hosts.host1", "localhost:4141");
        // Setup the RPC connection
        this.client = RpcClientFactory.getInstance(properties);
        // Use the following method to create a thrift client (instead of the above line):
        // this.client = RpcClientFactory.getThriftInstance(hostname, port);
    }

    public void sendDataToFlume(String data) {
        // Create a Flume Event object that encapsulates the sample data
        Map<String, String> headers = new HashMap<>();
        headers.put("rateTypeId", "12");
        headers.put("productCategoryId", "4");
        byte[] body = data.getBytes(StandardCharsets.UTF_8);
        Event event = EventBuilder.withBody(body, headers);

        // Send the event
        try {
            client.append(event);
        } catch (FlumeClientException e) {
            // clean up and recreate the client
            client.close();
            client = null;
            client = RpcClientFactory.getDefaultInstance(hostname, port);
        }
    }

    public void cleanUp() {
        // Close the RPC connection
        client.close();
    }
}
