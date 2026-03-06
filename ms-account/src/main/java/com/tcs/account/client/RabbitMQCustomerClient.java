package com.tcs.account.client;

import com.tcs.account.config.RabbitMQConfig;
import com.tcs.account.dto.client.CustomerClientResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQCustomerClient implements CustomerClient {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public CustomerClientResponseDto findCustomer(Long customerId) {
        return rabbitTemplate.convertSendAndReceiveAsType(
                RabbitMQConfig.CUSTOMER_QUEUE,
                customerId,
                new ParameterizedTypeReference<>() {}
        );
    }
}
