package com.example.dyeTrack.core.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeanceExercise.PresetSeanceExercise;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class PresetSeance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPresetSeance;

    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "idCreator", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant lastUpdate;

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

    @OneToMany(mappedBy = "presetSeance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PresetSeanceExercise> presetSeanceExercises = new ArrayList<>();

    public List<PresetSeanceExercise> getPresetSeanceExercise() {
        return presetSeanceExercises;
    }

    public void setPresetSeanceExercise(List<PresetSeanceExercise> presetSeanceExercises) {
        this.presetSeanceExercises = presetSeanceExercises;
    }

    public PresetSeance() {
    }

    public PresetSeance(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Long getIdPresetSeance() {
        return idPresetSeance;
    }

    public void setIdPresetSeance(Long idPresetSeance) {
        this.idPresetSeance = idPresetSeance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
