package com.example.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class JobController {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job movieJob;

    @PostMapping("/start-job")
    public ResponseEntity<String> startJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(movieJob, params);

        return ResponseEntity.ok("Job started");
    }
}
