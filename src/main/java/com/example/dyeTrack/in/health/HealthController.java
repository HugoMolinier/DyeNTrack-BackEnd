package com.example.dyeTrack.in.health;

import com.example.dyeTrack.in.infoExerciseUser.dto.out.ReturnInfoExerciseUserDTO;
import com.example.dyeTrack.in.utils.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/health")
class HealthController {

    @GetMapping
    public ResponseEntity<ResponseBuilder.ResponseDTO<String>> checkHealth() {
        return ResponseBuilder.success("","API is ON");
    }
}

