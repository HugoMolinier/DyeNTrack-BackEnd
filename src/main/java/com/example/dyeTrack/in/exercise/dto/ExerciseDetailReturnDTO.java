package com.example.dyeTrack.in.exercise.dto;

import java.util.List;

import com.example.dyeTrack.core.entity.Equipment;
import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExerciseDetailReturnDTO extends ExerciseUltraLightReturnDTO {

    private List<MuscleInfoReturnDTO> muscleInfos;
    private Long mainFocusGroup;
    private Long mainMuscle;

    private Long defaultEquipment;

    private List<EquipmentInfoReturnDTO> equipmentInfo;

    private String descriptionFR;
    private String descriptionEN;
    private String linkVideo;
    private Long idCreator;

    public ExerciseDetailReturnDTO() {
        super();
    }

    public ExerciseDetailReturnDTO(Long idExercise, String nameFR,String nameEN, String descriptionFR,String descriptionEN, String linkVideo, Long idUser,
            List<MuscleInfoReturnDTO> muscleInfos, Long mainFocusGroup) {
        super(idExercise, nameFR,nameEN);
        this.descriptionFR = descriptionFR;
        this.descriptionEN = descriptionEN;
        this.linkVideo = linkVideo;
        this.idCreator = idUser;
        this.muscleInfos = muscleInfos;
        this.mainFocusGroup = mainFocusGroup;
    }


    public ExerciseDetailReturnDTO(Exercise exercise, List<MuscleInfoReturnDTO> muscleInfos, Long mainFocusGroup,Long mainMuscle,List<EquipmentInfoReturnDTO> equipmentInfo,Long defaultEquipment) {
        super(exercise);
        this.descriptionFR = exercise.getDescriptionFR();
        this.descriptionEN = exercise.getDescriptionEN();
        this.linkVideo = exercise.getLinkVideo();
        this.idCreator = exercise.getUser() != null ? exercise.getUser().getId() : null;
        this.muscleInfos = muscleInfos;
        this.mainFocusGroup = mainFocusGroup;
        this.mainMuscle = mainMuscle;
        this.defaultEquipment = defaultEquipment;
        this.equipmentInfo= equipmentInfo;
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

    public List<MuscleInfoReturnDTO> getMuscleInfos() {
        return muscleInfos;
    }

    public void setMuscleInfos(List<MuscleInfoReturnDTO> muscleInfos) {
        this.muscleInfos = muscleInfos;
    }

    public void setMainMuscle(Long idMuscle) {
        this.mainMuscle = idMuscle;
    }

    public Long getMainFocusGroup() {
        return mainFocusGroup;
    }

    public void setMainFocusGroup(Long newMainFocusGroup) {
        this.mainFocusGroup = newMainFocusGroup;
    }
    public Long getMainMuscle() {
        return mainMuscle;
    }

    public List<EquipmentInfoReturnDTO> getEquipmentInfo() {
        return equipmentInfo;
    }

    public void setEquipmentInfo(List<EquipmentInfoReturnDTO> equipmentInfo) {
        this.equipmentInfo = equipmentInfo;
    }

    public Long getDefaultEquipment() {
        return defaultEquipment;
    }

    public void setDefaultEquipment(Long defaultEquipment) {
        this.defaultEquipment = defaultEquipment;
    }
}
