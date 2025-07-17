package cl.tenpo.valueObjects;

import java.time.LocalDate;

public class DateRange {
    private final LocalDate start;
    private final LocalDate end;

    public DateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public boolean includes(LocalDate date) {
        return (start == null || !date.isBefore(start)) && (end == null || !date.isAfter(end));
    }
}
