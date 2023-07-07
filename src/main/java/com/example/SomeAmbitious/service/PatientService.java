package com.example.SomeAmbitious.service;

import com.example.SomeAmbitious.records.PatientRow;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@NoArgsConstructor
public class PatientService {

    public boolean isInsured(PatientRow patientRow) {
        Random random = new Random();
        return  random.nextBoolean();
    }
}
