package com.example.SomeAmbitious.batch;

import com.example.SomeAmbitious.records.Patient;
import com.example.SomeAmbitious.records.PatientRow;
import com.example.SomeAmbitious.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Slf4j
public class PatientItemProcessor implements ItemProcessor<PatientRow, Patient>, StepExecutionListener {

//    @Autowired
    private final PatientService patientService;

    public PatientItemProcessor(final PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public Patient process(final PatientRow patientRow) {
        log.info("Processing patient of personal number: " + patientRow.personalNumber());

        boolean insured = patientService.isInsured(patientRow);

        return new Patient(
                patientRow.personalNumber(),
                patientRow.firstName(),
                patientRow.lastName(),
                insured,
                LocalDate.now()
        );
    }


    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("===Before Step===");
    }
    @Nullable
    @Override
    public  ExitStatus afterStep(StepExecution stepExecution) {
        log.info("===After Step===");
        return ExitStatus.COMPLETED;
    }
}
