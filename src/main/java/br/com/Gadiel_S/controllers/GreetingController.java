package br.com.Gadiel_S.controllers;

import br.com.Gadiel_S.models.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    // http://localhost:8080/greeting?name=Gadiel
    @RequestMapping("/greeting")
    public Greeting greeting(
            @RequestParam(value = "name", defaultValue = "word")
            String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

}
