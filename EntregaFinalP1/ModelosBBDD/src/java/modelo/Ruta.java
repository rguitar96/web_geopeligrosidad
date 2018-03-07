/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo
 */
public class Ruta {

    private int id_ruta;
    private String id_usuario;
    private ArrayList<Punto> puntos;
    private double distancia;
    private double peligrosidad;

    public double getPeligrosidad() {
        return peligrosidad;
    }

    public void setPeligrosidad(double peligrosidad) {
        this.peligrosidad = peligrosidad;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public Ruta(int id_ruta, String id_usuario, ArrayList<Punto> puntos, double distancia) {
        this.id_ruta = id_ruta;
        this.id_usuario = id_usuario;
        this.puntos = puntos;
        this.distancia = distancia;
    }
    
    public Ruta(int id_ruta, String id_usuario, ArrayList<Punto> puntos) {
        this.id_ruta = id_ruta;
        this.id_usuario = id_usuario;
        this.puntos = puntos;
    }

    public ArrayList<Punto> getPuntos() {
        return puntos;
    }

    public void setPuntos(ArrayList<Punto> puntos) {
        this.puntos = puntos;
    }

    public int getId_ruta() {
        return id_ruta;
    }

    public void setId_ruta(int id_ruta) {
        this.id_ruta = id_ruta;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

}
