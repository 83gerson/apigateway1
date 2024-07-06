package com.example.apigateway1.service;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayService.class);

    @Autowired
    private RestTemplate restTemplate;

    public byte[] sendToAuthorization(byte[] byteArray) {
        String authorizationServiceUrl = "http://localhost:8082/api/authorization/authorize";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(byteArray, headers);
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                authorizationServiceUrl,
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        return responseEntity.getBody();
    }

    public byte[] sendToExternalService(byte[] isoMessage, String externalServiceUrl) {
        logger.info("Enviando mensaje ISO 8583 a servicio externo");
    
        String isoBase64 = Base64.getEncoder().encodeToString(isoMessage);
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);  
    
        // Crea la entidad ISO 8583 en Base64
        HttpEntity<String> requestEntity = new HttpEntity<>(isoBase64, headers);
    
        // Envía
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                externalServiceUrl,
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );
    
        // Convierte la respuesta a bytes
        byte[] responseBytes = responseEntity.getBody();
    
        return responseBytes;
    }
    
}
