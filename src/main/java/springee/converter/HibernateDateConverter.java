package springee.converter;

import javax.persistence.AttributeConverter;
import java.sql.Date;
import java.time.LocalDate;

public class HibernateDateConverter implements AttributeConverter<LocalDate, Date> {
  @Override
  public Date convertToDatabaseColumn(LocalDate attribute) {
    if (attribute == null) {
      return null;
    }
    return Date.valueOf(attribute);
  }

  @Override
  public LocalDate convertToEntityAttribute(Date dbData) {
    if (dbData == null) {
      return null;
    }
    return dbData.toLocalDate();
  }
}
