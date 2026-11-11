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

# Flume MongoDb

This project uses only standard Flume components. It wires an Avro Source to a set of File Channels and wires a MongoDb Sink to those same File Channels.

To start this Flume so that it can be used with the forwarder, write to a local MongoDB
instance, and allow statistics to be queried via HTTP use: 
```
java -Dflume.monitoring.type=http -Dflume.monitoring.port=6443 -jar flume-native-mongodb-2.0.0-SNAPSHOT.jar -u classpath:///flume-mongodb-config.yml -u classpath:///flume-mongodb-local.yml -n mongodb
```
