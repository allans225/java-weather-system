package com.jweather;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.json.JSONObject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JWeatherApplication {
    private static final String API_BASE_URL = "https://api.weatherapi.com/v1/current.json";

    public static void main(String[] args) {
        SpringApplication.run(JWeatherApplication.class, args);
    }

    public static void getWeather(String city) {
        try {
            String climateData = getClimateData(city);
            JSONObject json = new JSONObject(climateData);

            // Verificação robusta de erro da API
            if (json.has("error")) {
                String errorMsg = json.getJSONObject("error").getString("message");
                System.out.println("Erro da API: " + errorMsg);
                return;
            }

            printClimateData(json); // Passe o JSONObject já criado para economizar processamento

        } catch (IllegalStateException e) {
            System.err.println("Erro de Configuração: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar dados: " + e.getMessage());
        }
    }

    // Imprime dados meteorológicos de forma organizada
    public static void printClimateData(JSONObject climateData) {
        // Dados originais em JSON obtidos do site meteorológico
        JSONObject weatherInformation = climateData.getJSONObject("current");
        JSONObject location = climateData.getJSONObject("location");

        // Extrai os dados da localização
        String city = location.getString("name");
        String country = location.getString("country");

        // Extrai dados adicionais
        String weatherCondition = weatherInformation.getJSONObject("condition").getString("text");
        int humidity = weatherInformation.getInt("humidity");
        float windKph = (float) weatherInformation.getDouble("wind_kph");
        float pressureMb = (float) weatherInformation.getDouble("pressure_mb");
        float thermalSensation = (float) weatherInformation.getDouble("feelslike_c");
        float currentTemperature = (float) weatherInformation.getDouble("temp_c");

        // Extrai data e hora da string retornada pela WeatherAPI
        String dateTimeString = weatherInformation.getString("last_updated");

        System.out.println("Informações Meteorológicas para " + city + ", " + country);
        System.out.println("Data e Hora: " + dateTimeString);
        System.out.println("Temperatura Atual: " + currentTemperature + "°C");
        System.out.println("Sensação Térmica: " + thermalSensation + "°C");
        System.out.println("Condição do Tempo: " + weatherCondition);
        System.out.println("Umidade: " + humidity + "%");
        System.out.println("Velocidade do Vento: " + windKph + " km/h");
        System.out.println("Pressão Atmosférica: " + pressureMb + " mb");
    }

    public static String getClimateData(String city) throws Exception {
        // Validação da Chave
        String apiKey = System.getenv("JWEATHER_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Erro: Variável de ambiente JWEATHER_KEY não configurada.");
        }

        // Formatação segura da URL (usando StandardCharsets)
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String urlWithKey = String.format("%s?key=%s&q=%s", API_BASE_URL, apiKey, encodedCity);

        // Configuração da Requisição
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithKey))
                .header("Accept", "application/json") // Boa prática: avisar que espera JSON
                .GET()
                .build();

        // Execução com HttpClient (Java 21+ permite o uso direto de try-with-resources)
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificação de Sucesso (Status 200)
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException("Falha na API: HTTP " + response.statusCode() + " - " + response.body());
            }
        }
    }

    public static String getCity() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome da Cidade: ");
        return scanner.nextLine();
    }
}
