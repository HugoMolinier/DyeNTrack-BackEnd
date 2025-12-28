package com.example.dyeTrack.in.exercise.dto;

import java.util.List;

import com.example.dyeTrack.core.valueobject.MuscleInfo;

import jakarta.validation.constraints.NotBlank;

public class ExerciseCreateDTO {

    @NotBlank(message = "nameFR est obligatoire")
    private String nameFR;

    @NotBlank(message = "nameEN est obligatoire")
    private String nameEN;
    private String description;
    private String linkVideo;

    @NotBlank(message = "relExerciseMuscle est obligatoire")
    private List<MuscleInfo> relExerciseMuscle;

    public ExerciseCreateDTO() {
    }

    public ExerciseCreateDTO(String nameFR, String nameEN, String description, String linkVideo,
                             List<MuscleInfo> relExerciseMuscle) {
        this.nameFR = nameFR;
        this.nameEN = nameEN;
        this.description = description;
        this.linkVideo = linkVideo;
        this.relExerciseMuscle = relExerciseMuscle;
    }


    public String getNameFR() {
        return nameFR;
    }

    public void setNameFR(String nameFR) {
        this.nameFR = nameFR;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }

    public List<MuscleInfo> getRelExerciseMuscles() {
        return relExerciseMuscle;
    }

    public void setRelExerciseMuscles(List<MuscleInfo> relExerciseMuscle) {
        this.relExerciseMuscle = relExerciseMuscle;
    }
}
