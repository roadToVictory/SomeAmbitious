package com.example.SomeAmbitious.records;

import java.time.LocalDate;

public record Patient(
        Integer personalNumber,
        String firstName,
        String lastName,
        boolean insured,
        LocalDate registered
) { }
