package ru.itis.application.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalData implements Serializable {

    private String name;
    private String surname;
    private String patronymic;
    private String age;
    private String passportSeries;
    private String passportNumber;
    private Date issueDate;
    private Date filingDate;

//    private String documentType;
}
