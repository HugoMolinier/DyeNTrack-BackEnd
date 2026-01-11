package com.example.dyeTrack.core.entity.RelEexerciseEquipment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RelExerciseEquipmentId implements Serializable {

    private Long exerciseId;

    private Long equipmentId;

    public RelExerciseEquipmentId() {}

    public RelExerciseEquipmentId(Long exerciseId, Long equipmentId) {
        this.exerciseId = exerciseId;
        this.equipmentId = equipmentId;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelExerciseEquipmentId)) return false;
        RelExerciseEquipmentId that = (RelExerciseEquipmentId) o;
        return Objects.equals(exerciseId, that.exerciseId)
                && Objects.equals(equipmentId, that.equipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseId, equipmentId);
    }
}
