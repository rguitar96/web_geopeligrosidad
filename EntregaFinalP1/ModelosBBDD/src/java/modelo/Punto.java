/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.Date;

/**
 *
 * @author Rodrigo
 */
public class Punto {

    private int id_punto;
    private int id_ruta;
    private double latitud;
    private double longitud;

    public Punto(int id_punto, int id_ruta, double latitud, double longitud) {
        this.id_punto = id_punto;
        this.id_ruta = id_ruta;
        this.latitud = latitud;
        this.longitud = longitud;
    }
    

    public int getId_punto() {
        return id_punto;
    }

    public void setId_punto(int id_punto) {
        this.id_punto = id_punto;
    }

    public int getId_ruta() {
        return id_ruta;
    }

    public void setId_ruta(int id_ruta) {
        this.id_ruta = id_ruta;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

}
