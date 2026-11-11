/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.flume.sample.mongodb.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.flume.Channel;
import org.apache.flume.ChannelSelector;
import org.apache.flume.Sink;
import org.apache.flume.SinkRunner;
import org.apache.flume.SourceRunner;
import org.apache.flume.channel.LoadBalancingChannelSelector;
import org.apache.flume.channel.file.FileChannel;
import org.apache.flume.sink.mongodb.MongoDbSink;
import org.apache.flume.source.avro.AvroSource;
import org.apache.flume.spring.boot.config.AbstractFlumeConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/** */
@Configuration
@ComponentScan(basePackages = "org.apache.flume")
public class FlumeMongoDbConfig extends AbstractFlumeConfiguration {
    private static final String SINK_NAME = "MongoDbSink-";
    /*
     * Capture the properties from the Spring configuration.
     */
    @Bean
    @ConfigurationProperties(prefix = "flume.sources.avro")
    public Map<String, String> ratingSourceProperties() {
        return new HashMap<>();
    }

    @Bean
    @ConfigurationProperties(prefix = "flume.channels.file-channels")
    public Map<String, ChannelConfiguration> ratingChannelsProperties() {
        return new HashMap<>();
    }

    @Bean
    @ConfigurationProperties(prefix = "flume.sinks.mongodb")
    public MongoDbSinkConfiguration mongoDbSinkProperties() {
        return new MongoDbSinkConfiguration();
    }

    /*
     * Create the file channels.
     */

    @Bean
    public Map<String, Channel> ratingFileChannels(final Map<String, ChannelConfiguration> ratingChannelProperties) {
        Map<String, Channel> channels = new HashMap<>();
        for (Map.Entry<String, ChannelConfiguration> entry : ratingChannelProperties.entrySet()) {
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
    public SourceRunner ratingSource(
            final Map<String, Channel> ratingFileChannels, final Map<String, String> ratingSourceProperties) {
        final Map<String, String> params = new HashMap<>();
        params.put("policy", "ROUND_ROBIN");
        List<Channel> channels = new ArrayList<>(ratingFileChannels.values());
        ChannelSelector selector = createChannelSelector(LoadBalancingChannelSelector.class, channels, params);

        return configureSource("ratingSource", AvroSource.class, selector, ratingSourceProperties);
    }

    /*
     * Create the Sinks and SinkRunners This creates a single MongoDbSink per file channel.
     */
    @Bean
    public Map<String, SinkRunner> sinkRunners(
            final Map<String, Channel> ratingFileChannels, final MongoDbSinkConfiguration mongoDbSinkProperties) {
        List<Sink> sinks = new ArrayList<>();
        for (Map.Entry<String, Channel> channelEntry : ratingFileChannels.entrySet()) {
            String channelName = channelEntry.getKey();
            String suffix = channelName.substring(channelName.length() - 1);
            sinks.add(configureSink(
                    SINK_NAME + suffix,
                    MongoDbSink.class,
                    channelEntry.getValue(),
                    mongoDbSinkProperties.getProperties()));
        }
        return createSinkRunners(sinks);
    }
}
