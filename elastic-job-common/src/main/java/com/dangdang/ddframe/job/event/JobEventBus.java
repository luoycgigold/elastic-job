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

package com.dangdang.ddframe.job.event;

import com.dangdang.ddframe.job.event.log.JobLogEventConfiguration;
import com.dangdang.ddframe.job.event.log.JobLogEventListener;
import com.dangdang.ddframe.job.event.rdb.JobRdbEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobRdbEventListener;
import com.google.common.eventbus.EventBus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 运行痕迹事件总线.
 * 
 * @author zhangliang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JobEventBus {
    
    private static volatile JobEventBus instance;
    
    private final EventBus eventBus = new EventBus();
    
    private final ConcurrentHashMap<String, JobEventListener> listeners = new ConcurrentHashMap<>();
    
    public static JobEventBus getInstance() {
        if (null == instance) {
            synchronized (JobEventBus.class) {
                if (null == instance) {
                    instance = new JobEventBus();
                }
            }
        }
        return instance;
    }
    
    public void register(final JobEventConfiguration[] jobEventConfigs) {
        for (JobEventConfiguration jobEventConfig : jobEventConfigs) {
            if (jobEventConfig instanceof JobRdbEventConfiguration) {
                instance.register(new JobRdbEventListener((JobRdbEventConfiguration) jobEventConfig));
            } else if (jobEventConfig instanceof JobLogEventConfiguration) {
                instance.register(new JobLogEventListener((JobLogEventConfiguration) jobEventConfig));
            }
        }
    }
    
    /**
     * 注册事件监听器.
     *
     * @param listener 监听器
     */
    public void register(final JobEventListener listener) {
        if (null == listeners.putIfAbsent(listener.getName(), listener)) {
            eventBus.register(listener);
        }
    }
    
    /**
     * 发布事件.
     *
     * @param event 事件
     */
    public void post(final Object event) {
        if (!listeners.isEmpty()) {
            eventBus.post(event);
        }
    }
    
    /**
     * 清除监听器.
     */
    public synchronized void clearListeners() {
        for (Object each : listeners.values()) {
            eventBus.unregister(each);
        }
        listeners.clear();
    }
}
