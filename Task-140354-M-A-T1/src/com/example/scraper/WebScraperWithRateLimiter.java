package com.example.scraper;

import com.google.common.util.concurrent.RateLimiter;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class WebScraperWithRateLimiter {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Create a RateLimiter with a rate of 1 request per second (adjust the rate as needed)
        final RateLimiter rateLimiter = RateLimiter.create(1.0);

        String urlToScrape = "https://example.com"; // Replace this with the URL you want to scrape

        scrapeWebPage(urlToScrape, rateLimiter);
    }

    public static void scrapeWebPage(String url, RateLimiter rateLimiter) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        while (true) { // Loop for multiple requests if needed
            // Acquire a permit before making the request to limit the rate
            rateLimiter.acquire();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                // Process the scraped data here
                System.out.println("Scraped data: " + responseBody.length());
            } else {
                System.err.println("Request failed with status: " + response.statusCode());
            }

            // Add a delay between requests if necessary to avoid overwhelming the server
            Thread.sleep(Duration.ofSeconds(1).toMillis());
        }
    }
}

