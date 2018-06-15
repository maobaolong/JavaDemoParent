package net.mbl.demo.concurrentdemo.job;

import org.eclipse.core.internal.jobs.JobManager;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;

/**
 * Created by mbl on 20/03/2018.
 */
public class JobDemo {
  static IProgressMonitor rootMonitor = new NullProgressMonitor();
  static SubProgressMonitor monitor = new SubProgressMonitor(rootMonitor, 60000);
  static class MYJob extends Job {
    public MYJob() {
      super("MY Job");
    }

    public IStatus run(IProgressMonitor monitor) {
      monitor.beginTask("MYJob", 60000);
      for (int i = 0; i < 60000; i++) {
        System.out.println("This is a MYJob");
        monitor.worked(1);
      }
      monitor.done();
      return Status.OK_STATUS;
    }
  }
  static class MyProgressProvider extends ProgressProvider {

    @Override
    public IProgressMonitor createMonitor(Job job) {
      return monitor;
    }
  }
  public static void main(String[] args) throws InterruptedException {

    IJobManager jm = Job.getJobManager();
    IProgressMonitor myGroup = jm.createProgressGroup();
    myGroup.beginTask("aaaa", 1800000);
    Job job = new MYJob();
    job.setProgressGroup(myGroup, 600000);
    job.schedule();
    while (true) {
      Thread.sleep(1000);
    }

//    job.getThread().join();
//    Thread.sleep(100000);
  }
}
