package com.highcrit.ffacheckers.api.services;

import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, String> {
  @Override
  public String convertToDatabaseColumn(UUID attribute) {
    return attribute.toString();
  }

  @Override
  public UUID convertToEntityAttribute(String dbData) {
    return UUID.fromString(dbData);
  }
}
