package com.example.dyeTrack.in.exercise.dto;

public class EquipmentInfoReturnDTO {

    private Long idEquipment;

    public EquipmentInfoReturnDTO() {}

    public EquipmentInfoReturnDTO(Long idEquipment) {
        this.idEquipment = idEquipment;
    }

    public Long getIdEquipment() {
        return idEquipment;
    }

    public void setIdEquipment(Long idEquipment) {
        this.idEquipment = idEquipment;
    }
}
