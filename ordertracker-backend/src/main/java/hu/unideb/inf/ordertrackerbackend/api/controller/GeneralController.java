package hu.unideb.inf.ordertrackerbackend.api.controller;

import hu.unideb.inf.ordertrackerbackend.api.dto.common.ExtendedResponse;
import hu.unideb.inf.ordertrackerbackend.api.model.WelcomeResponse;
import hu.unideb.inf.ordertrackerbackend.api.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/general/")
@CrossOrigin(origins = "http://localhost:4200")
public class GeneralController {

    @Autowired
    GeneralService generalService;

    @GetMapping("/welcome")
    public ResponseEntity<WelcomeResponse> getWelcome() {
        return generalService.getWelcome();
    }

    @PostMapping("/admin/set-welcome")
    public ResponseEntity<ExtendedResponse> setWelcome(@RequestBody WelcomeResponse welcomeDto) {
        return generalService.setWelcome(welcomeDto);
    }

}
