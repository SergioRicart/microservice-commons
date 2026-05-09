package com.sergioricart.commons.infrastructure.event.consumer;

import org.apache.avro.specific.SpecificRecord;
import org.springframework.messaging.Message;

public interface EventSpecificConsumer<T extends SpecificRecord> {

    void accept(Message<T> message);

    String getSchema();
}
