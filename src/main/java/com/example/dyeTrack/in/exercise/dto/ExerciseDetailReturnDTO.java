package com.example.dyeTrack.in.exercise.dto;

import java.util.List;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExerciseDetailReturnDTO extends ExerciseUltraLightReturnDTO {

    private List<MuscleInfo> muscleInfos;
    private Long mainFocusGroup;

    private String descriptionFR;
    private String descriptionEN;
    private String linkVideo;
    private Long idCreator;

    public ExerciseDetailReturnDTO() {
        super();
    }

    public ExerciseDetailReturnDTO(Long idExercise, String nameFR,String nameEN, String descriptionFR,String descriptionEN, String linkVideo, Long idUser,
            List<MuscleInfo> muscleInfos, Long mainFocusGroup) {
        super(idExercise, nameFR,nameEN);
        this.descriptionFR = descriptionFR;
        this.descriptionEN = descriptionEN;
        this.linkVideo = linkVideo;
        this.idCreator = idUser;
        this.muscleInfos = muscleInfos;
        this.mainFocusGroup = mainFocusGroup;
    }

    public ExerciseDetailReturnDTO(Exercise exercise, List<MuscleInfo> muscleInfos, Long mainFocusGroup) {
        super(exercise);
        this.descriptionFR = exercise.getDescriptionFR();
        this.descriptionEN = exercise.getDescriptionEN();
        this.linkVideo = exercise.getLinkVideo();
        this.idCreator = exercise.getUser() != null ? exercise.getUser().getId() : null;
        this.muscleInfos = muscleInfos;
        this.mainFocusGroup = mainFocusGroup;
    }

    public ExerciseDetailReturnDTO(Exercise exercise) {
        super(exercise);
        this.descriptionFR = exercise.getDescriptionFR();
        this.descriptionEN = exercise.getDescriptionEN();
        this.linkVideo = exercise.getLinkVideo();
        this.idCreator = exercise.getUser() != null ? exercise.getUser().getId() : null;

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

    public Long getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(Long idUser) {
        this.idCreator = idUser;
    }

    public List<MuscleInfo> getMuscleInfos() {
        return muscleInfos;
    }

    public void setMuscleInfos(List<MuscleInfo> muscleInfos) {
        this.muscleInfos = muscleInfos;
    }

    public Long getMainFocusGroup() {
        return mainFocusGroup;
    }

    public void setMainFocusGroup(Long newMainFocusGroup) {
        this.mainFocusGroup = newMainFocusGroup;
    }
}
