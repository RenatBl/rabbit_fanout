package ru.itis.application.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import ru.itis.application.models.PersonalData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

public class PdfConverter {

    private final String path;

    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 24,
            Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.NORMAL);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    public PdfConverter(String path) {
        this.path = path;
    }

    @SneakyThrows
    public void createDocument(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        PersonalData personalData = objectMapper.readValue(data, PersonalData.class);
        switch (personalData.getDocumentType()) {
            case "dismissal":
                createDismissalDocument(personalData);
                break;
            case "reduction":
                createReductionDocument(personalData);
                break;
            case "academic_leave":
                createAcademicLeaveDocument(personalData);
                break;
            case "enrollment":
                createEnrollmentDocument(personalData);
                break;
            default:
                throw new IllegalArgumentException("Wrong data");
        }
    }

    private void createDismissalDocument(PersonalData personalData) throws DocumentException {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(path + "\\" +
                    personalData.getSurname() + "_" + personalData.getDocumentType() + ".pdf")));
            document.open();

            addMetadata(document, "Letter of dismissal " + personalData.getSurname(), personalData.getSurname());
            addDocumentInfo(document, "To general manager%LLC \"Horns and hooves\"%Pupkin I. V. % Leading " +
                    "engineer%" + personalData.getSurname() + " " + personalData.getName() + "%" +
                    personalData.getPatronymic());
            addTitle(document, "Letter of dismissal");

            Date issueDate = personalData.getIssueDate();
            addMainText(document, "I " + personalData.getSurname() + " " + personalData.getName() + " " +
                    personalData.getPatronymic() + " " + personalData.getAge() + " years old" + ". Passport: series " +
                    personalData.getPassportSeries() + ", number " + personalData.getPassportNumber() + " issued " +
                    issueDate.getDay() + "." + issueDate.getMonth() + "." + issueDate.getYear() + ". " +
                    "In accordance with article 80 of the Labor code of the Russian Federation, please dismiss me " +
                    "at your own request");
            Date filingDate = personalData.getFilingDate();
            addDateAndSignature(document, filingDate.getDay() + "." + filingDate.getMonth() + "." +
                    filingDate.getYear());
            document.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void createReductionDocument(PersonalData personalData) throws DocumentException {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(path + "\\" +
                    personalData.getSurname() + "_" + personalData.getDocumentType() + ".pdf")));
            document.open();

            addMetadata(document, "Application for reduction " + personalData.getSurname(),
                    personalData.getSurname());
            addDocumentInfo(document, "To the Director of % HSE ITIS%Abramsky M. M.%3rd year Student " +
                    personalData.getSurname() + "%" + personalData.getName() + " " + personalData.getPatronymic());
            addTitle(document, "Application for reduction");

            Date issueDate = personalData.getIssueDate();
            addMainText(document, "I " + personalData.getSurname() + " " + personalData.getName() + " " +
                    personalData.getPatronymic() + " " + personalData.getAge() + " years old" + ". Passport: series " +
                    personalData.getPassportSeries() + ", number " + personalData.getPassportNumber() + " issued " +
                    issueDate.getDay() + "." + issueDate.getMonth() + "." + issueDate.getYear() + ". " +
                    "Please expel me at your own request.");

            Date filingDate = personalData.getFilingDate();
            addDateAndSignature(document, filingDate.getDay() + "." + filingDate.getMonth() + "." +
                    filingDate.getYear());
            document.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void createAcademicLeaveDocument(PersonalData personalData) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(path + "\\" +
                    personalData.getSurname() + "_" + personalData.getDocumentType() + ".pdf")));
            document.open();

            addMetadata(document, "Application for academic leave " + personalData.getSurname(),
                    personalData.getSurname());
            addDocumentInfo(document, "To the Director of % HSE ITIS%Abramsky M. M.%3rd year Student " +
                    personalData.getSurname() + "%" + personalData.getName() + " " + personalData.getPatronymic());
            addTitle(document, "Application for academic leave");

            Date issueDate = personalData.getIssueDate();
            addMainText(document, "Я " + personalData.getSurname() + " " + personalData.getName() + " " +
                    personalData.getPatronymic() + " " + personalData.getAge() + " years old. " + "Passport: series " +
                    personalData.getPassportSeries() + " number " + personalData.getPassportNumber() + " issued " +
                    issueDate.getDay() + "." + issueDate.getMonth() + "." + issueDate.getYear() + ". " +
                    "Please grant me a 1-year sabbatical.");
            Date filingDate = personalData.getFilingDate();
            addDateAndSignature(document, filingDate.getDay() + "." + filingDate.getMonth() + "." +
                    filingDate.getYear());
            document.close();
        } catch (FileNotFoundException | DocumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void createEnrollmentDocument(PersonalData personalData) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(path + "\\" +
                    personalData.getSurname() + "_" + personalData.getDocumentType() + ".pdf")));
            document.open();

            addMetadata(document, "Application for enrollment " + personalData.getSurname(),
                    personalData.getSurname());
            addDocumentInfo(document, "To the Director of % HSE ITIS%To M. M. Abramsky.%" +
                    personalData.getSurname() + "%" + personalData.getName() + " " + personalData.getPatronymic());
            addTitle(document, "Application for enrollment");

            Date issueDate = personalData.getIssueDate();
            addMainText(document, "I " + personalData.getSurname() + " " + personalData.getName() + " " +
                    personalData.getPatronymic() + " " + personalData.getAge() + " years old. " + "Passport: series " +
                    personalData.getPassportSeries() + " number " + personalData.getPassportNumber() + " issued " +
                    issueDate.getDay() + "." + issueDate.getMonth() + "." + issueDate.getYear() + ". " +
                    "Please enroll me in the 2nd year of the higher school of ITIS");
            Date filingDate = personalData.getFilingDate();
            addDateAndSignature(document, filingDate.getDay() + "." + filingDate.getMonth() + "." +
                    filingDate.getYear());
            document.close();
        } catch (FileNotFoundException | DocumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void addMetadata(Document document, String title, String subject) {
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor("ITIS");
        document.addCreator("ITIS");
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private void addDocumentInfo(Document document, String text) throws DocumentException {
        String[] arr = text.split("%");
        for (String s : arr) {
            Paragraph paragraph = new Paragraph(s);
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            paragraph.setFont(smallBold);
            document.add(paragraph);
        }
    }

    private void addTitle(Document document, String text) throws DocumentException {
        Paragraph preface = new Paragraph(text, titleFont);
        preface.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(preface, 1);
        document.add(preface);
    }

    private void addMainText(Document document, String text) throws DocumentException {
        Paragraph preface = new Paragraph(text, subFont);
        preface.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(preface, 1);
        document.add(preface);
    }

    private void addDateAndSignature(Document document, String text) throws DocumentException {
        Paragraph preface = new Paragraph("Signature _______________________   " + text, smallBold);
        preface.setAlignment(Element.ALIGN_RIGHT);
        addEmptyLine(preface, 1);
        document.add(preface);
    }
}
