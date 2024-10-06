package com.cnpm.demo.model.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClockInController {
    @GetMapping("/clockin_page")
    public ModelAndView clockinPage() {
        return new ModelAndView("clockin_page"); // Trả về view home_page.html
    }
     @PostMapping("/clockin")
    public ResponseEntity<String> clockin() {
        String url = "http://localhost:5000/clockin";  // URL của API Flask

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        return response;
    }

    @PostMapping("/clockout")
    public ResponseEntity<String> clockout() {
        String url = "http://localhost:5000/clockout";  // URL của API Flask

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        return response;
    }
}
