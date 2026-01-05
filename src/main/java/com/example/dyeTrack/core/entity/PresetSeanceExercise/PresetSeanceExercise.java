package com.example.dyeTrack.core.entity.PresetSeanceExercise;

import com.example.dyeTrack.core.entity.Equipment;
import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.entity.PresetSeance;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class PresetSeanceExercise {

    @EmbeddedId
    private PresetSeanceExerciseId id = new PresetSeanceExerciseId();

    @ManyToOne
    @MapsId("presetSeanceId")
    @JoinColumn(name = "id_preset", nullable = false)
    private PresetSeance presetSeance;

    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private Integer nbSet;
    private Integer rangeRepInf;
    private Integer rangeRepSup;

    @Column(name = "order_exercise")
    private Integer orderExercise;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lateralite_id", nullable = false)
    private Lateralite lateralite;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    // --- Constructeurs ---
    public PresetSeanceExercise() {
    }

    public PresetSeanceExercise(PresetSeance presetSeance, Exercise exercise,
                                Integer nbSet, Integer rangeRepInf, Integer rangeRepSup,
            Integer orderExercise, Lateralite lateralite, Equipment equipment) {
        this.presetSeance = presetSeance;
        this.id = new PresetSeanceExerciseId(presetSeance.getIdPresetSeance(), exercise.getIdExercise());
        this.exercise = exercise;
        this.nbSet = nbSet;
        this.rangeRepInf = rangeRepInf;
        this.rangeRepSup = rangeRepSup;
        this.orderExercise = orderExercise;
        this.lateralite = lateralite;
        this.equipment = equipment;
    }

    // --- Getters & Setters ---
    public PresetSeanceExerciseId getId() {
        return id;
    }

    public PresetSeance getPresetSeance() {
        return presetSeance;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public Integer getNbSet() {
        return nbSet;
    }

    public void setNbSet(Integer nbSet) {
        this.nbSet = nbSet;
    }

    public Integer getRangeRepInf() {
        return rangeRepInf;
    }

    public void setRangeRepInf(Integer rangeRepInf) {
        this.rangeRepInf = rangeRepInf;
    }

    public Integer getRangeRepSup() {
        return rangeRepSup;
    }

    public void setRangeRepSup(Integer rangeRepSup) {
        this.rangeRepSup = rangeRepSup;
    }

    public Integer getOrderExercise() {
        return orderExercise;
    }

    public void setOrderExercise(Integer orderExercise) {
        this.orderExercise = orderExercise;
    }

    public Lateralite getLateralite() {
        return lateralite;
    }

    public void setLateralite(Lateralite lateralite) {
        this.lateralite = lateralite;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
}
