package com.sergioricart.commons.infrastructure.event.util;

import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper
public interface MapperUtils {

    default Instant epochMilliToInstant(Long millis) {
        return millis != null ? Instant.ofEpochMilli(millis) : null;
    }

    default String charSequenceToString(CharSequence charSequence) {
        return charSequence != null ? String.valueOf(charSequence) : "";
    }

    default CharSequence stringToCharSequence(String value) {
        return value;
    }

    default OffsetDateTime fromEpochMilli(Long epochMillis) {
        return epochMillis != null ? Instant.ofEpochMilli(epochMillis).atOffset(ZoneOffset.UTC) : null;
    }
}
