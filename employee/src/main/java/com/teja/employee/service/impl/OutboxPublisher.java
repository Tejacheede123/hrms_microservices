package com.teja.employee.service.impl;

import com.teja.employee.entity.OutboxEvent;
import com.teja.employee.repository.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {
    private final OutboxEventRepository outboxRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {

        var events =
                outboxRepository.findTop50ByPublishedFalseOrderByIdAsc();

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(
                        event.getTopic(),
                        event.getEventKey(),
                        event.getPayload()
                ).get();

                markAsPublished(event.getId());

                log.info("Outbox event published: {}",
                        event.getEventId());

            } catch (Exception ex) {
                log.error(
                        "Failed to publish outbox event {}. It will be retried.",
                        event.getEventId(),
                        ex
                );
            }
        }
    }

    @Transactional
    public void markAsPublished(Long id) {

        OutboxEvent event = outboxRepository.findById(id)
                .orElseThrow();

        event.setPublished(true);
        event.setPublishedAt(Instant.now());

        outboxRepository.save(event);
    }
}
