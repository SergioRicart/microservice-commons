package com.sergioricart.notification_service.notification.infrastructure.event.consumer;

import com.sergioricart.commons.infrastructure.event.consumer.EventSpecificConsumer;
import com.sergioricart.commons.infrastructure.event.util.MessagingUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Component
public abstract class KafkaEventConsumer implements Consumer<Message<GenericRecord>> {

    private final MessagingUtil messagingUtil;

    private final Map<String, EventSpecificConsumer<SpecificRecord>> eventSpecificConsummerMap;

    public KafkaEventConsumer(List<EventSpecificConsumer> specificConsummers, MessagingUtil messagingUtil){

        eventSpecificConsummerMap = specificConsummers.stream().collect(
                Collectors.toMap(
                        EventSpecificConsumer::getSchema,
                        consumer -> consumer
                )
        );

        this.messagingUtil = messagingUtil;

    };

    @Override
    @SneakyThrows
    public void accept(Message<GenericRecord> genericRecordMessage) {

        log.info("Received Message: {}", genericRecordMessage.toString());

        SpecificRecord specificRecord = messagingUtil.getSpecificRecord(genericRecordMessage.getPayload());

        String schemaFullName = specificRecord.getClass().getTypeName();

        EventSpecificConsumer<SpecificRecord> specificConsummer = eventSpecificConsummerMap.get(schemaFullName);

        if (specificConsummer != null) {

            Message<SpecificRecord> specificRecordMessage = messagingUtil.buildMessage(
                    (SpecificRecord) genericRecordMessage.getPayload(),
                    genericRecordMessage.getHeaders()
            );

            log.info("Sending specific record: {}", specificRecordMessage.toString());

            specificConsummer.accept(specificRecordMessage);

        }else  {
            log.warn("No consumer for schema {}", schemaFullName);
        }

    }

    public abstract void listen(Message<GenericRecord> message);


}
