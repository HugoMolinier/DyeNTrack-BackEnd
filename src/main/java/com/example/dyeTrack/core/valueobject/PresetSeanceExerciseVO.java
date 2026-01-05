package com.example.dyeTrack.core.valueobject;

public class PresetSeanceExerciseVO {
    private Long idExercise;
    private Integer nbSet;
    private Integer rangeRepInf;
    private Integer rangeRepSup;
    private Long idLateralite;
    private Long idEquipment;

    public PresetSeanceExerciseVO(Long idExercise, Integer nbSet, Integer rangeRepInf, Integer rangeRepSup,
            Long idLateralite, Long idEquipment) {
        this.idExercise = idExercise;
        this.nbSet = nbSet;
        this.rangeRepInf = rangeRepInf;
        this.rangeRepSup = rangeRepSup;
        this.idLateralite = idLateralite;
        this.idEquipment = idEquipment;
    }

    public PresetSeanceExerciseVO() {
    }

    public Long getIdExercise() {
        return idExercise;
    }

    public Integer getNbSet() {
        return nbSet;
    }

    public Integer getRangeRepInf() {
        return rangeRepInf;
    }

    public Integer getRangeRepSup() {
        return rangeRepSup;
    }

    public Long getIdLateralite() {
        return idLateralite;
    }

    public Long getIdEquipment() {
        return idEquipment;
    }

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
    }

    public void setParameter(Integer nbSet) {
        this.nbSet = nbSet;
    }

    public void setRangeRepInf(Integer rangeRepInf) {
        this.rangeRepInf = rangeRepInf;
    }

    public void setRangeRepSup(Integer rangeRepSup) {
        this.rangeRepSup = rangeRepSup;
    }

    public void setIdLateralite(Long idLateralite) {
        this.idLateralite = idLateralite;
    }

    public void setIdEquipment(Long idEquipment) {
        this.idEquipment = idEquipment;
    }
}