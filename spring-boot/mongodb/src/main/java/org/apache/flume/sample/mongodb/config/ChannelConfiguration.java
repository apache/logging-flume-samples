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

import java.util.HashMap;
import java.util.Map;

/** Class Description goes here. */
public class ChannelConfiguration {

    private String checkpointDir;
    private String dataDirs;
    private long minimumRequiredSpace;
    private long capacity;

    public String getCheckpointDir() {
        return checkpointDir;
    }

    public void setCheckpointDir(String checkpointDir) {
        this.checkpointDir = checkpointDir;
    }

    public String getDataDirs() {
        return dataDirs;
    }

    public void setDataDirs(String dataDirs) {
        this.dataDirs = dataDirs;
    }

    public long getMinimumRequiredSpace() {
        return minimumRequiredSpace;
    }

    public void setMinimumRequiredSpace(long minimumRequiredSpace) {
        this.minimumRequiredSpace = minimumRequiredSpace;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    Map<String, String> getProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("checkpointDir", checkpointDir);
        map.put("dataDirs", dataDirs);
        map.put("minimumRequiredSpace", Long.toString(minimumRequiredSpace));
        map.put("capacity", Long.toString(capacity));
        return map;
    }
}
