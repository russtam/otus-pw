package ru.rustam.otus.common.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rustam.otus.common.model.ClientMessageDto;
import ru.rustam.otus.common.service.ClientMessageService;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientMessageServiceImpl implements ClientMessageService {

    @Value("${clientMessagingServiceHost:http://otuspw-client-messaging-service-svc:8000}")
    private String CLIENT_MESSAGING_SERVICE_URL;
    private final RestTemplate restTemplate;

    @Override
    public List<ClientMessageDto> getAllClientMessages(String username) {
        ParameterizedTypeReference<List<ClientMessageDto>> typeRef =
                new ParameterizedTypeReference<>() {
                };
        return restTemplate.exchange(CLIENT_MESSAGING_SERVICE_URL + "/messages/{userName}",
                HttpMethod.GET, HttpEntity.EMPTY, typeRef, Map.of("userName", username)).getBody();
    }

}
