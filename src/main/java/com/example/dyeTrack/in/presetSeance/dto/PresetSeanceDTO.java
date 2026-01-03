package com.example.dyeTrack.in.presetSeance.dto;

import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeance;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciseVO;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PresetSeanceDTO {

    private List<PresetSeanceExerciseVO> presetSeanceExerciseVO;
    private Long idPresetSeance;
    private String name;

    public PresetSeanceDTO() {
    }

    public PresetSeanceDTO(Long idPresetSeance, String name,
            List<PresetSeanceExerciseVO> presetSeanceExerciseVOs) {
        this.idPresetSeance = idPresetSeance;
        this.name = name;
        this.presetSeanceExerciseVO = presetSeanceExerciseVOs;
    }

    public PresetSeanceDTO(String name) {
        this.name = name;
    }
    public PresetSeanceDTO(PresetSeance presetSeance, List<PresetSeanceExerciseVO> presetSeanceExerciseVOs) {
        this.idPresetSeance = presetSeance.getIdPresetSeance();
        this.name = presetSeance.getName();
        this.presetSeanceExerciseVO = presetSeanceExerciseVOs;
    }
    public PresetSeanceDTO(String  name, List<PresetSeanceExerciseVO> presetSeanceExerciseVOs) {
        this.name = name;
        this.presetSeanceExerciseVO = presetSeanceExerciseVOs;
    }

    public Long getIdPreset() {
        return idPresetSeance;
    }

    public void setIdPreset(Long idPresetSeance) {
        this.idPresetSeance = idPresetSeance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PresetSeanceExerciseVO> getPresetSeanceExerciseVO() {
        return presetSeanceExerciseVO;
    }

    public void setPresetSeanceExerciseVO(List<PresetSeanceExerciseVO> presetSeanceExerciseVO) {
        this.presetSeanceExerciseVO = presetSeanceExerciseVO;
    }

}
