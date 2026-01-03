package com.example.dyeTrack.core.port.in;

import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeance;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciseVO;

public interface PresetSeanceUseCase {
    PresetSeance save(Long idPreset,String name, Long idUserWhoAdd, List<PresetSeanceExerciseVO> relExerciseMuscles);

    List<PresetSeance> getAllPresetOfUser(Long idUser, String name);

    PresetSeance getById(Long idPreset, Long idUser);

    void delete(Long idpresetSeance, Long idUserQuiDelete);
}
