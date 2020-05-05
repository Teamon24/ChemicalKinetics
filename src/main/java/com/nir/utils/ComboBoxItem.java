package com.nir.utils;

public class ComboBoxItem {
    private Long id;
    private String formula;

    public ComboBoxItem(Long id, String formula) {
        this.id = id;
        this.formula = formula;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    @Override
    public String toString() {
        return formula;
    }
}
