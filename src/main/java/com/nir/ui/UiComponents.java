package com.nir.ui;

import com.nir.ui.reaction.PeriodicElementPicker;
import com.nir.ui.reaction.PeriodicTable;

public final class UiComponents {

    public static PeriodicTable periodicTable() {
        return new PeriodicTable();
    }

    public static PeriodicElementPicker periodicElementPicker() {
        return new PeriodicElementPicker();
    }
}
