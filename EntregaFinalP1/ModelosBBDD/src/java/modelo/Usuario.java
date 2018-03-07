/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Rodrigo
 */
public class Usuario {

    private String id_usuario;
    private String contra;

    public Usuario() {

    }

    public Usuario(String id_usuario, String contra) {
        this.id_usuario = id_usuario;
        this.contra = contra;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

}
