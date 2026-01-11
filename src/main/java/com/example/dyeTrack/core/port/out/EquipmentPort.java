package com.example.dyeTrack.core.port.out;

import java.util.List;
import java.util.Optional;

import com.example.dyeTrack.core.entity.Equipment;

public interface EquipmentPort {
    List<Equipment> getAll();

    Optional<Equipment> getById(Long idEquipment);
    void save(Equipment equipment);

}
