/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.ejb.timer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.ScheduleExpression;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import jakarta.ejb.TimerConfig;
import jakarta.ejb.TimerService;

/**
 * Demonstrates how to use the EJB's @Timeout.
 *
 * @author <a href="mailto:ozizka@redhat.com">Ondrej Zizka</a>
 * @author Paul Ferraro
 */
@Singleton
@Startup
public class TimeoutExample {

    @Resource
    private TimerService timerService;

    @Timeout
    public void timeout(Timer timer) {
        TimeoutHandler.INSTANCE.accept(timer);
    }

    @PostConstruct
    public void initialize() {
        // Set schedule to every 3 seconds (starting at second 0 of every minute).
        ScheduleExpression expression = new ScheduleExpression().hour("*").minute("*").second("0/3");
        this.timerService.createCalendarTimer(expression, new TimerConfig(this.getClass().getSimpleName(), false));
    }
}
