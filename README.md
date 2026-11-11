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

# Flume Samples

This project contains:
1. A sample application that sends data to Flume.
2. A Flume instance configured to forward events to a second Flume instance.
3. A Flume instance that is built using Spring Boot configured to forward events to a second Flume instance.
4. A Flume instance configured to write JSON events to MongoDb. 
5. A Flume instance that is built using Spring Boot configured to write JSON events to MongoDb.

In all of these cases File Channels are used to provide guaranteed delivery.

To run these samples first install MongoDb. There are a couple of options to do this but on MacOS
using homebrew works quite nicely. Once it is installed MongoDb Compass can be used to
create a database named "Events" and a collection named "events". To fully test the
MongoDb Sink also create an index on requestDto.id as a text field and mark it as unique.

Once MongoDb is up and running start a Flume MongoDb instance and a Flume Forwarder instance
using the instructions found in those projects. Finally, run the FlumeClient. The first 
run will cause a document to be created in the events collection. If it is run again 
a second record will NOT be created and a message will be logged in the Flume MongoDb log
file.

**NOTE:** These samples are just that. They are **NOT** intended to be used as is without
some customization. For example, it is extremely unlikely your application will be as simple 
as FlumeClient and your services are not likely all going to run on localhost. For that 
reason there should be no expectation that there will ever be formal "releases" of the 
code in this repository. However, please feel free to take the source code and modify
it to meet your specific needs.