package ch.hearc.nde.pulsaryapi.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ChronoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime start;
    private LocalDateTime end;
    private String name;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = true)
    private ProjectEntity project;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @PrePersist
    private void onCreate() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updated_at = LocalDateTime.now();
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    @Override
    public String toString() {
        return "ChronoEntity{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", name='" + name + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", user=" + user +
                '}';
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }
}
