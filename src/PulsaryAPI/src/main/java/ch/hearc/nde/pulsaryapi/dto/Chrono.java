package ch.hearc.nde.pulsaryapi.dto;

import java.time.LocalDateTime;

public class Chrono {
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;

    public String getName() {
        return name;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
