<!--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

# Flume Native Forwarder

This project uses only standard Flume components. It wires an Avro Source to a set of File Channels and wires one or more Avro Sinks to those same File Channels.

To start this Flume so that it can be used with one of the Flume samples that writes
to MongoDb and allow statistics to be queried via HTTP use: 
```text
java -Dflume.monitoring.type=http -Dflume.monitoring.port=6444 -jar flume-native-forwarder-2.0.0-SNAPSHOT.jar -u classpath:///flume-forwarder-config.yml -u classpath:///flume-forwarder-local.yml -n forwarder 
```
This sample forwarder is configured to listen at port 4141 so that the FlumeClient application 
can send messages to it. It forwards to a second Flume listening at localhost port 4142.