package pkg_controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import pkg_modelo.DAOHospital;
import pkg_modelo.HorarioOcupadoException;
import pkg_modelo.Medico;
import pkg_modelo.Paciente;
import pkg_modelo.Cita;
import pkg_vista.*;

public class hospitalControl implements ActionListener {

    private DAOHospital dao;

    private vistaMenu vMenu;
    private vistaCita vCita;
    private vistaMedico vMedico;
    private vistaPaciente vPaciente;
    private vistaTablaCita vTablaCitas;
    private vistaTablaMedicos vTablaMedicos;
    private vistaTablaPacientes vTablaPacientes;

    public hospitalControl(vistaMenu vMenu) {
        this.dao = new DAOHospital();
        this.vMenu = vMenu;

        this.vMenu.getBtnNuevaCita().addActionListener(this);
        this.vMenu.getBtnNuevoMedico().addActionListener(this);
        this.vMenu.getBtnNuevoPaciente().addActionListener(this);
        this.vMenu.getBtnVerCitas().addActionListener(this);
        this.vMenu.getBtnVerMedicos().addActionListener(this);
        this.vMenu.getBtnVerPacientes().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object origen = e.getSource();
        
        if (origen == vMenu.getBtnNuevaCita()) {
            if (vCita == null) {
                vCita = new vistaCita();
                vCita.getBtnAgendar().addActionListener(this);
                vCita.getBtnVolver().addActionListener(this);
            }
            cargarCombosCita();
            vCita.setVisible(true);
            vMenu.setVisible(false);
        }

        else if (origen == vMenu.getBtnNuevoMedico()) {
            if (vMedico == null) {
                vMedico = new vistaMedico();
                vMedico.getBtnGuardar().addActionListener(this);
                vMedico.getBtnVolver().addActionListener(this);
            }
            vMedico.setVisible(true);
            vMenu.setVisible(false);
        }

        else if (origen == vMenu.getBtnNuevoPaciente()) {
            if (vPaciente == null) {
                vPaciente = new vistaPaciente();
                vPaciente.getBtnGuardarpaciente().addActionListener(this);
                vPaciente.getBtnVolver().addActionListener(this);
            }
            vPaciente.setVisible(true);
            vMenu.setVisible(false);
        }

        else if (origen == vMenu.getBtnVerCitas()) {
            if (vTablaCitas == null) {
                vTablaCitas = new vistaTablaCita();
                vTablaCitas.getBtnVolver().addActionListener(this);
            }
            llenarTablaCitas();
            vTablaCitas.setVisible(true);
            vMenu.setVisible(false);
        }

        else if (origen == vMenu.getBtnVerMedicos()) {
            if (vTablaMedicos == null) {
                vTablaMedicos = new vistaTablaMedicos();
                vTablaMedicos.getBtnVolver().addActionListener(this);
            }
            llenarTablaMedicos();
            vTablaMedicos.setVisible(true);
            vMenu.setVisible(false);
        }

        else if (origen == vMenu.getBtnVerPacientes()) {
            if (vTablaPacientes == null) {
                vTablaPacientes = new vistaTablaPacientes();
                
                // CORRECCIÓN 1: Se usa "this" para estandarizar el controlador
                vTablaPacientes.getBtnVolver().addActionListener(this);

                vTablaPacientes.getBtnEliminarPa().addActionListener(evt -> eliminarPaciente());
                vTablaPacientes.getTblPacientes().getSelectionModel().addListSelectionListener(evt -> seleccionarPaciente());
            }
            llenarTablaPacientes();
            vTablaPacientes.setVisible(true);
            vMenu.setVisible(false);
        }

        else if (vCita != null && origen == vCita.getBtnVolver()) {
            vCita.setVisible(false);
            vMenu.setVisible(true);
        }
        else if (vMedico != null && origen == vMedico.getBtnVolver()) {
            vMedico.setVisible(false);
            vMenu.setVisible(true);
        }
        else if (vPaciente != null && origen == vPaciente.getBtnVolver()) {
            vPaciente.setVisible(false);
            vMenu.setVisible(true);
        }
        else if (vTablaCitas != null && origen == vTablaCitas.getBtnVolver()) {
            vTablaCitas.setVisible(false);
            vMenu.setVisible(true);
        }
        else if (vTablaMedicos != null && origen == vTablaMedicos.getBtnVolver()) {
            vTablaMedicos.setVisible(false);
            vMenu.setVisible(true);
        }
        // CORRECCIÓN 2: Faltaba esta validación para atrapar el clic de volver en pacientes
        else if (vTablaPacientes != null && origen == vTablaPacientes.getBtnVolver()) {
            vTablaPacientes.setVisible(false);
            vMenu.setVisible(true);
        }

        else if (vCita != null && origen == vCita.getBtnAgendar()) {
            procesarAgendarCita();
        }
        else if (vMedico != null && origen == vMedico.getBtnGuardar()) {
            procesarGuardarMedico();
        }
        else if (vPaciente != null && origen == vPaciente.getBtnGuardarpaciente()) {
            procesarGuardarPaciente();
        }
    }

    private void cargarCombosCita() {
        vCita.getCbPaciente().removeAllItems();
        for (Paciente p : dao.consultarPacientes()) {
            vCita.getCbPaciente().addItem(p);
        }

        vCita.getCbMedico().removeAllItems();
        for (Medico m : dao.consultarMedicos()) {
            vCita.getCbMedico().addItem(m);
        }
    }

    private void llenarTablaCitas() {
        String[] columnas = {"ID Cita", "Paciente", "Médico", "Fecha", "Hora", "Diagnóstico"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        try {
            for (Cita c : dao.obtenerTodasLasCitas()) {
                modelo.addRow(new Object[]{
                    c.getIdCita(),
                    c.getPaciente().getNombre() + " " + c.getPaciente().getApellido(),
                    c.getMedico().getNombre() + " " + c.getMedico().getApellido(),
                    c.getFecha(),
                    c.getHora(),
                    c.getDiagnostico()
                });
            }
        } catch (SQLException ex) {
            System.err.println("Error al llenar tabla citas: " + ex.getMessage());
        }

        vTablaCitas.getTblCitas().setModel(modelo);
    }

    private void procesarAgendarCita() {
        try {
            Paciente p = (Paciente) vCita.getCbPaciente().getSelectedItem();
            Medico m = (Medico) vCita.getCbMedico().getSelectedItem();
            String fecha = vCita.getTxtFecha().getText();
            String hora = vCita.getTxtHora().getText();

            if (dao.verificarHorarioOcupado(m.getId(), fecha, hora)) {
                throw new HorarioOcupadoException("El médico seleccionado ya tiene una cita el " + fecha + " a las " + hora);
            }

            Cita nuevaCita = new Cita(p, m, fecha, hora, "Pendiente de consulta");
            dao.insertarCita(nuevaCita);
            
            JOptionPane.showMessageDialog(vCita, "Cita agendada con éxito.");
            vCita.setVisible(false);
            vMenu.setVisible(true);

        } catch (HorarioOcupadoException ex) {
            JOptionPane.showMessageDialog(vCita, ex.getMessage(), "Error de Horario", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vCita, "Error de BD: " + ex.getMessage());
        }
    }

    private void llenarTablaMedicos() {
        String[] columnas = {"ID", "Nombre", "Apellido", "Teléfono", "Cédula", "Especialidad", "Horario"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Medico m : dao.consultarMedicos()) {
            modelo.addRow(new Object[]{
                m.getId(),
                m.getNombre(),
                m.getApellido(),
                m.getTelefono(),
                m.getCedula(),
                m.getEspecialidad(),
                m.getHorario()
            });
        }

        vTablaMedicos.getTblMedicos().setModel(modelo);
    }

    private void procesarGuardarMedico() {
        String nom = vMedico.getTxtNombre().getText();
        String ape = vMedico.getTxtApellido().getText();
        String tel = vMedico.getTxtTelefono().getText();
        String ced = vMedico.getTxtCedula().getText();
        String esp = vMedico.getTxtEspecialidad().getText();
        String hor = vMedico.getTxtHorario().getText();

        Medico m = new Medico(0, nom, ape, 0, tel, ced, esp, hor);
        if (dao.registrarMedico(m)) {
            JOptionPane.showMessageDialog(vMedico, "Médico registrado con éxito.");
            vMedico.setVisible(false);
            vMenu.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(vMedico, "Error al registrar médico.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void llenarTablaPacientes() {
        String[] columnas = {"ID", "Nombre", "Apellido", "Edad", "Teléfono", "Alergias", "Tipo Sangre"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Paciente p : dao.consultarPacientes()) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getApellido(),
                p.getEdad(),
                p.getTelefono(),
                p.getAlergias(),
                p.getTipoSangre()
            });
        }

        vTablaPacientes.getTblPacientes().setModel(modelo);
    }

    private void procesarGuardarPaciente() {
        try {
            String nom = vPaciente.getTxtNombre().getText().trim();
            String ape = vPaciente.getTxtApellido().getText().trim();
            String edadTexto = vPaciente.getTxtEdad().getText().trim();
            String tel = vPaciente.getTxtTelefono().getText().trim();
            String alergias = vPaciente.getTxtAlergias().getText().trim();
            String tipoSangre = vPaciente.getTxtSangre().getText().trim();

            if (nom.isEmpty() || ape.isEmpty() || edadTexto.isEmpty() || tel.isEmpty() || alergias.isEmpty() || tipoSangre.isEmpty()) {
                JOptionPane.showMessageDialog(vPaciente, "Por favor llena todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int edad = Integer.parseInt(edadTexto);
            Paciente p = new Paciente(0, nom, ape, edad, tel, alergias, tipoSangre);

            boolean guardadoExitoso = dao.registrarPaciente(p);

            if (guardadoExitoso) {
                JOptionPane.showMessageDialog(vPaciente, "¡Paciente registrado con éxito!.");
                
                vPaciente.getTxtNombre().setText("");
                vPaciente.getTxtApellido().setText("");
                vPaciente.getTxtEdad().setText("");
                vPaciente.getTxtTelefono().setText("");
                vPaciente.getTxtAlergias().setText("");
                vPaciente.getTxtSangre().setText("");

                vPaciente.setVisible(false);
                vMenu.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(vPaciente, "Uyyy, ha habido un error al guardar en mysql. Aguas.", "Error BD", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vPaciente, "La edad debe ser un número entero, traviesillo.", "Error de formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void seleccionarPaciente() {
        int fila = vTablaPacientes.getTblPacientes().getSelectedRow();
        if (fila >= 0) {
            if (vPaciente == null) {
                vPaciente = new vistaPaciente();
                vPaciente.getBtnGuardarpaciente().addActionListener(this);
                vPaciente.getBtnVolver().addActionListener(this);
            }
            
            String nombre = vTablaPacientes.getTblPacientes().getValueAt(fila, 1).toString();
            String apellido = vTablaPacientes.getTblPacientes().getValueAt(fila, 2).toString();
            String edad = vTablaPacientes.getTblPacientes().getValueAt(fila, 3).toString();
            String telefono = vTablaPacientes.getTblPacientes().getValueAt(fila, 4).toString();
            String alergias = vTablaPacientes.getTblPacientes().getValueAt(fila, 5).toString();
            String tipoSangre = vTablaPacientes.getTblPacientes().getValueAt(fila, 6).toString();
            
            vPaciente.getTxtNombre().setText(nombre);
            vPaciente.getTxtApellido().setText(apellido);
            vPaciente.getTxtEdad().setText(edad);
            vPaciente.getTxtTelefono().setText(telefono);
            vPaciente.getTxtAlergias().setText(alergias);
            vPaciente.getTxtSangre().setText(tipoSangre);
        }
    }

    private void eliminarPaciente() {
        try {
            int fila = vTablaPacientes.getTblPacientes().getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(vTablaPacientes, "Selecciona un paciente de la tabla para eliminarlo.");
                return;
            }

            int idPaciente = Integer.parseInt(vTablaPacientes.getTblPacientes().getValueAt(fila, 0).toString());
            String nombre = vTablaPacientes.getTblPacientes().getValueAt(fila, 1).toString();

            int confirmacion = JOptionPane.showConfirmDialog(vTablaPacientes,
                    "¿Estás seguro de que quieres eliminar a " + nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                dao.eliminarPaciente(idPaciente); 
                
                if (vPaciente != null) {
                    vPaciente.getTxtNombre().setText("");
                    vPaciente.getTxtApellido().setText("");
                    vPaciente.getTxtEdad().setText("");
                    vPaciente.getTxtTelefono().setText("");
                    vPaciente.getTxtAlergias().setText("");
                    vPaciente.getTxtSangre().setText("");
                }
                
                JOptionPane.showMessageDialog(vTablaPacientes, "El paciente ha sido eliminado.");
                llenarTablaPacientes(); 
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vTablaPacientes, "Error al eliminar: " + ex.getMessage());
        }
    }
}