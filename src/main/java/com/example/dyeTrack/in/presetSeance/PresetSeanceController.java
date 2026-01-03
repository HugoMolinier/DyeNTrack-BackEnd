package com.example.dyeTrack.in.presetSeance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeanceExercise.PresetSeanceExercise;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciseVO;
import com.example.dyeTrack.in.presetSeance.dto.PresetSeanceDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.PresetSeance;

import com.example.dyeTrack.core.service.PresetSeanceService;
import com.example.dyeTrack.in.utils.ResponseBuilder;
import com.example.dyeTrack.in.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/preset-seances")
@SecurityRequirement(name = "bearerAuth")
public class PresetSeanceController {

    private final PresetSeanceService presetSeanceService;

    public PresetSeanceController(PresetSeanceService presetSeanceService) {
        this.presetSeanceService = presetSeanceService;
    }

    @PostMapping("/save")
    @Operation(summary = "Create or update (if id pass) Preset attribuate to a user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<PresetSeanceDTO>> create(
            @RequestBody @Valid PresetSeanceDTO dtoRequest) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        return ResponseBuilder
                .success(buildDetailDTO(presetSeanceService.save(dtoRequest.getIdPreset() , dtoRequest.getName(), idTokenUser,
                        dtoRequest.getPresetSeanceExerciseVO())), "Preset créé avec succès");

    }

    @GetMapping("/getAll")
    @Operation(summary = "Get All Preset of user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<List<PresetSeanceDTO>>> findAllOfUser(
            @RequestParam(required = false) String name) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        List<PresetSeance> presetSeances = presetSeanceService.getAllPresetOfUser(idTokenUser, name);

        List<PresetSeanceDTO> presetOut = new ArrayList<>();
        for (PresetSeance presetSeance : presetSeances) {
            presetOut.add(buildDetailDTO(presetSeance));
        }

        return ResponseBuilder.success(presetOut, "Liste des presets récupérée avec succès");
    }


    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delelte a Preset of user", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseBuilder.ResponseDTO<String>> delete(@PathVariable Long id) {

        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        presetSeanceService.delete(id, idTokenUser);
        return ResponseBuilder.success(null, "Preset supprimé avec succès");
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "GetByID a Preset of user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<PresetSeanceDTO>> getById(
            @PathVariable Long id) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        return ResponseBuilder.success(buildDetailDTO(presetSeanceService.getById(id, idTokenUser)),
                "Preset récupéré avec succès");

    }

    // Helper
    private PresetSeanceDTO buildDetailDTO(PresetSeance presetSeance) {

        List<PresetSeanceExerciseVO> voList = presetSeance.getPresetSeanceExercise().stream()
                .sorted(Comparator.comparingInt(PresetSeanceExercise::getOrderExercise))
                .map(pse -> new PresetSeanceExerciseVO(
                        pse.getExercise().getIdExercise(),
                        pse.getParameter(),
                        pse.getRangeRepInf(),
                        pse.getRangeRepSup(),
                        pse.getLateralite().getId(),
                        pse.getEquipment().getId()))
                .toList();

        return new PresetSeanceDTO(presetSeance, voList);
    }

}
