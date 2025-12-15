package com.example.dyeTrack.core.entity;

import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "idUser", "dayData" }))
public class DayDataOfUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDayData;

    @Column(name = "day_data", nullable = false)
    private LocalDate dayData;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = true)
    @JsonIgnore
    private User user;

    @OneToOne(mappedBy = "dataOfUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private PhysioTrack physioTrack;

    @Column(nullable = false)
    private Instant lastUpdate;

    public PhysioTrack getPhysioTrack() {
        return physioTrack;
    }

    public void setPhysioTrack(PhysioTrack physioTrack) {
        this.physioTrack = physioTrack;
    }

    @OneToOne(mappedBy = "dataOfUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private NutritionTrack nutritionTrack;

    public NutritionTrack getNutritionTrack() {
        return nutritionTrack;
    }

    public void setNutritionTrack(NutritionTrack nutritionTrack) {
        this.nutritionTrack = nutritionTrack;
    }

    @OneToOne(mappedBy = "dataOfUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private SeanceTrack seanceTrack;

    public SeanceTrack getSeanceTrack() {
        return seanceTrack;
    }

    public void setSeanceTrack(SeanceTrack seanceTrack) {
        this.seanceTrack = seanceTrack;
    }

    public DayDataOfUser() {
    }

    public DayDataOfUser(LocalDate dayData, User user) {
        this.dayData = dayData;
        this.user = user;
    }

    // Getters
    public Long getIdDayData() {
        return idDayData;
    }

    public LocalDate getDay() {
        return dayData;
    }

    public User getUser() {
        return user;
    }

    public void setDay(LocalDate dayData) {
        this.dayData = dayData;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdate = Instant.now();
    }
}
