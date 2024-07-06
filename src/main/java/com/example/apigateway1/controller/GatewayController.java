package com.example.apigateway1.controller;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apigateway1.service.GatewayService;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

    @Autowired
    private GatewayService gatewayService;

    @PostMapping(value = "/authorize", consumes = "text/plain", produces = "text/plain")
    public ResponseEntity<String> authorize(@RequestBody String base64Data) {
        byte[] byteArray = Base64.getDecoder().decode(base64Data);
        logger.info("Datos decodificados en bytes en Apigateway1: {}", new String(byteArray));

        byte[] responseBytes = gatewayService.sendToAuthorization(byteArray);

        String base64Response = Base64.getEncoder().encodeToString(responseBytes);
        return ResponseEntity.ok(base64Response);
    }

    @PostMapping(value = "/receiveIso", consumes = "application/octet-stream", produces = "application/octet-stream")
    public ResponseEntity<byte[]> receiveIso(@RequestBody byte[] byteArray) {
        logger.info("ISO 8583 recibido en Apigateway1: {}", new String(byteArray));
        return ResponseEntity.ok(byteArray);
    }

    @PostMapping(value = "/sendIso", consumes = "application/octet-stream", produces = "application/octet-stream")
    public ResponseEntity<byte[]> sendIso(@RequestBody byte[] isoMessage) {
        String externalServiceUrl = "http://25.11.90.194:5132/api/Autorizacion";
        byte[] responseBytes = gatewayService.sendToExternalService(isoMessage, externalServiceUrl);
        return ResponseEntity.ok(responseBytes);
    }
    
}
