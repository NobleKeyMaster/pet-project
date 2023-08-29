package com.example.petproject.service;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PhotoService {

    @Value("${cloud.aws.url}")
    private String url;

    @Value("${cloud.aws.bucket_name}")
    private String bucketName;

    public ResponseEntity<String> uploadPhoto(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        try {
            body.add("file", file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        String serverUrl = url + bucketName + file.getOriginalFilename();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.ACCEPTED);
        restTemplate
                .put(serverUrl, requestEntity, String.class);

        return response;
    }

}