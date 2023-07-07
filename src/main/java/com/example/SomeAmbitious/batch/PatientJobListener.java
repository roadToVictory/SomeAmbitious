package com.example.SomeAmbitious.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.lang.Nullable;

@Slf4j
public class PatientJobListener implements JobExecutionListener {

    @Override
    @Nullable
    public void beforeJob(final JobExecution jobExecution) {
        log.info("-----> Job has started <-----");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            log.info("-----> Job has ended successfully <-----");
        } else {
            log.info("-----> Job has ended with error <-----");
        }
    }
}
