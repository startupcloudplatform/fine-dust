package com.crossent.msa.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class RequestService {

    @LoadBalanced
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gateway.basic.user:}")
    String user;

    @Value("${gateway.basic.password:}")
    String password;

    private HttpHeaders getHeaders(){
        String basicAuth = String.format("%s:%s", user, password);
        String base64Auth = Base64Utils.encodeToString(basicAuth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Basic %s", base64Auth));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public ResponseEntity<Object> getHttpAsUri(URI uri) {
        ResponseEntity<Object> result = null;
        try{
            HttpEntity<?> request = new HttpEntity<>(getHeaders());
            result = restTemplate.exchange(uri, HttpMethod.GET, request, Object.class);
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
