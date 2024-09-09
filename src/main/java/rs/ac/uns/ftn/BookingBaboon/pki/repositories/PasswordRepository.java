package rs.ac.uns.ftn.BookingBaboon.pki.repositories;

import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Repository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Repository
public class PasswordRepository {
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
            e.printStackTrace();
        }
        return null;
    }
}
