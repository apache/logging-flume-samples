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
import org.apache.flume.conf.internal.SuppressFBWarnings;
import org.apache.flume.sink.mongodb.MongoDbSinkConstants;

/** Class Description goes here. */
public class MongoDbSinkConfiguration {

    private String uri;
    private String database;
    private String collection;
    private String collectionHeader;
    private Map<String, String> collectionMap;
    private String writeConcern;
    private String includeHeaders;
    private String collectionMapFallback;
    private String batchSize;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getCollectionHeader() {
        return collectionHeader;
    }

    public void setCollectionHeader(String collectionHeader) {
        this.collectionHeader = collectionHeader;
    }

    @SuppressFBWarnings(value = "EI_EXPOSE_REP")
    public Map<String, String> getCollectionMap() {
        return collectionMap;
    }

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2")
    public void setCollectionMap(Map<String, String> collectionMap) {
        this.collectionMap = collectionMap;
    }

    public String getWriteConcern() {
        return writeConcern;
    }

    public void setWriteConcern(String writeConcern) {
        this.writeConcern = writeConcern;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public String getIncludeHeaders() {
        return includeHeaders;
    }

    public void setIncludeHeaders(String includeHeaders) {
        this.includeHeaders = includeHeaders;
    }

    public String getCollectionMapFallback() {
        return collectionMapFallback;
    }

    public void setCollectionMapFallback(String collectionMapFallback) {
        this.collectionMapFallback = collectionMapFallback;
    }

    public Map<String, String> getProperties() {
        Map<String, String> map = new HashMap<>();
        if (uri != null) {
            map.put(MongoDbSinkConstants.CONNECTION_URI, uri);
        }
        if (database != null) {
            map.put(MongoDbSinkConstants.DATABASE_NAME, database);
        }
        if (collection != null) {
            map.put(MongoDbSinkConstants.COLLECTION, collection);
        }
        if (collectionHeader != null) {
            map.put(MongoDbSinkConstants.COLLECTION_HEADER, collectionHeader);
        }
        if (collectionMap != null) {
            for (Map.Entry<String, String> entry : collectionMap.entrySet()) {
                map.put(MongoDbSinkConstants.COLLECTION_MAP_PREFIX + entry.getKey(), entry.getValue());
            }
        }
        if (writeConcern != null) {
            map.put(MongoDbSinkConstants.WRITE_CONCERN, writeConcern);
        }
        if (batchSize != null) {
            map.put(MongoDbSinkConstants.BATCH_SIZE, batchSize);
        }
        if (includeHeaders != null) {
            map.put(MongoDbSinkConstants.INCLUDE_HEADERS, includeHeaders);
        }
        if (collectionMapFallback != null) {
            map.put(MongoDbSinkConstants.COLLECTION_MAP_FALLBACK, collectionMapFallback);
        }

        return map;
    }
}
