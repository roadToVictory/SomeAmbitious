package com.example.SomeAmbitious.batch;

import com.example.SomeAmbitious.records.Patient;
import com.example.SomeAmbitious.records.PatientRow;
import com.example.SomeAmbitious.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Slf4j
public class PatientItemProcessor implements ItemProcessor<PatientRow, Patient> {

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
}
