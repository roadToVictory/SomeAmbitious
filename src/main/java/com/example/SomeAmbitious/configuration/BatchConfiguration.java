package com.example.SomeAmbitious.configuration;

import com.example.SomeAmbitious.batch.PatientItemProcessor;
import com.example.SomeAmbitious.batch.PatientJobListener;
import com.example.SomeAmbitious.records.Patient;
import com.example.SomeAmbitious.records.PatientRow;
import com.example.SomeAmbitious.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.RecordFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

//    @Autowired
//    private JobBuilder jobBuilder;
//
//    @Autowired
//    private StepBuilder stepBuilder;
//
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    JobLauncher jobLauncher;

    @Bean
    public FlatFileItemReader<PatientRow> reader() {
        return new FlatFileItemReaderBuilder<PatientRow>()
                .name("patientItemReader")
                .resource(new ClassPathResource("patients.csv"))
                .delimited()
                .names("personalNumber", "firstName", "lastName")
                .fieldSetMapper(new RecordFieldSetMapper<>(PatientRow.class))
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Patient> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Patient>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO patient " +
                        "(personal_number, first_name, last_name, insured, admitted_at) " +
                        "VALUES " +
                        "(:personalNumber, :firstName, :lastName, :insured, :registered)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public PatientService patientService() {
        return new PatientService();
    }

    @Bean
    public PatientJobListener listener() {
        return new PatientJobListener();
    }

    @Bean
    public PatientItemProcessor processor(PatientService patientService) {
        return new PatientItemProcessor(patientService);
    }

    @Bean
    public Step step(
            FlatFileItemReader<PatientRow> reader,
            PatientItemProcessor processor,
            JdbcBatchItemWriter<Patient> writer,
            JobRepository jobRepository
    ) {
        return new StepBuilder("myStep", jobRepository)
                .<PatientRow, Patient>chunk(2, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean("myJob")
    public Job myJob(PatientJobListener patientJobListener, Step step, JobRepository jobRepository) {
        log.error(step.getName() + " myJob");

        return new JobBuilder("myJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(patientJobListener)
                .flow(step)
                .end()
                .build();
    }

    @Bean("job")
    public Job job(PatientJobListener patientJobListener, Step step, JobRepository jobRepository) {
        log.error(step.getName() + " job");
        return new JobBuilder("job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(patientJobListener)
                .flow(step)
                .end()
                .build();
    }

//    @Bean
//    public JobLauncher jobLauncher() throws Exception {
//        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
//        jobLauncher.setJobRepository(jobRepository);
//        jobLauncher.afterPropertiesSet();
//        return jobLauncher;
//    }

}
