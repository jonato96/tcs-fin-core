package com.tcs.account.rabbit;

import com.tcs.account.config.RabbitMQConfig;
import com.tcs.account.dto.CustomerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    public CustomerResponseDto findCustomer(Long customerId) {
        return rabbitTemplate.convertSendAndReceiveAsType(
                RabbitMQConfig.CUSTOMER_QUEUE,
                customerId,
                new ParameterizedTypeReference<>() {}
        );
    }
}
