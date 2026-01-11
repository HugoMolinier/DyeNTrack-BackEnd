package com.example.dyeTrack.core.service;

import java.util.*;
import java.util.stream.Collectors;

import com.example.dyeTrack.core.entity.Equipment;
import com.example.dyeTrack.core.entity.RelEexerciseEquipment.RelExerciseEquipment;
import com.example.dyeTrack.core.port.out.EquipmentPort;
import com.example.dyeTrack.core.valueobject.EquipmentInfo;
import com.example.dyeTrack.in.exercise.dto.ExerciseDetailReturnDTO;
import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.exception.EntityNotFoundException;
import com.example.dyeTrack.core.exception.ForbiddenException;
import com.example.dyeTrack.core.port.in.ExerciseUseCase;
import com.example.dyeTrack.core.port.out.ExercisePort;
import com.example.dyeTrack.core.port.out.MusclePort;
import com.example.dyeTrack.core.port.out.UserPort;
import com.example.dyeTrack.core.util.EntityUtils;
import com.example.dyeTrack.core.valueobject.MuscleInfo;

import jakarta.transaction.Transactional;

@Service
public class ExerciseService implements ExerciseUseCase {
    private final ExercisePort exercisePort;
    private final MusclePort musclePort;
    private final UserPort userPort;
    private final EquipmentPort equipmentPort;

    public ExerciseService(ExercisePort exercisePort, UserPort userPort,
            MusclePort musclePort,EquipmentPort equipmentPort) {
        this.exercisePort = exercisePort;
        this.userPort = userPort;
        this.musclePort = musclePort;
        this.equipmentPort=equipmentPort;
    }

    public Exercise getByIdExercise(Long idExercise, Boolean onlyPrincipalMuscle) {
        Exercise e = EntityUtils.getExerciseOrThrow(idExercise, exercisePort);
        e.getRelExerciseMuscles().removeIf(rem -> !rem.isPrincipal());
        return e;
    }

    public List<Exercise> getAll(String name, Boolean officialExercise, Long idUser, Boolean onlyPrincipalMuscle,
            Boolean showMainFocusMuscularGroup, List<Integer> idsGroupeMuscle, List<Integer> idMuscle,
            List<Long> idsExercise) {
        return (showMainFocusMuscularGroup || idsGroupeMuscle != null)
                ? exercisePort.getAllWithShowGroupe(name, officialExercise, idUser, onlyPrincipalMuscle,
                        idsGroupeMuscle, idMuscle, idsExercise)
                : exercisePort.getAll(name, officialExercise, idUser, onlyPrincipalMuscle, idMuscle, idsExercise);
    }

    @Transactional
    public Exercise create(String nameFR, String nameEN, String descriptionFR, String descriptionEN, String linkVideo, Long idUser,
                           List<MuscleInfo> relExerciseMuscles, List<EquipmentInfo> equipmentInfos) {
        if (nameFR == null)
            throw new IllegalArgumentException("nameFR empty");
        if (nameEN == null)
            throw new IllegalArgumentException("nameEN empty");
        if (idUser == null)
            throw new IllegalArgumentException("idUser empty");
        if (relExerciseMuscles == null || relExerciseMuscles.isEmpty())
            throw new IllegalArgumentException("La liste des muscles ne peut pas être vide");

        User user = EntityUtils.getUserOrThrow(idUser, userPort);

        Exercise exercise = new Exercise(nameFR,nameEN, descriptionFR,descriptionEN, linkVideo, user);
        List<RelExerciseMuscle> relations = buildRelExerciseMuscles(exercise, relExerciseMuscles);
        exercise.getRelExerciseMuscles().addAll(relations);

        List<RelExerciseEquipment> relationsEquipment = buildEquipmentInfo(exercise, equipmentInfos);
        exercise.getRelExerciseEquipments().addAll(relationsEquipment);

        return exercisePort.create(exercise);
    }



    @Transactional
    public Exercise update(Long idExercise, Long idUserQuiModifie, String nameFR,String nameEN, String descriptionFR,String descriptionEN, String linkVideo,
            List<MuscleInfo> relExerciseMuscles, List<EquipmentInfo> equipmentInfos) {

        Exercise exercise = EntityUtils.getExerciseOrThrow(idExercise, exercisePort);
        if (exercise.getUser() == null)
            throw new ForbiddenException("Impossible de modifier un exercise officiel");
        if (!exercise.getUser().getId().equals(idUserQuiModifie))
            throw new ForbiddenException("Cet utilisateur ne peut pas modifier cet exercise");

        if (nameFR != null) exercise.setNameFR(nameFR);
        if (nameEN != null) exercise.setNameEN(nameEN);
        if (descriptionFR != null) exercise.setDescriptionFR(descriptionFR);
        if (descriptionEN != null) exercise.setDescriptionFR(descriptionEN);
        if (linkVideo != null) exercise.setLinkVideo(linkVideo);

        if (relExerciseMuscles != null && !relExerciseMuscles.isEmpty()) {
            List<RelExerciseMuscle> relations = buildRelExerciseMuscles(exercise, relExerciseMuscles);
            exercise.getRelExerciseMuscles().clear();
            exercise.getRelExerciseMuscles().addAll(relations);
        }

        if (equipmentInfos != null && !equipmentInfos.isEmpty()) {
            List<RelExerciseEquipment> relationsEquipment = buildEquipmentInfo(exercise, equipmentInfos);
            exercise.getRelExerciseEquipments().addAll(relationsEquipment);
        }
        return exercisePort.update(exercise);
    }


    //Temporaire
    @Transactional
    public void assignAllEquipmentToExistingExercises() {
        List<Exercise> exercises =   this.getAll(
                null, true, null, false,
                true, null,
                null, null);
        List<Equipment> allEquipment = equipmentPort.getAll();

        if(allEquipment.isEmpty()) {
            throw new IllegalStateException("Aucun équipement disponible pour assigner");
        }

        for (Exercise exercise : exercises) {
            if (exercise.getRelExerciseEquipments().isEmpty() ||exercise.getRelExerciseEquipments() ==null) {
                exercise.getRelExerciseEquipments().clear();

                boolean first = true;
                for (Equipment equipment : allEquipment) {
                    exercise.getRelExerciseEquipments().add(
                            new RelExerciseEquipment(exercise, equipment, first)
                    );
                    first = false;
                }

                exercisePort.update(exercise);
            }
        }
    }


    @Transactional
    public void delete(Long idExercise, Long idUserQuiDelete) {

        Exercise exercise = EntityUtils.getExerciseOrThrow(idExercise, exercisePort);
        if (exercise.getUser() == null)
            throw new ForbiddenException("Impossible de delete un exercise officiel");
        if (!exercise.getUser().getId().equals(idUserQuiDelete))
            throw new ForbiddenException("Cet utilisateur ne peut pas delete cet exercise");
        exercisePort.delete(exercise);
    }

    // ============Utilitaire /
    private List<RelExerciseMuscle> buildRelExerciseMuscles(Exercise exercise,
            List<MuscleInfo> relExerciseMuscles) {

        Map<Long, Muscle> muscleMap = musclePort.getAll().stream()
                .collect(Collectors.toMap(Muscle::getId, m -> m));

        Map<Long, Boolean> musclePrincipalMap = new HashMap<>();
        List<RelExerciseMuscle> relations = new ArrayList<>();
        int principalCount = 0;

        for (MuscleInfo muscleInfo : relExerciseMuscles) {
            Long muscleId = muscleInfo.getIdMuscle();
            Boolean existingPrincipal = musclePrincipalMap.get(muscleId);

            if (existingPrincipal != null && !existingPrincipal.equals(muscleInfo.isPrincipal())) {
                throw new IllegalArgumentException(
                        "Le muscle " + muscleId + " ne peut pas être à la fois principal et non principal");
            }

            if (existingPrincipal == null) {
                Muscle muscle = muscleMap.get(muscleId);
                if (muscle == null)
                    throw new EntityNotFoundException("muscle Not found with id " + muscleId);

                relations.add(new RelExerciseMuscle(muscle, exercise, muscleInfo.isPrincipal()));
                musclePrincipalMap.put(muscleId, muscleInfo.isPrincipal());

                if (muscleInfo.isPrincipal())
                    principalCount++;
            }
        }
        if (principalCount != 1)
            throw new IllegalArgumentException("Il doit y avoir 1 muscle principal");

        return relations;
    }

    private List<RelExerciseEquipment> buildEquipmentInfo(Exercise exercise, List<EquipmentInfo> equipmentInfos){
        List<RelExerciseEquipment> relations = new ArrayList<>();

        // 1️⃣ Cas : aucun équipement fourni
        if (equipmentInfos == null || equipmentInfos.isEmpty()) {
            List<Equipment> allEquipment = equipmentPort.getAll();
            if (allEquipment.isEmpty()) {
                throw new EntityNotFoundException("Aucun équipement disponible");
            }
            allEquipment.forEach( equipment ->
                    relations.add(new RelExerciseEquipment(exercise, equipment, relations.isEmpty())));
            return relations;
        }
        boolean defaultFound = false;
        Set<Long> equipmentIds = new HashSet<>();

        for (EquipmentInfo info : equipmentInfos) {

            Long equipmentId = info.getIdEquipment();

            if (!equipmentIds.add(equipmentId)) {
                throw new IllegalArgumentException(
                        "L'équipement " + equipmentId + " est présent plusieurs fois"
                );
            }

            Equipment equipment = equipmentPort.getById(equipmentId).orElseThrow(() ->
                            new EntityNotFoundException("Equipment not found with id " + equipmentId)
                    );

            if (info.isDefault() &&defaultFound ) {
                if (defaultFound) {
                    throw new IllegalArgumentException(
                            "Un seul équipement par défaut est autorisé"
                    );
                }
                defaultFound = true;
            }

            relations.add(
                    new RelExerciseEquipment(exercise, equipment, info.isDefault())
            );
        }

        if (!defaultFound) {
            throw new IllegalArgumentException(
                    "Un équipement par défaut est obligatoire"
            );
        }

        return relations;
    }

}
