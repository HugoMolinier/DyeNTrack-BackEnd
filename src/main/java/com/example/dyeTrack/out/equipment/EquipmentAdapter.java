package com.example.dyeTrack.out.equipment;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.Equipment;
import com.example.dyeTrack.core.port.out.EquipmentPort;

@Component
public class EquipmentAdapter implements EquipmentPort {
    private EquipmentRepository repository;

    public EquipmentAdapter(EquipmentRepository repository) {
        this.repository = repository;
    }

    public List<Equipment> getAll() {
        return repository.findAll();
    }

    public Optional<Equipment> getById (Long id){ return repository.findById(id);}

    @Override
    public void save(Equipment equipment) {
        repository.save(equipment);
    }

}
