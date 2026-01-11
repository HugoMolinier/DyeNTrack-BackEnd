package com.example.dyeTrack.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeanceExercise.PresetSeanceExercise;
import com.example.dyeTrack.core.entity.RelEexerciseEquipment.RelExerciseEquipment;
import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.entity.infoExerciseUser.InfoExerciseUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExercise;

    @NotBlank
    private String nameFR;

    @NotBlank
    private String nameEN;

    @Column(columnDefinition = "TEXT")
    private String descriptionFR;

    @Column(columnDefinition = "TEXT")
    private String descriptionEN;

    @Column(columnDefinition = "TEXT")
    private String linkVideo;

    @ManyToOne
    @JoinColumn(name = "idCreator", nullable = true)
    private User user;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RelExerciseMuscle> relExerciseMuscles = new ArrayList<>();

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PresetSeanceExercise> presetSeanceExercises = new ArrayList<>();

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfoExerciseUser> relRecensementExercises = new ArrayList<>();

    @OneToMany(
            mappedBy = "exercise",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<RelExerciseEquipment> relExerciseEquipments = new ArrayList<>();


    public List<InfoExerciseUser> getRelRecensementExercises() {
        return relRecensementExercises;
    }

    public void setRelRecensementExercises(List<InfoExerciseUser> relRecensementExercises) {
        this.relRecensementExercises = relRecensementExercises;
    }

    public List<RelExerciseEquipment> getRelExerciseEquipments() {
        return relExerciseEquipments;
    }

    public void setRelExerciseEquipments(List<RelExerciseEquipment> relExerciseEquipments) {
        this.relExerciseEquipments = relExerciseEquipments;
    }

    public List<RelExerciseMuscle> getRelExerciseMuscles() {
        return relExerciseMuscles;
    }

    public Exercise() {
    }


    public Exercise(String nameFR, String nameEN, String descriptionFR,String descriptionEN, String linkVideo, User user) {
        this.nameFR = nameFR;
        this.nameEN = nameEN;
        this.descriptionFR = descriptionFR;
        this.descriptionEN = descriptionEN;
        this.linkVideo = linkVideo;
        this.user = user;
    }

    public Long getIdExercise() {
        return idExercise;
    }

    public String getNameFR() {
        return nameFR;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getDescriptionFR() {
        return descriptionFR;
    }

    public String getDescriptionEN() {
        return descriptionEN;
    }

    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String link) {
        this.linkVideo = link;
    }

    public User getUser() {
        return user;
    }

    public void setDescriptionFR(String newDescription) {
        this.descriptionFR = newDescription;
    }

    public void setDescriptionEN(String newDescription) {
        this.descriptionEN = newDescription;
    }

    public void setNameFR(String newNameFR) {
        this.nameFR = newNameFR;
    }

    @Override
    public String toString() {
        return "idExercise" + this.idExercise +
                " : nameFR " + this.nameFR +
                ", user " + this.user;
    }
}
