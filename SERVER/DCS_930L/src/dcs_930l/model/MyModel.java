/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.model;

import dcs_930l.config.Koneksi;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Dhan
 */
public class MyModel {

    public boolean delete(String table, String where, int value) {
        boolean status_delete = false;
        String query = "delete from " + table + " where " + where + "=?";
        PreparedStatement ps;
        try {
            ps = Koneksi.getKoneksi().prepareStatement(query);
            ps.setInt(1, value);
            status_delete = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(MyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status_delete;
    }

    public boolean delete(String table, String where, String value) {
        boolean status_delete = false;
        String query = "delete from " + table + " where " + where + "=?";
        PreparedStatement ps;
        try {
            ps = Koneksi.getKoneksi().prepareStatement(query);
            ps.setString(1, value);
            status_delete = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(MyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status_delete;
    }

    public String generate_id(String table, String name_id, String first_word) {
        String id = "";
        try {
            Statement select = Koneksi.getKoneksi().createStatement();
            ResultSet rs = select.executeQuery("select MAX(RIGHT(" + name_id + ",6)) as kd_max from " + table);
            if (rs.last()) {
                rs.beforeFirst();
                while (rs.next()) {
                    int temp = rs.getInt(1) + 1;
                    id = String.format("%06d", temp);
                }
                id = first_word + "-" + id;
            } else {
                id = first_word + "-" + "000001";
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public DefaultTableModel readToTable(String tableName, String desc) {
        Vector<Vector<Object>> data = null;
        Vector<String> columnNames = null;
        try {
            String query = "select * from " + tableName + " order by " + desc + " desc";
            Statement st;
            st = Koneksi.getKoneksi().createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            columnNames = new Vector<>();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i).toUpperCase().replace('_', ' '));
            }
            data = new Vector<>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    if (rs.getObject(columnIndex) instanceof Double) {
                        Double obj = rs.getDouble(columnIndex);
                        String number = formatNumberToString(obj);
                        vector.add(number);
                    } else {
                        vector.add(rs.getObject(columnIndex));
                    }
                }
                data.add(vector);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new DefaultTableModel(data, columnNames);
    }

    public String formatNumberToString(Object object) {
        DecimalFormat format = new DecimalFormat("###,###");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        format.setDecimalFormatSymbols(formatRp);
        String number = format.format(object);
        return number;
    }

    public Double formatNumberToDouble(String number) {
        DecimalFormat ff = new DecimalFormat("###,###");
        Double result = null;
        try {
            result = ff.parse(number).doubleValue();
        } catch (ParseException ex) {
            Logger.getLogger(MyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public DefaultTableModel readToTable(String query) {
        Vector<Vector<Object>> data = null;
        Vector<String> columnNames = null;
        try {
            Statement st;
            st = Koneksi.getKoneksi().createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            columnNames = new Vector<>();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i).toUpperCase().replace('_', ' '));
            }
            data = new Vector<>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    if (rs.getObject(columnIndex) instanceof Double) {
                        Double obj = rs.getDouble(columnIndex);
                        String number = formatNumberToString(obj);
                        vector.add(number);
                    } else {
                        vector.add(rs.getObject(columnIndex));
                    }
                }
                data.add(vector);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new DefaultTableModel(data, columnNames);
    }
}
