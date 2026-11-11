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
package org.apache.flume.forwarder.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.flume.Channel;
import org.apache.flume.ChannelSelector;
import org.apache.flume.FlumeException;
import org.apache.flume.Sink;
import org.apache.flume.SinkRunner;
import org.apache.flume.SourceRunner;
import org.apache.flume.channel.LoadBalancingChannelSelector;
import org.apache.flume.channel.file.FileChannel;
import org.apache.flume.conf.Configurables;
import org.apache.flume.sink.LoadBalancingSinkProcessor;
import org.apache.flume.sink.avro.AvroSink;
import org.apache.flume.source.avro.AvroSource;
import org.apache.flume.spring.boot.config.AbstractFlumeConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/** */
@Configuration
@ComponentScan(basePackages = "com.nextiva.flume")
public class FlumeForwarderConfig extends AbstractFlumeConfiguration {

    /*
     * Capture the properties from the Spring configuration.
     */
    @Bean
    @ConfigurationProperties(prefix = "flume.source")
    public Map<String, String> sourceProperties() {
        return new HashMap<>();
    }

    @Bean
    @ConfigurationProperties(prefix = "flume.channels")
    public Map<String, ChannelConfiguration> channelsProperties() {
        return new HashMap<>();
    }

    @Bean
    @ConfigurationProperties(prefix = "flume.sinks")
    public Map<String, SinkConfiguration> sinksProperties() {
        return new HashMap<>();
    }

    @Bean
    @ConfigurationProperties(prefix = "flume.sink-groups.rrobin")
    public Map<String, String> rrobinProperties() {
        return new HashMap<>();
    }

    /*
     * Create the file channels.
     */

    @Bean
    public Map<String, Channel> fileChannels(final Map<String, ChannelConfiguration> channelProperties) {
        Map<String, Channel> channels = new HashMap<>();
        for (Map.Entry<String, ChannelConfiguration> entry : channelProperties.entrySet()) {
            String channelName = entry.getKey();
            FileChannel channel = configureChannel(
                    channelName, FileChannel.class, entry.getValue().getProperties());
            channels.put(channelName, channel);
        }
        return channels;
    }

    /*
     * Create the Sources.
     */
    @Bean
    public SourceRunner source(final Map<String, Channel> fileChannels, final Map<String, String> sourceProperties) {
        final Map<String, String> params = new HashMap<>();
        params.put("policy", "ROUND_ROBIN");
        List<Channel> channels = new ArrayList<>(fileChannels.values());
        ChannelSelector selector = createChannelSelector(LoadBalancingChannelSelector.class, channels, params);

        return configureSource("source", AvroSource.class, selector, sourceProperties);
    }

    /*
     * Create the Sinks.
     */
    @Bean
    public Map<String, List<Sink>> sinks(
            final Map<String, Channel> fileChannels, final Map<String, SinkConfiguration> sinksProperties) {
        Map<String, List<Sink>> sinkMap = new HashMap<>();
        for (Map.Entry<String, Channel> channelEntry : fileChannels.entrySet()) {
            List<Sink> sinks = new ArrayList<>();
            String channelName = channelEntry.getKey();
            String suffix = channelName.substring(channelName.length() - 1);
            for (Map.Entry<String, SinkConfiguration> entry : sinksProperties.entrySet()) {
                sinks.add(configureSink(
                        entry.getKey() + "-" + suffix,
                        AvroSink.class,
                        channelEntry.getValue(),
                        entry.getValue().getProperties()));
            }
            sinkMap.put(channelEntry.getKey(), sinks);
        }
        return sinkMap;
    }

    @Bean
    public Map<String, SinkRunner> sinkRunners(
            final Map<String, String> rrobinProperties, final Map<String, List<Sink>> sinks) {
        Map<String, SinkRunner> sinkRunners = new HashMap<>();
        for (Map.Entry<String, List<Sink>> entry : sinks.entrySet()) {
            sinkRunners.put(
                    entry.getKey(),
                    createSinkRunner(configureSinkProcessor(
                            rrobinProperties, LoadBalancingSinkProcessor.class, entry.getValue())));
        }
        return sinkRunners;
    }

    /**
     * This is a bug in Flume Spring Boot - It doesn't initialize the channels so we need to override it.
     *
     * @param clazz The Selector class.
     * @param channels The Channels.
     * @param params The configuration parameters.
     * @return The ChannelSelector.
     */
    protected ChannelSelector createChannelSelector(
            Class<? extends ChannelSelector> clazz, List<Channel> channels, Map<String, String> params) {
        ChannelSelector selector;
        try {
            selector = clazz.newInstance();
        } catch (Exception ex) {
            throw new FlumeException("Unable to create channel selector " + clazz.getName(), ex);
        }
        selector.setChannels(channels);
        Configurables.configure(selector, createContext(params));
        return selector;
    }

    public static <T> T cast(final Object o) {
        @SuppressWarnings("unchecked")
        final T t = (T) o;
        return t;
    }
}
