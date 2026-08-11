/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 package pkg_modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author uli08
 */
public class DAOHospital {
   
    private final String URL = "jdbc:mysql://localhost:3306/hospital";
    private final String USER = "root";
    private final String PASS = "root";

    public boolean registrarMedico(Medico medico) {
        String sql = "INSERT INTO Medico (nombre, apellido, telefono, cedula, especialidad, horario) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, medico.getNombre());
            ps.setString(2, medico.getApellido());
            ps.setString(3, medico.getTelefono());
            ps.setString(4, medico.getCedula());
            ps.setString(5, medico.getEspecialidad());
            ps.setString(6, medico.getHorario());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar médico: " + e.getMessage());
            return false;
        }
    }

    public List<Medico> consultarMedicos() {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Medico";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Medico m = new Medico(
                    rs.getInt("id_medico"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    0,
                    rs.getString("telefono"),
                    rs.getString("cedula"),
                    rs.getString("especialidad"),
                    rs.getString("horario")
                );
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar médicos: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarMedico(int idMedico) {
        String sql = "DELETE FROM Medico WHERE id_medico = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMedico);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar médico: " + e.getMessage());
            return false;
        }
    }
    //Metodo para Consultar a los pacientes jiji
    public List<Paciente> consultarPacientes() {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Paciente";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Paciente p = new Paciente(
                    rs.getInt("id_paciente"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getInt("edad"),
                    rs.getString("telefono"),
                    rs.getString("alergias"), 
                    rs.getString("tiposSangre") 
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar los pacientes: " + e.getMessage());
        }
        return lista;
    }
    //Metodo para agregar a los pacientes!
    public boolean registrarPaciente(Paciente paciente) {
        String sql = "INSERT INTO Paciente (nombre, apellido, edad, telefono, alergias, tiposSangre) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setInt(3, paciente.getEdad());
            ps.setString(4, paciente.getTelefono());
            ps.setString(5, paciente.getAlergias()); 
            ps.setString(6, paciente.getTipoSangre());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Ha ocurrido un error al registrar el paciente: " + e.getMessage());
            return false;
        }
    }
    //Metodo para eliminar a los pacientibiris
    public boolean eliminarPaciente(int idPaciente) {
        String sql = "DELETE FROM Paciente WHERE id_paciente = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPaciente);
            ps.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("¡Ups! Error al eliminar el paciente: " + e.getMessage());
            return false;
        }
    }
}
