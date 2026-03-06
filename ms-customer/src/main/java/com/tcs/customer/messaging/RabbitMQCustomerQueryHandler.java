package com.tcs.customer.messaging;

import com.tcs.customer.config.RabbitMQConfig;
import com.tcs.customer.dto.CustomerResponseDto;
import com.tcs.customer.mapper.CustomerMapper;
import com.tcs.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQCustomerQueryHandler implements CustomerQueryHandler {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @RabbitListener(queues = RabbitMQConfig.CUSTOMER_QUEUE)
    @Override
    public CustomerResponseDto findCustomer(Long customerId) {
        log.debug("RabbitMQ request received for customerId: {}", customerId);
        return customerRepository.findById(customerId)
                .map(customerMapper::toDto)
                .orElse(null);
    }
}
