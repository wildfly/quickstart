package org.jboss.as.quickstarts.cluster.hasingleton.service.ejb;

import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple example to demonstrate a implementation of a clusterwide singleton timer.
 * 
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Singleton
public class SchedulerBean implements Scheduler {
    private static Logger LOGGER = LoggerFactory.getLogger(SchedulerBean.class);
    @Resource
    private TimerService timerService;
    
    @Timeout
    public void scheduler(Timer timer) {
        LOGGER.info("HASingletonTimer: Info="+timer.getInfo());
    }
    
    @Override
    public void initialize(String info) {
        ScheduleExpression sexpr = new ScheduleExpression();
        // set schedule to every 10 seconds for demonstration
        sexpr.hour("*").minute("*").second("0/10");
        // persistent must be false because the timer is started by the HASingleton service
        timerService.createCalendarTimer(sexpr,new TimerConfig(info, false));
    }
    
    @Override
    public void stop() {
        LOGGER.info("Stop all existing HASingleton timers");
        for (Timer timer : timerService.getTimers()) {
            LOGGER.trace("Stop HASingelton timer: "+timer.getInfo());
            timer.cancel();
        }
    }
}
