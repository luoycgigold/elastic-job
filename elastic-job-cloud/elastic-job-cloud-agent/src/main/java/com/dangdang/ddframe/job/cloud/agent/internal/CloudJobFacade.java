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

package com.dangdang.ddframe.job.cloud.agent.internal;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.config.JobRootConfiguration;
import com.dangdang.ddframe.job.api.exception.JobExecutionEnvironmentException;
import com.dangdang.ddframe.job.api.executor.JobFacade;
import com.dangdang.ddframe.job.api.type.dataflow.api.DataflowJobConfiguration;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * 为调度器提供内部服务的门面类.
 * 
 * @author zhangliang
 */
@RequiredArgsConstructor
public class CloudJobFacade implements JobFacade {
    
    private final ShardingContext shardingContext;
    
    private final JobConfigurationContext jobConfig;
    
    @Override
    public JobRootConfiguration loadJobRootConfiguration(final boolean fromCache) {
        return jobConfig;
    }
    
    @Override
    public void checkJobExecutionEnvironment() throws JobExecutionEnvironmentException {
    }
    
    @Override
    public void failoverIfNecessary() {
    }
    
    @Override
    public void registerJobBegin(final ShardingContext shardingContext) {
    }
    
    @Override
    public void registerJobCompleted(final ShardingContext shardingContext) {
    }
    
    @Override
    public ShardingContext getShardingContext() {
        return shardingContext;
    }
    
    @Override
    public boolean misfireIfNecessary(final Collection<Integer> shardingItems) {
        return false;
    }
    
    @Override
    public void clearMisfire(final Collection<Integer> shardingItems) {
    }
    
    @Override
    public boolean isExecuteMisfired(final Collection<Integer> shardingItems) {
        return false;
    }
    
    @Override
    public boolean isEligibleForJobRunning() {
        return jobConfig.getTypeConfig() instanceof DataflowJobConfiguration && ((DataflowJobConfiguration) jobConfig.getTypeConfig()).isStreamingProcess();
    }
    
    @Override
    public boolean isNeedSharding() {
        return false;
    }
    
    @Override
    public void cleanPreviousExecutionInfo() {
    }
    
    @Override
    public void beforeJobExecuted(final ShardingContext shardingContext) {
    }
    
    @Override
    public void afterJobExecuted(final ShardingContext shardingContext) {
    }
}
