package com.example.dyeTrack.core.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Equipment;
import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.entity.PresetSeance;
import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.entity.PresetSeanceExercise.PresetSeanceExercise;
import com.example.dyeTrack.core.exception.EntityNotFoundException;
import com.example.dyeTrack.core.exception.ForbiddenException;
import com.example.dyeTrack.core.port.in.PresetSeanceUseCase;
import com.example.dyeTrack.core.port.out.EquipmentPort;
import com.example.dyeTrack.core.port.out.ExercisePort;
import com.example.dyeTrack.core.port.out.LateralitePort;
import com.example.dyeTrack.core.port.out.PresetSeancePort;
import com.example.dyeTrack.core.port.out.UserPort;
import com.example.dyeTrack.core.util.EntityUtils;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciseVO;

import jakarta.transaction.Transactional;

@Service
public class PresetSeanceService implements PresetSeanceUseCase {

    private final PresetSeancePort presetSeancePort;
    private final UserPort userPort;
    private final LateralitePort lateralitePort;
    private final EquipmentPort equipmentPort;
    private final ExercisePort exercisePort;

    public PresetSeanceService(PresetSeancePort presetSeancePort, UserPort userPort, LateralitePort lateralitePort,
            EquipmentPort equipmentPort, ExercisePort exercisePort) {
        this.presetSeancePort = presetSeancePort;
        this.userPort = userPort;
        this.equipmentPort = equipmentPort;
        this.lateralitePort = lateralitePort;
        this.exercisePort = exercisePort;
    }

    @Transactional
    public PresetSeance save(Long idPreset, String name, Long idUserWhoAdd, List<PresetSeanceExerciseVO> presetSeanceExercise) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name cannot be empty");
        User user = EntityUtils.getUserOrThrow(idUserWhoAdd, userPort);

        PresetSeance presetSeance;
        if (idPreset != null) {
            presetSeance = presetSeancePort.getById(idPreset);
            if (!Objects.equals(presetSeance.getUser().getId(), idUserWhoAdd))
                throw new ForbiddenException("PresetSeance not found");

            if (presetSeance == null) {
                throw new IllegalArgumentException("PresetSeance not found");
            }
            presetSeance.setName(name);
        } else {
            presetSeance = new PresetSeance(name, user);
        }

        if (presetSeanceExercise != null) {
            if (presetSeance.getPresetSeanceExercise() == null) {
                presetSeance.setPresetSeanceExercise(new ArrayList<>());
            }

            Map<Long, PresetSeanceExercise> existingMap = presetSeance.getPresetSeanceExercise().stream()
                    .collect(Collectors.toMap(p -> p.getExercise().getIdExercise(), p -> p));

            List<Equipment> equipments = equipmentPort.getAll();
            List<Lateralite> lateralites = lateralitePort.getAll();
            List<PresetSeanceExercise> finalExercises = new ArrayList<>();
            int index = 1;

            for (PresetSeanceExerciseVO vo : presetSeanceExercise) {
                PresetSeanceExercise existing = existingMap.get(vo.getIdExercise());

                Exercise exercise = EntityUtils.getExerciseOrThrow(vo.getIdExercise(), exercisePort);
                Lateralite lateralite = lateralites.stream()
                        .filter(l -> l.getId().equals(vo.getIdLateralite()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Lateralite ID " + vo.getIdLateralite() + " invalide"));
                Equipment equipment = equipments.stream()
                        .filter(eq -> eq.getId().equals(vo.getIdEquipment()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Equipment ID " + vo.getIdEquipment() + " invalide"));

                if (existing != null) {
                    // Mettre à jour seulement si nécessaire
                    existing.setParameter(vo.getParameter());
                    existing.setRangeRepInf(vo.getRangeRepInf());
                    existing.setRangeRepSup(vo.getRangeRepSup());
                    existing.setOrderExercise(index++);
                    existing.setLateralite(lateralite);
                    existing.setEquipment(equipment);
                    finalExercises.add(existing);
                } else {
                    // Créer un nouvel exercice
                    finalExercises.add(new PresetSeanceExercise(
                            presetSeance,
                            exercise,
                            vo.getParameter(),
                            vo.getRangeRepInf(),
                            vo.getRangeRepSup(),
                            index++,
                            lateralite,
                            equipment
                    ));
                }
            }
            presetSeance.getPresetSeanceExercise().clear();
            presetSeance.getPresetSeanceExercise().addAll(finalExercises);
        }
        presetSeance.setLastUpdate(Instant.now());
        return presetSeancePort.save(presetSeance);
    }

    public List<PresetSeance> getAllPresetOfUser(Long idUser, String name) {
        return presetSeancePort.findAllPresetOfUser(idUser, name);
    }

    public PresetSeance getById(Long idPreset, Long idUser) {
        PresetSeance presetSeance = presetSeancePort.getById(idPreset);
        if (presetSeance == null)
            throw new EntityNotFoundException("Preset not found with id " + idPreset);
        if (!presetSeance.getUser().getId().equals(idUser))
            throw new ForbiddenException("Accès interdit");
        return presetSeance;
    }


    public void delete(Long idpresetSeance, Long idUserQuiModifie) {
        if (idpresetSeance == null)
            throw new IllegalArgumentException("idPreset cannot be null");

        PresetSeance presetSeance = presetSeancePort.getById(idpresetSeance);
        if (presetSeance == null)
            throw new EntityNotFoundException("Preset not found with id " + idpresetSeance);

        if (!presetSeance.getUser().getId().equals(idUserQuiModifie))
            throw new ForbiddenException("Cet utilisateur ne peut pas delete ce preset");

        if (presetSeance.getPresetSeanceExercise() != null && !presetSeance.getPresetSeanceExercise().isEmpty()) {
            presetSeance.getPresetSeanceExercise().clear();
        }
        presetSeancePort.delete(presetSeance);
    }

}
