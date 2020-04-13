package com.nir.utils.math;

import com.nir.utils.InitUtils;

import java.util.ArrayList;

public class Matrix2<T> {
    Type type;
    Integer rows;
    Integer columns;

    Object[][] elements;

    public Matrix2(Type<T> type, ArrayList<ArrayList<T>> elements)  {
        this.type = type;
        this.rows = elements.size();
        this.columns = elements.get(0).size();
        this.elements = new Object[rows][columns];
        for (int i = 0; i < rows; i++) {
            final ArrayList<T> rows = elements.get(i);
            for (int j = 0; j < columns; j++) {
                this.elements[i][j] = rows.get(j);
            }
        }
    }

    public Matrix2(Type<T> type, Object[][] elements) {
        this.type = type;
        this.rows = elements.length;
        this.columns = elements[0].length;
        this.elements = elements;
    }

    public T[] get(Integer row)  {
        return (T[]) elements[row];
    }

    public Matrix2<T> transpose() {
        Object[][] transposed = new Object[columns][rows];
        this.elements = new Object[rows][columns];
        for (int i = 0; i < rows; i++) {
            final Object[] rows = elements[i];
            for (int j = 0; j < columns; j++) {
                transposed[j][i] = rows[j];
            }
        }
        return new Matrix2(type, transposed);
    }
}
