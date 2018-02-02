/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.test;

import com.fens.desktopMonitor.bo.ConfiguracionBO;
import com.fens.desktopMonitor.cron.MonitorTimbradoJob;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.jdbc.ResourceManager;
import java.sql.Connection;
import java.util.Date;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author ISCesar
 */
public class TestMonitorTimbradoJob {
    
    Connection conn = Conexion.getConn();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        TestMonitorTimbradoJob test = new TestMonitorTimbradoJob();
        test.run();
    }
    
    public void run() throws Exception{
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        Date runTime = DateBuilder.evenMinuteDate(new Date());
        
        Connection conn = ResourceManager.getConnection();
        ConfiguracionBO configuracionBO = new ConfiguracionBO(this.conn,1);
        Configuracion configuracionDto = configuracionBO.getConfiguracion();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("configuracion_dto", configuracionDto);
        jobDataMap.put("conexion_bd", conn);
        jobDataMap.put("max_error_conn_pac", 3); //3 errores maximo de conexion a PAC
        
        JobDetail job = JobBuilder.newJob(MonitorTimbradoJob.class)
                .withIdentity("job1", "group1")
                .usingJobData(jobDataMap)
                .build();

        //Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();
        Trigger trigger2 = TriggerBuilder.newTrigger()
                                        .withIdentity("trigger2", "group1")
                                        .startNow()
                                        .withSchedule(simpleSchedule()
                                                .withIntervalInSeconds(5)
                                                .repeatForever())
                                        .build();

        sched.scheduleJob(job, trigger2);

        sched.start();

        //Probamos por x seg
        try
        {
          Thread.sleep(60000L);
        }
        catch (Exception e)
        {
        }

        sched.interrupt(job.getKey());
        sched.shutdown(true);
    }
    
}
