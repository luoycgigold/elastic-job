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

import com.dangdang.ddframe.job.event.JobExecutionEvent.ExecutionSource;
import com.dangdang.ddframe.job.event.JobTraceEvent.LogLevel;
import com.dangdang.ddframe.job.event.fixture.Caller;
import com.dangdang.ddframe.job.event.fixture.TestJobEvenListener;
import com.dangdang.ddframe.job.event.log.JobLogEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobRdbEventConfiguration;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class JobEventBusTest {
    
    @Mock
    private Caller caller;
    
    private JobEventConfiguration logEventConfig = new JobLogEventConfiguration();
    
    private JobEventConfiguration rdbEventConfig = new JobRdbEventConfiguration(org.h2.Driver.class.getName(), "jdbc:h2:mem:job_event_bus", "sa", "", LogLevel.INFO);
    
    private JobEventBus jobEventBus = JobEventBus.getInstance();
    
    @Before
    public void setUp() {
        jobEventBus.register(new JobEventConfiguration[]{logEventConfig, rdbEventConfig});
    }
    
    @After
    public void tearDown() {
        jobEventBus.clearListeners();
    }
    
    @Test
    public void assertPostWithoutListenerRegistered() {
        jobEventBus.post(new JobTraceEvent("test_job", LogLevel.INFO, "ok"));
        jobEventBus.post(new JobExecutionEvent("test_job", ExecutionSource.NORMAL_TRIGGER, Arrays.asList(0, 1)));
        verify(caller, times(0)).call();
    }
    
    @Test
    public void assertPostWithListenerRegistered() {
        jobEventBus.register(new TestJobEvenListener(caller));
        jobEventBus.post(new JobTraceEvent("test_job", LogLevel.INFO, "ok"));
        jobEventBus.post(new JobExecutionEvent("test_job", ExecutionSource.NORMAL_TRIGGER, Arrays.asList(0, 1)));
        verify(caller, times(2)).call();
    }
    
    @Test
    public void assertPostWithListenerRegisteredTwice() {
        jobEventBus.register(new TestJobEvenListener(caller));
        jobEventBus.register(new TestJobEvenListener(caller));
        jobEventBus.post(new JobTraceEvent("test_job", LogLevel.INFO, "ok"));
        jobEventBus.post(new JobExecutionEvent("test_job", ExecutionSource.NORMAL_TRIGGER, Arrays.asList(0, 1)));
        verify(caller, times(2)).call();
    }
}
