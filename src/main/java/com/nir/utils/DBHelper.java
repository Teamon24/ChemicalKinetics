package com.nir.utils;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.ArrayList;

public class DBHelper {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String user = "lesh";
    private static final String password = "explosive23";

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }

    public ArrayList<String> getReactions(long id) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet elRs = stmt.executeQuery("select formula from elementaryreaction where reaction_id = " + id + " order by el_reaction_id asc ");
        ArrayList<String> elEqs = new ArrayList<>();
        while (elRs.next()) {
            elEqs.add(elRs.getString(1).replaceAll(" ", ""));
        }
        conn.close();
        return elEqs;
    }

    public ArrayList<ComboBoxItem> getComplexReactions() throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select reaction_id, formula FROM complexreaction ORDER BY reaction_id DESC");
        ArrayList<ComboBoxItem> equations = new ArrayList<>();
        while (rs.next()) {
            equations.add(new ComboBoxItem(rs.getLong(1), rs.getString(2)));
        }
        conn.close();
        return equations;
    }

    public void insertReaction(String formula, ObservableList<Node> children) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        PreparedStatement preparedStmt = conn.prepareStatement("insert into complexreaction (formula, type) values (?, ?)");
        preparedStmt.setString(1, formula);
        preparedStmt.setString(2, "type");
        preparedStmt.execute();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select reaction_id FROM complexreaction ORDER BY reaction_id DESC LIMIT 1;");
        if (rs.first()) {
            long id = rs.getLong(1);
            for (Node node : children) {
                TextField textField = (TextField) node;
                preparedStmt = conn.prepareStatement("insert into elementaryreaction (formula, reaction_id) values (?, ?)");
                preparedStmt.setString(1, textField.getText());
                preparedStmt.setLong(2, id);
                preparedStmt.execute();
            }
        }
        conn.close();
    }
}
