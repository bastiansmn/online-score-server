package com.bastiansmn.scoreserver;

import com.bastiansmn.scoreserver.config.RedirectionProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@SpringBootApplication
@Slf4j
@RestController
@RequiredArgsConstructor
public class ScoreServerApplication {

    private final RedirectionProperties redirectionProperties;

	public static void main(String[] args) {
		SpringApplication.run(ScoreServerApplication.class, args);
	}

    // Redirecting to the swagger ui
    @GetMapping("/")
    public RedirectView index() {
        return new RedirectView(redirectionProperties.getUrl());
    }

}
