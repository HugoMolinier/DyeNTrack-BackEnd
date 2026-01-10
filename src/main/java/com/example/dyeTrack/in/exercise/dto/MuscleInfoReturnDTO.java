package com.example.dyeTrack.in.exercise.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class MuscleInfoReturnDTO {

    private Long idMuscle;

    public MuscleInfoReturnDTO() {}

    public MuscleInfoReturnDTO(Long idMuscle) {
        this.idMuscle = idMuscle;
    }

    public Long getIdMuscle() {
        return idMuscle;
    }

    public void setIdMuscle(Long idMuscle) {
        this.idMuscle = idMuscle;
    }
}
