/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.quickstarts.ha.singleton;

import java.util.logging.Logger;

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
 * This class implements demonstration singleton deployment @Startup @Singleton which for the purposes of the quickstart
 * prints out hello world every 5 seconds. This simply illustrates which of the cluster members is currently running
 * the singleton.
 *
 * @author Radoslav Husar
 */
@Startup
@Singleton
public class SingletonTimer {

    private static final Logger LOGGER = Logger.getLogger(SingletonTimer.class.toString());

    @Resource
    private TimerService timerService;

    @PostConstruct
    public void initialize() {
        LOGGER.warning("SingletonTimer is initializing.");

        // For the demonstration just output a Hello World log every 5 seconds.
        ScheduleExpression scheduleExpression = new ScheduleExpression().hour("*").minute("*").second("0/5");

        // Persistent must be set to false (since it defaults to true) because the timer is not specific to this JVM.
        TimerConfig test = new TimerConfig("Hello World!", false);

        timerService.createCalendarTimer(scheduleExpression, test);
    }

    @Timeout
    public void scheduler(Timer timer) {
        LOGGER.info("SingletonTimer: " + timer.getInfo());
    }

    @PreDestroy
    public void stop() {
        LOGGER.warning("SingletonTimer is stopping: the server is either being shutdown or another node has " +
                "become elected to be the singleton master.");
    }

}