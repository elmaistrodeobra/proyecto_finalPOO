/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg_modelo;

/**
 *
 * @author uli08
 */

public class Medico extends Persona {
    private String cedula;
    private String especialidad;
    private String horario;

    public Medico(int i, String n, String a, int e, String t, String cedula, String especialidad, String horario) {
        super(i, n, a, e, t);
        this.cedula = cedula;
        this.especialidad = especialidad;
        this.horario = horario;
    }

    public String mostrarPerfil() {
        return "DrM. " + nombre + " " + apellido + " - " + especialidad;
    }
    
    @Override
    public String toString() {
        return "Dr. " + nombre + " " + apellido + " - " + especialidad;
    }

    public String getCedula() { return cedula; }
    public String getEspecialidad() { return especialidad; }
    public String getHorario() { return horario; }
}
