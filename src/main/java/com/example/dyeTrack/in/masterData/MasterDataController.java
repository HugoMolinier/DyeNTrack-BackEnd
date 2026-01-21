package com.example.dyeTrack.in.masterData;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.dyeTrack.core.entity.RelEexerciseEquipment.RelExerciseEquipment;
import com.example.dyeTrack.in.exercise.dto.EquipmentInfoReturnDTO;
import com.example.dyeTrack.in.exercise.dto.MuscleInfoReturnDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.service.EquipmentService;
import com.example.dyeTrack.core.service.ExerciseService;
import com.example.dyeTrack.core.service.LateraliteService;
import com.example.dyeTrack.core.service.MuscleGroupService;
import com.example.dyeTrack.core.service.MuscleService;
import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.example.dyeTrack.in.exercise.dto.ExerciseDetailReturnDTO;
import com.example.dyeTrack.in.muscle.dto.ReturnMuscleDTO;
import com.example.dyeTrack.in.utils.ResponseBuilder;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/api/master")
public class MasterDataController {

    private final LateraliteService lateraliteService;
    private final MuscleService muscleService;
    private final EquipmentService equipmentService;
    private final MuscleGroupService muscleGroupService;
    private final ExerciseService exerciseService;

    private static Map<String, Object> cachedData;
    private static Instant lastUpdate;

    public MasterDataController(LateraliteService lateraliteService,
            MuscleService muscleService,
            EquipmentService equipmentService,
            MuscleGroupService muscleGroupService,
            ExerciseService exerciseService) {
        this.lateraliteService = lateraliteService;
        this.muscleService = muscleService;
        this.equipmentService = equipmentService;
        this.muscleGroupService = muscleGroupService;
        this.exerciseService = exerciseService;
    }

    @PostConstruct
    public void initCache() {
        refreshCache();
    }

    public void refreshCache() {
        cachedData = buildMasterData();
        lastUpdate = Instant.now();
        System.out.println("MasterData cache rafraîchi");
    }

    @GetMapping("/static")
    public ResponseEntity<ResponseBuilder.ResponseDTO<Map<String, Object>>> getAllData() {
        if (cachedData == null || lastUpdate == null || lastUpdate.isBefore(Instant.now().minus(1, ChronoUnit.DAYS))) {
            refreshCache();
            System.out.println("Cache rafraîchi automatiquement depuis /static car trop vieux");
        }
        return ResponseBuilder.success(cachedData, "Toutes les données récupérées avec succès");
    }


    @GetMapping("/reload")
    public void reload(){
        refreshCache();
    }
    @GetMapping("/lastUpdate")
    public ResponseEntity<ResponseBuilder.ResponseDTO<Instant>> getLastUpdate() {
        return ResponseBuilder.success(lastUpdate, "Dernière mise à jour des données");
    }

    private Map<String, Object> buildMasterData() {
        Map<String, Object> allData = new HashMap<>();

        allData.put("lateralites", lateraliteService.getAll());
        allData.put("musclesGroup", muscleGroupService.getAll());
        allData.put("equipment", equipmentService.getAll());

        List<ExerciseDetailReturnDTO> officialExercises = exerciseService.getAll(
                null, true, null, false,
                true, null,
                null, null).stream()
                .map(ex -> {
                    List<MuscleInfoReturnDTO> muscles = new ArrayList<>();
                    Long mainFocus = null;
                    Long mainMuscle=null;
                    for (RelExerciseMuscle rel : ex.getRelExerciseMuscles()) {
                        muscles.add(new MuscleInfoReturnDTO(rel.getMuscle().getId()));
                        if (rel.isPrincipal()){
                            mainMuscle= rel.getMuscle().getId();
                        }
                        if (rel.isPrincipal() && mainFocus == null && rel.getMuscle().getMuscleGroup() != null) {
                            mainFocus = rel.getMuscle().getMuscleGroup().getId();
                        }
                    }


                    List<EquipmentInfoReturnDTO> equipmentInfos = new ArrayList<>();
                    Long defaultEquipment = null;
                    for (RelExerciseEquipment relEq : ex.getRelExerciseEquipments()) {
                        if (relEq.getEquipment() != null) {
                            equipmentInfos.add(new EquipmentInfoReturnDTO(relEq.getEquipment().getId()));
                            if (relEq.getIsDefault()) {
                                defaultEquipment = relEq.getEquipment().getId();
                            }
                        }
                    }

                    return new ExerciseDetailReturnDTO(ex, muscles, mainFocus,mainMuscle, equipmentInfos, defaultEquipment);
                })
                .collect(Collectors.toList());

        allData.put("officialExercise", officialExercises);

        List<ReturnMuscleDTO> musclesDTO = muscleService.getAll()
                .stream()
                .map(ReturnMuscleDTO::new)
                .collect(Collectors.toList());
        allData.put("muscles", musclesDTO);

        return allData;
    }
}
