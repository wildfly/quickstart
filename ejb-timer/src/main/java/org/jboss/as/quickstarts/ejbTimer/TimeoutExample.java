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
package org.jboss.as.quickstarts.ejbTimer;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

/**
 * Demonstrates how to use the EJB's @Timeout.
 *
 * @author <a href="mailto:ozizka@redhat.com">Ondrej Zizka</a>
 */
@Singleton
@Startup
public class TimeoutExample {

    @Resource
    private TimerService timerService;

    @Timeout
    public void scheduler(Timer timer) {
        Date currentTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        System.out.println("TimeoutExample.scheduler() " + timer.getInfo() + simpleDateFormat.format(currentTime));
    }

    @PostConstruct
    public void initialize() {
        ScheduleExpression se = new ScheduleExpression();
        // Set schedule to every 3 seconds (starting at second 0 of every minute).
        se.hour("*").minute("*").second("0/3");
        timerService.createCalendarTimer(se, new TimerConfig("EJB timer service timeout at ", false));
    }

    @PreDestroy
    public void stop() {
        System.out.println("EJB Timer: Stop timers.");
        for (Timer timer : timerService.getTimers()) {
            System.out.println("Stopping timer: " + timer.getInfo());
            timer.cancel();
        }
    }
}
