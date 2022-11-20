package hu.unideb.inf.ordertrackerbackend.api.service;

import hu.unideb.inf.ordertrackerbackend.api.dto.common.ExtendedResponse;
import hu.unideb.inf.ordertrackerbackend.api.model.WelcomeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class GeneralService {

    private final static String MOTD_FILE = "motd.txt";
    private final static String MOTD_IMAGE_FILE = "motd_image";

    public ResponseEntity<ExtendedResponse> setWelcome(WelcomeResponse welcomeDto) {
        try {
            File motdFile = new File(MOTD_FILE);
            motdFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(MOTD_FILE));
            writer.write(welcomeDto.getMessage());
            writer.close();

            motdFile = new File(MOTD_IMAGE_FILE);
            motdFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(MOTD_IMAGE_FILE);
            fos.write(welcomeDto.getImageData());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(new ExtendedResponse());
    }

    public ResponseEntity<WelcomeResponse> getWelcome() {
        WelcomeResponse welcomeDto = new WelcomeResponse();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(MOTD_FILE));
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                welcomeDto.appendMessage(line + System.lineSeparator());
                line = reader.readLine();
            }
            reader.close();

            FileInputStream fis = new FileInputStream(MOTD_IMAGE_FILE);
            welcomeDto.setImageData(fis.readAllBytes());
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            welcomeDto = new WelcomeResponse();
            welcomeDto.setMessage("Welcome! :)");

            return ResponseEntity.ok(welcomeDto);
        }

        return ResponseEntity.ok(welcomeDto);
    }

}
