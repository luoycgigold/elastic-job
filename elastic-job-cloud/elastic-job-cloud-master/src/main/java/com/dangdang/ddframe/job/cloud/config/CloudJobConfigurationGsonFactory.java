/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.job.cloud.config;

import com.dangdang.ddframe.job.api.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.api.config.impl.AbstractJobConfigurationGsonTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

/**
 * Cloud作业配置的Gson工厂.
 *
 * @author zhangliang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CloudJobConfigurationGsonFactory {
    
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(CloudJobConfiguration.class, new CloudJobConfigurationGsonTypeAdapter()).create();
    
    /**
     * 将作业配置转换为JSON字符串.
     * 
     * @param cloudJobConfig 作业配置对象
     * @return 作业配置JSON字符串
     */
    public static String toJson(final CloudJobConfiguration cloudJobConfig) {
        return GSON.toJson(cloudJobConfig);
    }
    
    /**
     * 将JSON字符串转换为作业配置.
     *
     * @param cloudJobConfigJson 作业配置JSON字符串
     * @return 作业配置对象
     */
    public static CloudJobConfiguration fromJson(final String cloudJobConfigJson) {
        return GSON.fromJson(cloudJobConfigJson, CloudJobConfiguration.class);
    }
    
    /**
     * Cloud作业配置的Json转换适配器.
     *
     * @author zhangliang
     */
    public static final class CloudJobConfigurationGsonTypeAdapter extends AbstractJobConfigurationGsonTypeAdapter<CloudJobConfiguration> {
        
        @Override
        protected void addToCustomizedValueMap(final String jsonName, final JsonReader in, final Map<String, Object> customizedValueMap) throws IOException {
            switch (jsonName) {
                case "cpuCount":
                    customizedValueMap.put("cpuCount", in.nextDouble());
                    break;
                case "memoryMB":
                    customizedValueMap.put("memoryMB", in.nextDouble());
                    break;
                case "dockerImageName":
                    customizedValueMap.put("dockerImageName", in.nextString());
                    break;
                case "appURL":
                    customizedValueMap.put("appURL", in.nextString());
                    break;
                case "bootstrapScript":
                    customizedValueMap.put("bootstrapScript", in.nextString());
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        
        @Override
        protected CloudJobConfiguration getJobRootConfiguration(final JobTypeConfiguration typeConfig, final Map<String, Object> customizedValueMap) {
            return new CloudJobConfiguration(typeConfig, (double) customizedValueMap.get("cpuCount"), (double) customizedValueMap.get("memoryMB"),
                    (String) customizedValueMap.get("dockerImageName"), (String) customizedValueMap.get("appURL"), (String) customizedValueMap.get("bootstrapScript"));
        }
        
        @Override
        protected void writeCustomized(final JsonWriter out, final CloudJobConfiguration value) throws IOException {
            out.name("cpuCount").value(value.getCpuCount());
            out.name("memoryMB").value(value.getMemoryMB());
            out.name("dockerImageName").value(value.getDockerImageName());
            out.name("appURL").value(value.getAppURL());
            out.name("bootstrapScript").value(value.getBootstrapScript());
        }
    }
}
