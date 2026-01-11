package com.example.dyeTrack.core.entity.RelEexerciseEquipment;

import com.example.dyeTrack.core.entity.Equipment;
import com.example.dyeTrack.core.entity.Exercise;
import jakarta.persistence.*;

@Entity
@Table(name = "rel_exercise_equipment")
public class RelExerciseEquipment {

    @EmbeddedId
    private RelExerciseEquipmentId id;

    @ManyToOne()
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne()
    @MapsId("equipmentId")
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    private boolean isDefault;

    public RelExerciseEquipment() {}

    public RelExerciseEquipment(
            Exercise exercise,
            Equipment equipment,
            boolean isDefault
    ) {
        this.exercise = exercise;
        this.equipment = equipment;
        this.isDefault = isDefault;
        this.id = new RelExerciseEquipmentId(
                exercise.getIdExercise(),
                equipment.getId()
        );
    }

    public RelExerciseEquipmentId getId() {
        return id;
    }

    public void setId(RelExerciseEquipmentId id) {
        this.id = id;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
