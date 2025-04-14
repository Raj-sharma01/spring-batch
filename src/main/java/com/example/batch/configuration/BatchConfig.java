package com.example.batch.configuration;

import com.example.batch.dto.MovieDTO;
import com.example.batch.entity.Movie;
import com.example.batch.processer.MovieProcessor;
import com.example.batch.reader.OMDbReader;
import com.example.batch.writer.MovieWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
//Used to mark a Java class as a source of bean definitions. The config class can have non-bean methods and variables.
// A spring boot project can have multiple config files (files with @Configuration)
public class BatchConfig {

    @Bean
//  used on a method to declare a Spring-managed bean.Spring calls the @Bean annotated methods when the application starts. It invokes the method, takes the returned object, and stores it in the ApplicationContext as a bean.
//  This happens only once (by default, because it's singleton (just like any Component))
//  we can use @Scope("prototype") with any bean and this will ensure that the bean will be recreated anytime it is required (or needed to be autowired)
//  📌 The method is not a bean. 📌 The object it returns is the actual Spring Bean.
//  while calling the method spring auto-injects the arguments
//  if multiple same arguments exist spring will through error (NoUniqueBeanDefinitionException) or you might need to use @Primary or @Qualifier annotations

// @StepScope is similar to @Scope("prototype") but it is batch-specific, giving iit access to step execution context, such as job parameters, and is tied to the lifecycle of a batch step.

//  in movieJob JobRepository is a Spring Batch interface used to manage job execution metadata — completely unrelated to your MatchRepository.
//  in movieJob step is the below defined fetchMoviesStep. since there is only one Step bean, Step step worked otherwise using Step fetchMoviesStep is recommended.
//  JobBuilderFactory and StepBuilderFactory both are deprecated.

//  JobBuilder - Used to create/configure a Job
//  JobLauncher - Used to start/trigger a Job (used in controller)

    public Job movieJob(JobRepository jobRepository, Step step){ //number and type of argument constant?
        return new JobBuilder("movieJob",jobRepository)
                .start(step)
                .build();
    }

    // TaskExecutor - It’s used to execute tasks (or in ther words steps) asynchronously, i.e., multithreading. It is done only with chunk-oriented steps.
    // TaskLauncher - It’s part of Spring Cloud Task. Not a part of spring batch.

    // A Tasklet is a simpler step model in Spring Batch.

    // Step can be only one of:
    // A Tasklet Step: uses a Tasklet.
    // A Chunk-oriented Step: uses reader, processor, writer.

    // TaskLet is Used for:
    // One-time logic (cleanup, file deletion, summary generation)
    // Calling external services
    // Running scripts or shell commands
    // Custom DB operations

    // A Step can have only one tasklet

    // how retry when task failed?

    // for Retry - Use retry configurations:

    // For Chunk-based:

    // .chunk(10)
    // .faultTolerant()
    // .retry(MyException.class)
    // .retryLimit(3)

    // For Tasklet:
    // You have to handle retry manually using RepeatTemplate or similar logic.

    @Bean
//  StepBuilder - Used to build/configure a Step inside a job.
//  Transaction Manager - A Transaction Manager in Spring is responsible for handling the begin, commit, and rollback of database transactions.
//  Steps are the one that does the actual work (job) thus only they need a Transaction Manager. (Job is just like a container for steps)
//  PlatformTransactionManager - PlatformTransactionManager is the core interface in Spring for managing transactions. It defines the basic methods: getTransaction(...), commit(...), rollback(...)
//  when you use PlatformTransactionManager transactionManager spring will look into the dependency in the classpath and Auto-configured an Implementation for it (in our case JpaTransactionManager as we are using Spring Data JPA)
//  DataSourceTransactionManager - This is a concrete implementation of PlatformTransactionManager that is used when you're working with JDBC
//  when you use DataSourceTransactionManager transactionManager You're asking Spring to inject that specific implementation, not just any PlatformTransactionManager so spring will not auto configure anything and will ask you to explicitly declared a @Bean of type DataSourceTransactionManager
    public Step fetchMoviesStep(JobRepository jobRepository,
                                PlatformTransactionManager transactionManager,
                                OMDbReader reader,
                                MovieProcessor processor,
                                MovieWriter writer){
        // Reader reads 10 records one by one.
        // Each MovieDTO is passed individually to the processor.
        // After 10 processed items are collected → all 10 are passed at once to the writer (as a list of Entity).
        // the writer write these Entities to the DB all at once
        // Then, Spring uses the transactionManager to commit the transaction for all 10 records as a unit.
        return new StepBuilder("fetchMoviesStep", jobRepository)
                .<MovieDTO, Movie>chunk(10,transactionManager) //Reader must return MovieDTO; Processor must take MovieDTO and return Movie;Writer must accept Movie
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

        // what is the argument type of these reader, processor and writer?
    }

}
