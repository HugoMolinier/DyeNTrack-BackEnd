package com.example.dyeTrack.core.entity.setOfPlannedExercise;

import java.io.Serializable;
import java.util.Objects;

import com.example.dyeTrack.core.entity.setOfPlannedExercise.SetOfPlannedExercise.Side;

public class SetOfPlannedExerciseId implements Serializable {

    private Long plannedExercise;
    private int setOrder;
    private Side side;

    public SetOfPlannedExerciseId() {
    }

    public SetOfPlannedExerciseId(Long plannedExercise, int setOrder, SetOfPlannedExercise.Side side) {
        this.plannedExercise = plannedExercise;
        this.setOrder = setOrder;
        this.side = side;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SetOfPlannedExerciseId))
            return false;
        SetOfPlannedExerciseId that = (SetOfPlannedExerciseId) o;
        return setOrder == that.setOrder &&
                Objects.equals(plannedExercise, that.plannedExercise) &&
                side == that.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(plannedExercise, setOrder, side);
    }

    public Long getPlannedExercise() {
        return plannedExercise;
    }

    public void setPlannedExercise(Long plannedExercise) {
        this.plannedExercise = plannedExercise;
    }

    public int getSetOrder() {
        return setOrder;
    }

    public void setSetOrder(int setOrder) {
        this.setOrder = setOrder;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }
}
