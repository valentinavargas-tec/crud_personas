package ceiba.com.co.cita.modelo.entidad;

import ceiba.com.co.ValidadorArgumento;
import ceiba.com.co.excepcion.ExcepcionReglaNegocio;
import ceiba.com.co.excepcion.ExcepcionValorInvalido;

import java.time.LocalDateTime;

public class Cita {

    private static final String FECHA_HORA_OBLIGATORIA = "La fecha y hora de la cita es obligatoria";
    private static final String FECHA_DEBE_SER_FUTURA = "La fecha y hora de la cita médica debe ser una fecha futura";
    private static final String CANCELACION_INVALIDA = "No es posible cancelar la cita médica porque la hora programada ya transcurrió o la cita no se encuentra en estado programado.";
    private static final String TRANSICION_INVALIDA = "No se permite la transición de estado de %s a %s.";
    private static final String REPROGRAMACION_INVALIDA_ESTADO = "Solo se pueden reprogramar citas médicas en estado PROGRAMADA.";

    private final Long id;
    private final String pacienteDocumento;
    private String doctorDocumento;
    private LocalDateTime fechaHora;
    private TipoCita tipoCita;
    private EstadoCita estado;
    private String motivo;
    private String observaciones;

    private Cita(Builder builder) {
        this.id = builder.id;
        this.pacienteDocumento = builder.pacienteDocumento;
        this.doctorDocumento = builder.doctorDocumento;
        this.fechaHora = builder.fechaHora;
        this.tipoCita = builder.tipoCita;
        this.estado = builder.estado != null ? builder.estado : EstadoCita.PROGRAMADA;
        this.motivo = builder.motivo;
        this.observaciones = builder.observaciones;

        ValidadorArgumento.validarObligatorio(this.pacienteDocumento, "El documento del paciente es obligatorio");
        ValidadorArgumento.validarObligatorio(this.doctorDocumento, "El documento del doctor es obligatorio");
        ValidadorArgumento.validarObligatorio(this.tipoCita, "El tipo de cita es obligatorio");
        ValidadorArgumento.validarObligatorio(this.fechaHora, FECHA_HORA_OBLIGATORIA);
        if (this.id == null) {
            validarFechaFutura(this.fechaHora);
        }
    }

    private void validarFechaFutura(LocalDateTime fechaHora) {
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new ExcepcionValorInvalido(FECHA_DEBE_SER_FUTURA);
        }
    }

    public void reprogramar(LocalDateTime nuevaFechaHora, TipoCita nuevoTipoCita, String nuevoMotivo) {
        if (!EstadoCita.PROGRAMADA.equals(this.estado) && !EstadoCita.REASIGNADA.equals(this.estado)) {
            throw new ExcepcionReglaNegocio(REPROGRAMACION_INVALIDA_ESTADO);
        }
        ValidadorArgumento.validarObligatorio(nuevaFechaHora, FECHA_HORA_OBLIGATORIA);
        validarFechaFutura(nuevaFechaHora);
        this.fechaHora = nuevaFechaHora;
        if (nuevoTipoCita != null) {
            this.tipoCita = nuevoTipoCita;
        }
        if (nuevoMotivo != null && !nuevoMotivo.isBlank()) {
            this.motivo = nuevoMotivo;
        }
    }

    public void cancelar() {
        cancelar(null);
    }

    public void cancelar(String motivoCancelacion) {
        if ((!EstadoCita.PROGRAMADA.equals(this.estado) && !EstadoCita.REASIGNADA.equals(this.estado))
                || LocalDateTime.now().isAfter(this.fechaHora)) {
            throw new ExcepcionReglaNegocio(CANCELACION_INVALIDA);
        }
        this.estado = EstadoCita.CANCELADA;
        if (motivoCancelacion != null && !motivoCancelacion.isBlank()) {
            this.observaciones = (this.observaciones == null || this.observaciones.isBlank())
                    ? "Cancelada: " + motivoCancelacion
                    : this.observaciones + " | Cancelada: " + motivoCancelacion;
        }
    }

    public void reasignar(String nuevoDoctorDocumento) {
        reasignar(nuevoDoctorDocumento, null);
    }

    public void reasignar(String nuevoDoctorDocumento, LocalDateTime nuevaFechaHora) {
        if (!EstadoCita.PROGRAMADA.equals(this.estado) && !EstadoCita.REASIGNADA.equals(this.estado)) {
            throw new ExcepcionReglaNegocio(String.format(TRANSICION_INVALIDA, this.estado, EstadoCita.REASIGNADA));
        }
        ValidadorArgumento.validarObligatorio(nuevoDoctorDocumento, "El documento del doctor es obligatorio");
        if (nuevaFechaHora != null) {
            validarFechaFutura(nuevaFechaHora);
            this.fechaHora = nuevaFechaHora;
        } else if (this.fechaHora.isBefore(LocalDateTime.now())) {
            throw new ExcepcionReglaNegocio("No es posible reasignar la cita médica porque la fecha y hora programada ya transcurrió.");
        }
        this.doctorDocumento = nuevoDoctorDocumento;
        this.estado = EstadoCita.REASIGNADA;
        this.observaciones = (this.observaciones == null || this.observaciones.isBlank())
                ? "Reasignada a doctor: " + nuevoDoctorDocumento
                : this.observaciones + " | Reasignada a doctor: " + nuevoDoctorDocumento;
    }

    public void iniciarAtencion() {
        if (!EstadoCita.PROGRAMADA.equals(this.estado) && !EstadoCita.REASIGNADA.equals(this.estado)) {
            throw new ExcepcionReglaNegocio(String.format(TRANSICION_INVALIDA, this.estado, EstadoCita.EN_ATENCION));
        }
        this.estado = EstadoCita.EN_ATENCION;
    }

    public void completar() {
        if (!EstadoCita.EN_ATENCION.equals(this.estado)) {
            throw new ceiba.com.co.excepcion.ExcepcionReglaNegocio(String.format(TRANSICION_INVALIDA, this.estado, EstadoCita.COMPLETADA));
        }
        this.estado = EstadoCita.COMPLETADA;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getPacienteDocumento() {
        return pacienteDocumento;
    }

    public String getDoctorDocumento() {
        return doctorDocumento;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public TipoCita getTipoCita() {
        return tipoCita;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public static class Builder {
        private Long id;
        private String pacienteDocumento;
        private String doctorDocumento;
        private LocalDateTime fechaHora;
        private TipoCita tipoCita;
        private EstadoCita estado;
        private String motivo;
        private String observaciones;

        public Builder conId(Long id) {
            this.id = id;
            return this;
        }

        public Builder conPacienteDocumento(String pacienteDocumento) {
            this.pacienteDocumento = pacienteDocumento;
            return this;
        }

        public Builder conDoctorDocumento(String doctorDocumento) {
            this.doctorDocumento = doctorDocumento;
            return this;
        }

        public Builder conFechaHora(LocalDateTime fechaHora) {
            this.fechaHora = fechaHora;
            return this;
        }

        public Builder conTipoCita(TipoCita tipoCita) {
            this.tipoCita = tipoCita;
            return this;
        }

        public Builder conEstado(EstadoCita estado) {
            this.estado = estado;
            return this;
        }

        public Builder conMotivo(String motivo) {
            this.motivo = motivo;
            return this;
        }

        public Builder conObservaciones(String observaciones) {
            this.observaciones = observaciones;
            return this;
        }

        public Cita build() {
            return new Cita(this);
        }
    }
}
