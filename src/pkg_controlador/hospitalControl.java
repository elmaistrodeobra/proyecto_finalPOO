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
                vTablaPacientes.getBtnVolver().addActionListener(this);
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