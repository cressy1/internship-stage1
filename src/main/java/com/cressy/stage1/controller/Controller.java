package com.cressy.stage1.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class Controller {
    @Value("${weather.api.key}")
    private String weatherApiKey;

    @GetMapping("/internship")
    public Map<String, String> getClientInfo(@RequestHeader(value = "X-Forwarded-For", required = false) String clientIp) {

        RestTemplate restTemplate = new RestTemplate();

        String ipUrl = String.format("https://ipinfo.io/%s/json", clientIp);
        String ipResponse = restTemplate.getForObject(ipUrl, String.class);
        JSONObject ipJson = new JSONObject(ipResponse);
        String ip = ipJson.getString("ip");

        String locationUrl = String.format("https://ipinfo.io/%s/json", clientIp);
        String locationResponse = restTemplate.getForObject(locationUrl, String.class);
        JSONObject locationJson = new JSONObject(locationResponse);
        String city = locationJson.getString("city");


        String weatherUrl = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric", city, weatherApiKey);
        String weatherResponse = restTemplate.getForObject(weatherUrl, String.class);
        JSONObject weatherJson = new JSONObject(weatherResponse);
        String temp = weatherJson.getJSONObject("main").get("temp").toString();

        String greeting = String.format("Hello, Mike the temperature is %s degrees Celsius in %s", temp, city);

        return Map.of(
                "client_ip", ip,
                "location", city,
                "greeting", greeting
        );
    }

}
