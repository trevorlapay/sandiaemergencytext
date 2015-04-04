package gov.sandia.cron;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
/**
 * This kicks off the tweet job.
 * @author Administrator
 *
 */
public class TweetScheduler {


	public static void main(String[] args) throws Exception {

		JobDetail job = JobBuilder.newJob(gov.sandia.cron.TweetJob.class)
			.withIdentity("sandiaTweetJob", "group1").build();
 
		Trigger trigger = TriggerBuilder
			.newTrigger()
			.withIdentity("sandiaTweetTrigger", "group1")
			.withSchedule(
				SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(600).repeatForever())
			.build();
 
		// schedule it
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(job, trigger);
 
	}
}
