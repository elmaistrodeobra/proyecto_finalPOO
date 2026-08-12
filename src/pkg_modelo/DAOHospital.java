package pkg_modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    
    // Método para Consultar a los pacientes
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
                    rs.getString("tipoSangre") // CORREGIDO: sin la 's'
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar los pacientes: " + e.getMessage());
        }
        return lista;
    }
    
    // Método para agregar a los pacientes
    public boolean registrarPaciente(Paciente paciente) {
        // CORREGIDO: tiposSangre a tipoSangre
        String sql = "INSERT INTO Paciente (nombre, apellido, edad, telefono, alergias, tipoSangre) VALUES (?, ?, ?, ?, ?, ?)";
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
    
    // Método para eliminar a los pacientes
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
    
    // Método para actualizar a los pacientes
    public boolean actualizarPaciente(Paciente paciente) {
        // CORREGIDO: tiposSangre a tipoSangre
        String sql = "UPDATE Paciente SET nombre = ?, apellido = ?, edad = ?, telefono = ?, alergias = ?, tipoSangre = ? WHERE id_paciente = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setInt(3, paciente.getEdad());
            ps.setString(4, paciente.getTelefono());
            ps.setString(5, paciente.getAlergias());
            ps.setString(6, paciente.getTipoSangre());
            ps.setInt(7, paciente.getId()); 
            
            ps.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("¡Ups! Error al actualizar el paciente: " + e.getMessage());
            return false;
        }
    }
    
    public boolean verificarHorarioOcupado(int id_medico, String fecha, String hora) throws SQLException {
        String sql = "SELECT * FROM CitaMedica WHERE id_medico = ? AND fecha = ? AND hora = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_medico);
            ps.setString(2, fecha);
            ps.setString(3, hora);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    public void insertarCita(Cita cita) throws SQLException {
        String sql = "INSERT INTO CitaMedica (id_paciente, id_medico, fecha, hora, diagnostico) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cita.getPaciente().getId()); 
            ps.setInt(2, cita.getMedico().getId());   
            ps.setString(3, cita.getFecha());
            ps.setString(4, cita.getHora());
            ps.setString(5, cita.getDiagnostico());
            ps.executeUpdate();
        }
    }
    
    public ArrayList<Cita> obtenerTodasLasCitas() throws SQLException {
        ArrayList<Cita> lista = new ArrayList<>();
        // ACTUALIZADO: Para que traiga los 7 campos del paciente incluyendo edad, alergias y sangre
        String sql = "SELECT c.id_cita, c.fecha, c.hora, c.diagnostico, "
                   + "p.id_paciente, p.nombre AS p_nom, p.apellido AS p_ape, p.edad AS p_eda, p.telefono AS p_tel, p.alergias AS p_ale, p.tipoSangre AS p_san, "
                   + "m.id_medico, m.nombre AS m_nom, m.apellido AS m_ape, m.telefono AS m_tel, m.cedula, m.especialidad, m.horario "
                   + "FROM CitaMedica c "
                   + "INNER JOIN Paciente p ON c.id_paciente = p.id_paciente "
                   + "INNER JOIN Medico m ON c.id_medico = m.id_medico";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Paciente pac = new Paciente(
                    rs.getInt("id_paciente"),
                    rs.getString("p_nom"),
                    rs.getString("p_ape"),
                    rs.getInt("p_eda"),
                    rs.getString("p_tel"),
                    rs.getString("p_ale"),
                    rs.getString("p_san")
                );
                Medico med = new Medico(
                    rs.getInt("id_medico"),
                    rs.getString("m_nom"),
                    rs.getString("m_ape"),
                    0,
                    rs.getString("m_tel"),
                    rs.getString("cedula"),
                    rs.getString("especialidad"),
                    rs.getString("horario")
                );
                lista.add(new Cita(rs.getInt("id_cita"), pac, med, rs.getString("fecha"), rs.getString("hora"), rs.getString("diagnostico")));
            }
        }
        return lista;
    }
    
    public boolean actualizarDiagnostico(int idCita, String diagnostico) throws SQLException {
        String sql = "UPDATE CitaMedica SET diagnostico = ? WHERE id_cita = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, diagnostico);
            ps.setInt(2, idCita);
            return ps.executeUpdate() > 0;
        }
    }
}