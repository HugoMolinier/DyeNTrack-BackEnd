package com.example.dyeTrack.in.exercise.dto;

import java.util.List;

import com.example.dyeTrack.core.valueobject.MuscleInfo;

import jakarta.validation.constraints.NotBlank;

public class ExerciseCreateDTO {

    @NotBlank(message = "nameFR est obligatoire")
    private String nameFR;

    @NotBlank(message = "nameEN est obligatoire")
    private String nameEN;
    private String descriptionFR;
    private String descriptionEN;
    private String linkVideo;

    @NotBlank(message = "relExerciseMuscle est obligatoire")
    private List<MuscleInfo> relExerciseMuscle;

    public ExerciseCreateDTO() {
    }

    public ExerciseCreateDTO(String nameFR, String nameEN, String descriptionFR,String descriptionEN, String linkVideo,
                             List<MuscleInfo> relExerciseMuscle) {
        this.nameFR = nameFR;
        this.nameEN = nameEN;
        this.descriptionFR = descriptionFR;
        this.descriptionEN = descriptionEN;
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

    public String getDescriptionFR() {
        return descriptionFR;
    }
    public String getDescriptionEN() {
        return descriptionEN;
    }
    public void setDescriptionFR(String description) {
        this.descriptionFR = description;
    }
    public void setDescriptionEN(String description) {
        this.descriptionEN = description;
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
