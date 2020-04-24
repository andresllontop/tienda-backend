/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author andres
 */
public class UtilDateApp {

    public static LocalDate getLocalDate(java.sql.Date date) {
        return date == null ? null : date.toLocalDate();
    }

    public static java.sql.Date getDate(LocalDate localDate) {
        return localDate == null ? null : java.sql.Date.valueOf(localDate);
    }

    public static LocalDateTime getLocalDateTime(java.sql.Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    public static java.sql.Timestamp getTimestamp(LocalDateTime localDateTime) {
        return localDateTime == null ? null : java.sql.Timestamp.valueOf(localDateTime);
    }

    public static java.sql.Date ParseFecha(String fecha) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date utilDate = formato.parse(fecha);
        return new java.sql.Date(utilDate.getTime());
    }

    public static String getStringDate(String character_sep, String date) {
        String values[] = date.split(character_sep);
        return values[2] + "-" + values[1] + "-" + values[0];
    }

    public static String getDate(String fechai) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
