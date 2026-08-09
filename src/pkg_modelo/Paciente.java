/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg_modelo;

/**
 *
 * @author uli08
 */
public class Paciente extends Persona {
    protected String alergias;
    protected String tipoSangre;
    
    public Paciente(int i, String n, String a, int e, String t) {
        super(i, n, a, e, t);
    }
    
    @Override
    public String toString() {
        return nombre + " " + apellido + " " + alergias + " " + tipoSangre;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }
    
}
