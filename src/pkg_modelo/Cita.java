package pkg_modelo;

public class Cita {
    private int idCita;
    private Paciente paciente;
    private Medico medico;
    private String fecha;
    private String hora;
    private String diagnostico;

    public Cita(int idCita, Paciente paciente, Medico medico, String fecha, String hora, String diagnostico) {
        this.idCita = idCita;
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
        this.hora = hora;
        this.diagnostico = diagnostico;
    }

    public Cita(Paciente paciente, Medico medico, String fecha, String hora, String diagnostico) {
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
        this.hora = hora;
        this.diagnostico = diagnostico;
    }

    public int getIdCita() 
    { return idCita; }
    public Paciente getPaciente() 
    { return paciente; }
    public Medico getMedico() 
    { return medico; }
    public String getFecha()
    { return fecha; }
    public String getHora() 
    { return hora; }
    public String getDiagnostico() 
    { return diagnostico; }
}