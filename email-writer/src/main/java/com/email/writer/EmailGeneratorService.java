package com.email.writer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;
    @Value("${gemini.api.url}")
    private String  geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;


    private int requestCounter = 0;
    private long startTime = System.currentTimeMillis();



    public EmailGeneratorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }



    public String generateEmailReply(EmailRequest emailRequest) {
        System.out.println("Service endpoint hit");

        long currentTime = System.currentTimeMillis();

        if(currentTime - startTime > 60000){
            System.out.println("Requests in last minute: " + requestCounter);
            requestCounter = 0;
            startTime = currentTime;
        }

        requestCounter++;
        System.out.println("Current request count in this minute: " + requestCounter);
        // Build the prompt
        String prompt = buildPrompt(emailRequest);
        //Craft a request
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );
        //Do request and get response
        //1. Remember - To do a request to external site - you need API key.
        try {
            String response = webClient.post()
                    .uri(geminiApiUrl + geminiApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            //Return the response.
            return extractResponseContent(response);
        }catch (WebClientResponseException.TooManyRequests e) {
            return "Gemini API rate limit exceeded. Please wait and try again.";
        } catch (WebClientResponseException e) {
            return "Gemini API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }


    private String extractResponseContent(String response) {
        try{
            ObjectMapper mapper = new ObjectMapper(); // Objectmapper is a tool from jackson library which is useful to work on Json data
            // Converts Json to java objects and wise versa
            JsonNode rootNode = mapper.readTree(response);
            // readTree - This also from jackson library which is used to convert the json object as a tree.

            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text").asText();
        }catch (Exception e){
            return "Error processing request:" + e.getMessage();
        }
    }





    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content. Please don't generate a subject line ");
        if(emailRequest.getTone() !=null && !emailRequest.getTone().isEmpty()){
           prompt.append("Use a ").append(emailRequest.getTone()).append(" tone");
        }
        prompt.append("\n Original Email: \n").append(emailRequest.getEmailContent());
        return prompt.toString();
    }
}
