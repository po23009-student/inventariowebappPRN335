package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.conversores;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@FacesConverter(value = "offsetDateTimeConverter", managed = true)
public class OffsetDateTimeConverter implements Converter<OffsetDateTime> {

    private static final DateTimeFormatter FMT_WITH_OFFSET =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmXXX");

    private static final DateTimeFormatter FMT_NO_OFFSET =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("America/El_Salvador");

    @Override
    public OffsetDateTime getAsObject(FacesContext ctx, UIComponent comp, String value) {
        if (value == null || value.isBlank()) return null;

        String v = value.trim().replaceFirst("T\\s+", "T");
        try {
            if (v.matches(".*[+-]\\d{2}:\\d{2}$")) {
                return OffsetDateTime.parse(v, FMT_WITH_OFFSET);
            }
            LocalDateTime ldt = LocalDateTime.parse(v, FMT_NO_OFFSET);
            return ldt.atZone(DEFAULT_ZONE).toOffsetDateTime();
        } catch (DateTimeParseException e) {
            throw new ConverterException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fecha inválida",
                            "Usa 2025-10-14T13:45 (o con offset: 2025-10-14T13:45-06:00)"));
        }
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, OffsetDateTime value) {
        if (value == null) return "";
        return value.atZoneSameInstant(DEFAULT_ZONE).toLocalDateTime().format(FMT_NO_OFFSET);
    }
}