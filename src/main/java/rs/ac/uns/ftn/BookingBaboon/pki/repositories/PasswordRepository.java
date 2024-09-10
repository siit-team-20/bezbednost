package rs.ac.uns.ftn.BookingBaboon.pki.repositories;

import java.io.FileReader;
import java.io.IOException;
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Repository
public class PasswordRepository {
    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());
    public static final String keyStorePasswordsFilePath = "src/main/resources/passwords/keyStorePasswords.csv";

    public String getPassword(String keyStoreName) {
        try {
            FileReader fileReader = new FileReader(keyStorePasswordsFilePath);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] entry;
            while (true) {
                entry = csvReader.readNext();
                if (entry == null) {
                    csvReader.close();
                    fileReader.close();
                    break;
                }

                if (entry[0].equals(keyStoreName)) {
                    csvReader.close();
                    fileReader.close();
                    return entry[1];
                }
            }
        }
        catch (CsvValidationException | IOException e) {
            String value = bundle.getString("certificate.internalError");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, value);
        }
        return null;
    }
}
