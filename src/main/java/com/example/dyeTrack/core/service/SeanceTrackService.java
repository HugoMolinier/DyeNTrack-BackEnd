package com.example.dyeTrack.core.service;

import java.util.*;
import java.util.stream.Collectors;

import com.example.dyeTrack.core.entity.RelEexerciseEquipment.RelExerciseEquipment;
import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.DayDataOfUser;
import com.example.dyeTrack.core.entity.Equipment;
import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.entity.PlannedExercise;
import com.example.dyeTrack.core.entity.PresetSeance;
import com.example.dyeTrack.core.entity.SeanceTrack;
import com.example.dyeTrack.core.entity.setOfPlannedExercise.SetOfPlannedExercise;
import com.example.dyeTrack.core.entity.setOfPlannedExercise.SetOfPlannedExercise.Side;
import com.example.dyeTrack.core.exception.ForbiddenException;
import com.example.dyeTrack.core.port.in.SeanceTrackUseCase;
import com.example.dyeTrack.core.port.out.DayDataOfUserPort;
import com.example.dyeTrack.core.port.out.EquipmentPort;
import com.example.dyeTrack.core.port.out.ExercisePort;
import com.example.dyeTrack.core.port.out.LateralitePort;
import com.example.dyeTrack.core.port.out.PresetSeancePort;
import com.example.dyeTrack.core.port.out.SeanceTrackPort;
import com.example.dyeTrack.core.util.EntityUtils;
import com.example.dyeTrack.core.valueobject.PlannedExerciseVO;
import com.example.dyeTrack.core.valueobject.SeanceTrackVO;
import com.example.dyeTrack.core.valueobject.SetOfPlannedExerciseVO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class SeanceTrackService implements SeanceTrackUseCase {

    private final SeanceTrackPort seanceTrackPort;
    private final DayDataOfUserPort dataOfUserPort;
    private final PresetSeancePort presetSeancePort;
    private final LateralitePort lateralitePort;
    private final EquipmentPort equipmentPort;
    private final ExercisePort exercisePort;

    public SeanceTrackService(SeanceTrackPort seanceTrackPort, DayDataOfUserPort dataOfUserPort,
            ExercisePort exercisePort, LateralitePort lateralitePort, EquipmentPort equipmentPort,
            PresetSeancePort presetSeancePort) {
        this.seanceTrackPort = seanceTrackPort;
        this.dataOfUserPort = dataOfUserPort;
        this.exercisePort = exercisePort;
        this.equipmentPort = equipmentPort;
        this.lateralitePort = lateralitePort;
        this.presetSeancePort = presetSeancePort;
    }

    @Transactional
    public SeanceTrack createOrUpdateSeanceTrack(DayDataOfUser dataOfUser, Long userId, SeanceTrackVO vo) {

        if (dataOfUser == null)
            throw new EntityNotFoundException("DayData not found");
        if (!dataOfUser.getUser().getId().equals(userId))
            throw new ForbiddenException("Not dataOfUser of the user");

        SeanceTrack seanceTrack = dataOfUser.getSeanceTrack();
        if (seanceTrack == null) {
            seanceTrack = new SeanceTrack();
            seanceTrack.setDataOfUser(dataOfUser);
        }
        List<Equipment> equipments = equipmentPort.getAll();
        List<Lateralite> lateralites = lateralitePort.getAll();

        seanceTrack.setStartHour(vo.getStartHour());

        // Charger preset si existant
        if (vo.getPresetSeanceId() != null) {
            PresetSeance preset = presetSeancePort.getById(vo.getPresetSeanceId());
            if (preset == null)
                throw new EntityNotFoundException("Preset not found with id " + vo.getPresetSeanceId());
            seanceTrack.setPresetSeance(preset);
        }

        if (seanceTrack.getPlannedExercises() == null) {
            seanceTrack.setPlannedExercises(new ArrayList<>());
        }
        Map<Long, PlannedExercise> exerciseMapAlreadyExist = seanceTrack.getPlannedExercises().stream()
                .collect(Collectors.toMap(
                        pe -> pe.getExercise().getIdExercise(),
                        pe -> pe
                ));
        List<PlannedExercise> finalExercises = new ArrayList<>();
        for (PlannedExerciseVO exerciseVO : vo.getPlannedExercises()) {

            PlannedExercise plannedExercise = exerciseMapAlreadyExist.get(exerciseVO.getExerciseId());
            if (plannedExercise == null) {
                plannedExercise = new PlannedExercise();
                plannedExercise.setExercise(EntityUtils.getExerciseOrThrow(exerciseVO.getExerciseId(), exercisePort));
                plannedExercise.setSeanceTrack(seanceTrack);
                plannedExercise.setSetsOfPlannedExercise(new ArrayList<>());

            }

            plannedExercise.setExerciseOrder(exerciseVO.getExerciseOrder());
            Lateralite lateralite = lateralites.stream()
                    .filter(l -> l.getId().equals(exerciseVO.getLateraliteId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Lateralite ID " + exerciseVO.getLateraliteId() + " invalide"));

            Optional<RelExerciseEquipment> relEquipment = plannedExercise.getExercise()
                    .getRelExerciseEquipments().stream()
                    .filter(eq -> eq.getEquipment().getId().equals(exerciseVO.getEquipmentId()))
                    .findFirst();

            if (relEquipment.isEmpty()) {
                relEquipment = plannedExercise.getExercise()
                        .getRelExerciseEquipments().stream()
                        .filter(RelExerciseEquipment::getIsDefault)
                        .findFirst();
            }

            Equipment equipment = relEquipment.map(RelExerciseEquipment::getEquipment).orElse(null);

            if (lateralite != plannedExercise.getLateralite()) {
                plannedExercise.getSetsOfPlannedExercise().clear();
            }
            plannedExercise.setLateralite(lateralite);
            plannedExercise.setEquipment(equipment);

            // 🔁 Ajouter les sets liés à cet exercice
            Map<Integer, List<SetOfPlannedExercise.Side>> orderRecurenceSide = new HashMap<>();

            Map<String, SetOfPlannedExercise> setMapAlreadyExist = plannedExercise.getSetsOfPlannedExercise().stream()
                    .collect(Collectors.toMap(
                            s -> s.getSetOrder() + "_" + s.getSide(),
                            s -> s
                    ));
            List<SetOfPlannedExercise> finalSets = new ArrayList<>();

            // 3️⃣ Parcourir les sets du VO
            for (SetOfPlannedExerciseVO setVO : exerciseVO.getSets()) {
                SetOfPlannedExercise set = setMapAlreadyExist.get(setVO.getSetOrder() + "_" + setVO.getSide());
                if (set == null) {
                    set = new SetOfPlannedExercise();
                    set.setSetOrder(setVO.getSetOrder());
                    set.setPlannedExercise(plannedExercise);
                    set.setSide(setVO.getSide());
                }

                set.setRepsNumber(setVO.getRepsNumber());
                set.setRir(setVO.getRir());
                set.setCharge(setVO.getCharge());
                set.setTypeOfSet(setVO.getTypeOfSet());

                if (setVO.getSide() != Side.BOTH && lateralite.getId() != 2L) {
                    throw new IllegalArgumentException(
                            "Side " + setVO.getSide() + " non adapté à  " + lateralite.getNameFR());
                }
                verifySide(orderRecurenceSide, setVO.getSetOrder(), setVO.getSide());
                finalSets.add(set);
            }
            List<SetOfPlannedExercise> existingSets = plannedExercise.getSetsOfPlannedExercise();
            existingSets.clear();
            existingSets.addAll(finalSets);

            finalExercises.add(plannedExercise);
        }
        seanceTrack.getPlannedExercises().clear();
        seanceTrack.getPlannedExercises().addAll(finalExercises);
        return seanceTrackPort.save(seanceTrack);

    }

    @Transactional
    public SeanceTrack getSeanceOfDay(Long dayDataOfUserId, Long userId) {
        if (dayDataOfUserId == null)
            throw new IllegalArgumentException("dayDataOfUserId empty");

        DayDataOfUser dataOfUser = dataOfUserPort.getById(dayDataOfUserId);
        if (dataOfUser == null)
            throw new EntityNotFoundException("DayData not found");

        if (!dataOfUser.getUser().getId().equals(userId))
            throw new ForbiddenException("Not dataOfUser of the user");

        SeanceTrack seanceTrack = dataOfUser.getSeanceTrack();
        if (seanceTrack == null)
            throw new EntityNotFoundException("No SeanceTrack found for this day");

        return seanceTrack;
    }

    @Transactional
    public List<SeanceTrack> getAllSeancesOfUser(Long userId) {
        if (userId == null)
            throw new IllegalArgumentException("userId empty");
        List<DayDataOfUser> allDayData = dataOfUserPort.getAll(userId);
        if (allDayData.isEmpty())
            throw new EntityNotFoundException("No DayData found for this user");
        return allDayData.stream()
                .map(DayDataOfUser::getSeanceTrack)
                .filter(seance -> seance != null)
                .toList();
    }

    // Méthode de vérification
    private void verifySide(Map<Integer, List<SetOfPlannedExercise.Side>> map, int setOrder,
            SetOfPlannedExercise.Side side) {
        List<SetOfPlannedExercise.Side> sides = map.computeIfAbsent(setOrder, k -> new ArrayList<>());

        if (side == SetOfPlannedExercise.Side.BOTH) {
            if (!sides.isEmpty()) {
                throw new IllegalArgumentException(
                        "Impossible d'ajouter BOTH si des sides individuels existent pour setOrder " + setOrder);
            }
            sides.add(SetOfPlannedExercise.Side.BOTH);
        } else {
            if (sides.contains(SetOfPlannedExercise.Side.BOTH)) {
                throw new IllegalArgumentException(
                        "Impossible d'ajouter " + side + " car BOTH existe déjà pour setOrder " + setOrder);
            }
            if (sides.contains(side)) {
                throw new IllegalArgumentException(
                        "Side " + side + " déjà ajouté pour setOrder " + setOrder);
            }
            sides.add(side);
        }
    }

}
