package com.example.dyeTrack.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.example.dyeTrack.in.presetSeance.dto.PresetSeanceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciseVO;
import com.example.dyeTrack.in.exercise.dto.ExerciseDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
import com.example.dyeTrack.out.presetSeance.PresetSeanceRepository;
import com.example.dyeTrack.out.user.UserRepository;
import com.example.dyeTrack.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
@Transactional
public class PresetSeanceControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PresetSeanceRepository presetSeanceRepository;

        private String tokenUser1;

        @BeforeEach
        void setUp() throws Exception {
                userRepository.deleteAll();
                tokenUser1 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "da5d");
        }

        @Test
        void testsavePreset_success() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceDTO presetSeance = new PresetSeanceDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), 6, 10, 15, 1L,
                                                1L)));

                PresetSeanceDTO presetSeanceex = TestUtils.savePreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);

                assertThat(presetSeanceex).isNotNull();
                assertThat(presetSeanceex.getName()).isEqualTo(presetSeance.getName());
                assertThat(presetSeanceex.getPresetSeanceExerciseVO()).hasSize(1);
                assertThat(presetSeanceex.getPresetSeanceExerciseVO().get(0).getIdExercise())
                                .isEqualTo(createdexe.getIdExercise());


                assertThat(presetSeanceex.getPresetSeanceExerciseVO().get(0).getIdLateralite())
                                .isEqualTo(1L);
                assertThat(presetSeanceex.getPresetSeanceExerciseVO().get(0).getIdEquipment())
                                .isEqualTo(1L);
        }

        @Test
        void testUpdatePreset_success() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceDTO presetSeance = new PresetSeanceDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), 5, 10, 15, 1L,
                                                1L)));
                PresetSeanceDTO presetSeanceex = TestUtils.savePreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);

                presetSeanceex.setName("Not Push");


                PresetSeanceDTO presetSeanceex1 = TestUtils.savePreset(mockMvc, objectMapper, tokenUser1,
                        presetSeanceex);

                assertThat(presetSeanceex1).isNotNull();
                assertThat(presetSeanceex1.getName()).isEqualTo(presetSeanceex.getName());
                assertThat(presetSeanceex1.getPresetSeanceExerciseVO()).hasSize(1);
                assertThat(presetSeanceex1.getPresetSeanceExerciseVO().get(0).getIdExercise())
                                .isEqualTo(createdexe.getIdExercise());


                assertThat(presetSeanceex1.getPresetSeanceExerciseVO().get(0).getIdLateralite())
                                .isEqualTo(1L);
                assertThat(presetSeanceex1.getPresetSeanceExerciseVO().get(0).getIdEquipment())
                                .isEqualTo(1L);

                // ====

                ExerciseDetailReturnDTO createdexe2 = createSampleExercise("traction");

                presetSeanceex1.setName("Push");
                presetSeanceex1.setPresetSeanceExerciseVO(
                                List.of(new PresetSeanceExerciseVO(createdexe2.getIdExercise(), 6, 9, 14, 1L,
                                                1L),
                                                new PresetSeanceExerciseVO(createdexe.getIdExercise(), 3, 7,
                                                                12, 2L,
                                                                2L)));



                PresetSeanceDTO presetSeanceex2 = TestUtils.savePreset(mockMvc, objectMapper, tokenUser1,
                        presetSeanceex1);

                assertThat(presetSeanceex2).isNotNull();
                assertThat(presetSeanceex2.getName()).isEqualTo(presetSeanceex1.getName());
                assertThat(presetSeanceex2.getPresetSeanceExerciseVO()).hasSize(2);
                assertThat(presetSeanceex2.getPresetSeanceExerciseVO().get(0).getIdExercise())
                                .isEqualTo(createdexe2.getIdExercise());

                assertThat(presetSeanceex2.getPresetSeanceExerciseVO().get(1).getIdExercise())
                                .isEqualTo(createdexe.getIdExercise());



                assertThat(presetSeanceex2.getPresetSeanceExerciseVO().get(0).getNbSet())
                                .isEqualTo(6);
                assertThat(presetSeanceex2.getPresetSeanceExerciseVO().get(0).getRangeRepInf())
                                .isEqualTo(9);
                assertThat(presetSeanceex2.getPresetSeanceExerciseVO().get(0).getRangeRepSup())
                                .isEqualTo(14);

                assertThat(presetSeanceex2.getPresetSeanceExerciseVO().get(0).getIdLateralite())
                                .isEqualTo(1L);
                assertThat(presetSeanceex2.getPresetSeanceExerciseVO().get(0).getIdEquipment())
                                .isEqualTo(1L);

        }

        @Test
        void testGet_success() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceDTO presetSeance = new PresetSeanceDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), 5, 10, 15, 1L,
                                                1L)));

                PresetSeanceDTO savePreset = TestUtils.savePreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);

                String focusResp = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/preset-seances/getById/" + savePreset.getIdPreset())
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                PresetSeanceDTO getPreset = TestUtils.assertAndExtractData(focusResp,
                                "Preset récupéré avec succès", objectMapper,
                        PresetSeanceDTO.class);
                assertThat(focusResp).isNotNull();
                assertThat(getPreset.getName()).isEqualTo(savePreset.getName());
                assertThat(getPreset.getPresetSeanceExerciseVO()).hasSize(1);
                assertThat(getPreset.getPresetSeanceExerciseVO().get(0).getIdExercise())
                                .isEqualTo(createdexe.getIdExercise());

                assertThat(getPreset.getName())
                                .isEqualTo(savePreset.getName());

                assertThat(getPreset.getPresetSeanceExerciseVO().get(0).getIdLateralite())
                                .isEqualTo(1L);
                assertThat(getPreset.getPresetSeanceExerciseVO().get(0).getIdEquipment())
                                .isEqualTo(1L);

        }

        @Test
        void testsavePreset_invalidToken_shouldFail() throws Exception {
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));

                ExerciseDetailReturnDTO createdexe = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                PresetSeanceDTO presetSeance = new PresetSeanceDTO("Pull",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), 5, 7, 12, 1L,
                                                1L)));

                // Token invalide
                mockMvc.perform(post("/api/preset-seances/save")
                                .header("Authorization", "Bearer invalid_token_123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, presetSeance)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testsavePreset_otherUserToken_shouldFail() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceDTO presetSeance = new PresetSeanceDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), 5, 10, 15, 1L,
                                                1L)));

                PresetSeanceDTO createdPreset = TestUtils.savePreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);

                // Création d’un autre utilisateur
                String tokenUser2 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "otherUser");

                createdPreset.setName("Pull Modifié");

                mockMvc.perform(
                                post("/api/preset-seances/save")
                                .header("Authorization", "Bearer " + tokenUser2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, createdPreset)))
                                .andExpect(status().isForbidden());


        }

        @Test
        void testGetPreset_invalidToken_shouldFail() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceDTO presetSeance = new PresetSeanceDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), 5, 10, 15, 1L,
                                                1L)));

                PresetSeanceDTO createdPreset = TestUtils.savePreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);
                // Requête avec mauvais token
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/preset-seances/getById/" + createdPreset.getIdPreset())
                                .header("Authorization", "Bearer wrong_token"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testGetPreset_otherUserToken_shouldFail() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceDTO presetSeance = new PresetSeanceDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), 5, 10, 15, 1L,
                                                1L)));

                PresetSeanceDTO createdPreset = TestUtils.savePreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);

                // Autre utilisateur
                String tokenUser2 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "newUser");

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/preset-seances/getById/" + createdPreset.getIdPreset())
                                .header("Authorization", "Bearer " + tokenUser2))
                                .andExpect(status().isForbidden());

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/preset-seances/getById/" + "898998594")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testDeletePreset_successAndFailures() throws Exception {
                ExerciseDetailReturnDTO createdexe = createSampleExercise("pompe");

                PresetSeanceDTO presetSeance = new PresetSeanceDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdexe.getIdExercise(), 5, 10, 15, 1L,
                                                1L)));

                PresetSeanceDTO createdPreset = TestUtils.savePreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);
                Long idPreset = createdPreset.getIdPreset();

                assertThat(idPreset).isNotNull();

                // Vérifie que le preset existe bien
                assertThat(presetSeanceRepository.findById(idPreset)).isPresent();

                // 2️ Suppression réussie par le bon utilisateur
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/preset-seances/delete/" + idPreset)
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isOk());

                // Vérifie que le preset n’existe plus
                assertThat(presetSeanceRepository.findById(idPreset)).isEmpty();

                // 3️ Tentative de suppression d’un preset inexistant
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/preset-seances/delete/999999")
                                .header("Authorization", "Bearer " + tokenUser1))
                                .andExpect(status().isNotFound());

                // 4️ Recréation d’un preset par user1

                PresetSeanceDTO createdPreset2 = TestUtils.savePreset(mockMvc, objectMapper, tokenUser1,
                                presetSeance);
                String tokenUser2 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "userB");

                // Tentative de suppression par un autre utilisateur
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/preset-seances/delete/" + createdPreset2.getIdPreset())
                                .header("Authorization", "Bearer " + tokenUser2))
                                .andExpect(status().isForbidden());

                // Vérifie que le preset existe toujours
                assertThat(presetSeanceRepository.findById(createdPreset2.getIdPreset())).isPresent();

                // 5️ Tentative sans token
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/preset-seances/delete/" + createdPreset2.getIdPreset()))
                                .andExpect(status().isForbidden());
        }

        @Test
        void testGetAllOfUser_success() throws Exception {
                // 1️⃣ Création de 2 presets pour user1
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));
                ExerciseDetailReturnDTO createdExe = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                PresetSeanceDTO preset1 = new PresetSeanceDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdExe.getIdExercise(), 5, 10, 15, 1L,
                                                1L)));
                PresetSeanceDTO preset2 = new PresetSeanceDTO("Pull Day",
                                List.of(new PresetSeanceExerciseVO(createdExe.getIdExercise(), 5, 10, 15, 1L,
                                                1L)));

                // Création des presets via POST
                mockMvc.perform(post("/api/preset-seances/save")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, preset1)))
                                .andExpect(status().isOk());

                mockMvc.perform(post("/api/preset-seances/save")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, preset2)))
                                .andExpect(status().isOk());

                // 2️⃣ Récupération de tous les presets pour user1
                String response = mockMvc.perform(get("/api/preset-seances/getAll")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                List<PresetSeanceDTO> presets = TestUtils.assertAndExtractDataList(response,
                                "Liste des presets récupérée avec succès", objectMapper,
                        PresetSeanceDTO.class);

                assertThat(presets).hasSize(2);
                assertThat(presets.stream().map(PresetSeanceDTO::getName))
                                .containsExactlyInAnyOrder("Push Day", "Pull Day");

                String tokenUser2 = TestUtils.registerAndGetToken(mockMvc, objectMapper, "userB");

                String responseuser2 = mockMvc.perform(get("/api/preset-seances/getAll")
                                .header("Authorization", "Bearer " + tokenUser2)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                List<PresetSeanceDTO> presets2 = TestUtils.assertAndExtractDataList(responseuser2,
                                "Liste des presets récupérée avec succès", objectMapper,
                        PresetSeanceDTO.class);
                assertThat(presets2).hasSize(0);
        }

        @Test
        void testGetAllOfUser_withNameFilter() throws Exception {
                // Création de presets
                ExerciseCreateDTO dto = TestUtils.buildExercise("Pompes", "Exercise pectoraux",
                                List.of(new MuscleInfo(1L, true), new MuscleInfo(2L, false)));
                ExerciseDetailReturnDTO createdExe = TestUtils.createExercise(mockMvc, objectMapper, tokenUser1, dto);

                PresetSeanceDTO preset1 = new PresetSeanceDTO("Push Day",
                                List.of(new PresetSeanceExerciseVO(createdExe.getIdExercise(), 5, 10, 15, 1L,
                                                1L)));
                PresetSeanceDTO preset2 = new PresetSeanceDTO("Pull Day",
                                List.of(new PresetSeanceExerciseVO(createdExe.getIdExercise(), 5, 10, 15, 1L,
                                                1L)));

                mockMvc.perform(post("/api/preset-seances/save")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, preset1)))
                                .andExpect(status().isOk());

                mockMvc.perform(post("/api/preset-seances/save")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.toJson(objectMapper, preset2)))
                                .andExpect(status().isOk());

                // Filtrage sur "Push"
                String response = mockMvc.perform(get("/api/preset-seances/getAll")
                                .header("Authorization", "Bearer " + tokenUser1)
                                .param("name", "Push")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                List<PresetSeanceDTO> presets = TestUtils.assertAndExtractDataList(response,
                                "Liste des presets récupérée avec succès", objectMapper,
                        PresetSeanceDTO.class);

                assertThat(presets).hasSize(1);
                assertThat(presets.get(0).getName()).isEqualTo("Push Day");
        }

        private ExerciseDetailReturnDTO createSampleExercise(String name) throws Exception {
                return TestUtils.createExercise(mockMvc, objectMapper, tokenUser1,
                                TestUtils.buildExercise(name, "Exercise pectoraux",
                                                List.of(new MuscleInfo(1L, true),
                                                                new MuscleInfo(2L, false))));
        }

}
