package com.bastiansmn.scoreserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@SpringBootApplication
@Slf4j
@RestController
public class ScoreServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScoreServerApplication.class, args);
	}

    // Redirecting to the swagger ui
    @GetMapping("/")
    public RedirectView index() {
        return new RedirectView("swagger-ui/index.html");
    }

}
