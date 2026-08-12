/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg_controlador;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pkg_modelo.Cita;
import pkg_modelo.DAOHospital;
import pkg_modelo.Medico;
import pkg_modelo.Paciente;
import pkg_modelo.HorarioOcupadoException;

/**
 *
 * @author uli08
 */
public class citaControlador {
    private DAOHospital dao;

    public citaControlador() {
        this.dao = new DAOHospital();
    }


    public void agendarCita(Paciente paciente, Medico medico, String fecha, String hora) throws HorarioOcupadoException, SQLException {
        
        boolean ocupado = dao.verificarHorarioOcupado(medico.getId(), fecha, hora);
        
        if (ocupado) {
            throw new HorarioOcupadoException("El médico seleccionado ya tiene una cita el " + fecha + " a las " + hora);
        }
        
        Cita nuevaCita = new Cita(paciente, medico, fecha, hora, "Pendiente de consulta");
        dao.insertarCita(nuevaCita);
    }

    public ArrayList<Cita> listarCitas() {
        try {
            return dao.obtenerTodasLasCitas();
        } catch (SQLException e) {
            System.err.println("Error al obtener las citas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean registrarDiagnostico(int idCita, String diagnostico) {
        try {
            return dao.actualizarDiagnostico(idCita, diagnostico);
        } catch (SQLException e) {
            System.err.println("Error al guardar diagnóstico: " + e.getMessage());
            return false;
        }
    }
}
    
    
