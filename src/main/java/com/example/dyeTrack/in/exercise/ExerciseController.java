package com.example.dyeTrack.in.exercise;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.MuscleGroup;
import com.example.dyeTrack.core.entity.RelEexerciseEquipment.RelExerciseEquipment;
import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.service.ExerciseService;
import com.example.dyeTrack.in.exercise.dto.EquipmentInfoReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.MuscleInfoReturnDTO;
import com.example.dyeTrack.in.utils.ResponseBuilder;
import com.example.dyeTrack.in.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/Exercise")
public class ExerciseController {
    private ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseBuilder.ResponseDTO<List<ExerciseDetailReturnDTO>>> getAll(
            @RequestParam(defaultValue = "false") boolean showMuscles,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "false") Boolean officialExercise,
            @RequestParam(required = false) Long idUser,
            @RequestParam(defaultValue = "false") Boolean onlyPrincipalMuscle,
            @RequestParam(defaultValue = "false") Boolean showMainFocusMuscularGroup,
            @RequestParam(required = false) List<Integer> idsGroupeMuscle,
            @RequestParam(required = false) List<Integer> idsMuscle,
            @RequestParam(required = false) List<Long> idsExercise) {

        List<Exercise> exercises = exerciseService.getAll(name, officialExercise, idUser, onlyPrincipalMuscle,
                showMainFocusMuscularGroup, idsGroupeMuscle, idsMuscle, idsExercise);

        if (!showMuscles && !showMainFocusMuscularGroup) {
            List<ExerciseDetailReturnDTO> exercisesOut = new ArrayList<>();
            for (Exercise exercise : exercises) {
                exercisesOut.add(new ExerciseDetailReturnDTO(exercise));
            }
            return ResponseBuilder.success(exercisesOut, "Liste des exercises récupérée avec succès");
        }

        List<ExerciseDetailReturnDTO> result = new ArrayList<>();
        for (Exercise e : exercises) {
            result.add(buildDetailDTO(e, showMuscles, showMainFocusMuscularGroup));
        }
        return ResponseBuilder.success(result, "Liste des exercises récupérée avec succès");

    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ResponseBuilder.ResponseDTO<ExerciseDetailReturnDTO>> getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean showMuscles,
            @RequestParam(defaultValue = "false") Boolean showMainFocusMuscularGroup,
            @RequestParam(defaultValue = "false") Boolean onlyPrincipalMuscle) {

        Exercise exercise = exerciseService.getByIdExercise(id, onlyPrincipalMuscle);

        if (!showMuscles) {
            return ResponseBuilder.success(new ExerciseDetailReturnDTO(exercise),
                    "Exercise récupérée avec succès");
        }

        return ResponseBuilder.success(buildDetailDTO(exercise, showMuscles, showMainFocusMuscularGroup),
                "Exercise récupérée avec succès");
    }

    @PostMapping("/create")
    @Operation(summary = "Create Exercise information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseBuilder.ResponseDTO<ExerciseDetailReturnDTO>> create(
            @RequestBody @Valid ExerciseCreateDTO exercisedto) {

        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        Exercise exercise = exerciseService.create(
                exercisedto.getNameFR(),
                exercisedto.getNameEN(),
                exercisedto.getDescriptionFR(),
                exercisedto.getDescriptionEN(),
                exercisedto.getLinkVideo(),
                idTokenUser,
                exercisedto.getRelExerciseMuscles(),
                exercisedto.getEquipmentInfos());

        return ResponseBuilder.created(buildDetailDTO(exercise, true, true), "Exercise créé avec succès");
    }


    @PostMapping("/fillMissingEquipments")
    public ResponseEntity<ResponseBuilder.ResponseDTO<String>> fillMissingEquipments() {
        exerciseService.assignAllEquipmentToExistingExercises();
        return ResponseBuilder.success("Équipements manquants ajoutés aux exercises", "Opération réussie");
    }


    @Transactional
    @PostMapping("/createMultiple")
    @Operation(summary = "Create Multiple Exercise information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))

    public ResponseEntity<ResponseBuilder.ResponseDTO<List<ExerciseDetailReturnDTO>>> createMultiple(
            @RequestBody List<ExerciseCreateDTO> exercises) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        List<ExerciseDetailReturnDTO> exercisesOut = new ArrayList<>();
        for (ExerciseCreateDTO ex : exercises) {
            exercisesOut.add(new ExerciseDetailReturnDTO(exerciseService.create(
                    ex.getNameFR(),
                    ex.getNameEN(),
                    ex.getDescriptionFR(),
                    ex.getDescriptionEN(),
                    ex.getLinkVideo(),
                    idTokenUser,
                    ex.getRelExerciseMuscles(), ex.getEquipmentInfos())));
        }
        return ResponseBuilder.created(exercisesOut, "Exercises créés avec succès");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Exercise information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseBuilder.ResponseDTO<ExerciseDetailReturnDTO>> update(@PathVariable Long id,
                                                                                       @RequestBody @Valid ExerciseCreateDTO dto) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        Exercise updatedExercise = exerciseService.update(id, idTokenUser, dto.getNameFR(), dto.getNameEN(), dto.getDescriptionFR(), dto.getDescriptionEN(),
                dto.getLinkVideo(),
                dto.getRelExerciseMuscles(), dto.getEquipmentInfos());
        return ResponseBuilder.success(buildDetailDTO(updatedExercise, true, true), "Exercise mis à jour avec succès");

    }



    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Exercise information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseBuilder.ResponseDTO<String>> delete(@PathVariable Long id) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        exerciseService.delete(id, idTokenUser);
        return ResponseBuilder.success("Exercise deleted successfully", "Suppression réussie");

    }

    // Helper
    private ExerciseDetailReturnDTO buildDetailDTO(Exercise exercise, boolean includeMuscles,
                                                   boolean includeMainGroup) {
        List<MuscleInfoReturnDTO> muscles = new ArrayList<>();
        Long mainFocusGroup = null;
        Long mainMuscle = null;
        for (RelExerciseMuscle rel : exercise.getRelExerciseMuscles()) {
            if (includeMuscles) {
                muscles.add(new MuscleInfoReturnDTO(rel.getMuscle().getId()));
                if (rel.isPrincipal()) {
                    mainMuscle = rel.getMuscle().getId();
                }

            }

            if (includeMainGroup && rel.isPrincipal() && mainFocusGroup == null) {
                MuscleGroup gm = rel.getMuscle().getMuscleGroup();
                if (gm != null) {
                    mainFocusGroup = gm.getId();
                }
            }
        }

        // ===== Gestion des équipements =====
        List<EquipmentInfoReturnDTO> equipmentInfos = new ArrayList<>();
        Long defaultEquipment = null;
        for (RelExerciseEquipment relEq : exercise.getRelExerciseEquipments()) {
            if (relEq.getEquipment() != null) {
                equipmentInfos.add(new EquipmentInfoReturnDTO(relEq.getEquipment().getId()));
                if (relEq.getIsDefault()) {
                    defaultEquipment = relEq.getEquipment().getId();
                }
            }
        }


        return new ExerciseDetailReturnDTO(exercise, muscles, mainFocusGroup, mainMuscle, equipmentInfos, defaultEquipment);
    }

}
