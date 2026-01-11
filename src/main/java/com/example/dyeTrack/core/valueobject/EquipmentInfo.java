package com.example.dyeTrack.core.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public class EquipmentInfo {

    private Long idEquipment;
    private boolean isDefault = true;

    public EquipmentInfo() {
    }

    public EquipmentInfo(Long idEquipment, boolean isDefault) {
        this.idEquipment = idEquipment;
        this.isDefault = isDefault;
    }

    public Long getIdEquipment() {
        return idEquipment;
    }

    public void setIdEquipment(Long idEquipment) {
        this.idEquipment = idEquipment;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
