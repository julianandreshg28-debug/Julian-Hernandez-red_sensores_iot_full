package structures.management;

import model.LecturaSensor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GestorDeRed - Módulo Integrador de Estructuras de Datos
 * VERSION ESTUDIANTE
 *
 * Objetivo: Implementar la gestión de la red utilizando las colecciones de Java (JCF).
 * El éxito de este módulo no es solo que el código compile, sino que elijas
 * la estructura correcta para cada problema.
 */
public class GestorDeRed {
    // 1. Historial de Eventos: Registro lineal de todas las actividades
    private List<String> historialEventos;

    // 2. Navegador de Alertas: Permite moverse adelante y atrás entre alertas
    private Deque<String> navegadorAlertas;
    private String alertaActual;
    private int indiceAlerta = -1;

    // 3. Ciclo de Monitoreo: Gestión de la rotación de sensores a consultar
    private Queue<String> colaMonitoreo;

    // 4. Topología de la Red: Grafo de conexiones entre estaciones (Lista de adyacencia)
    private Map<String, List<String>> topologiaRed;

    // 5. Buffer de Ingesta: Cola de espera para procesar lecturas entrantes
    private Queue<LecturaSensor> bufferIngesta;

    public GestorDeRed() {
        // TODO: Inicializa las colecciones usando las implementaciones más adecuadas de Java
        // (ArrayList, ArrayDeque, HashMap, etc.)
        this.historialEventos = new ArrayList<>();
        this.navegadorAlertas = new ArrayDeque<>();
        this.colaMonitoreo = new ArrayDeque<>();
        this.topologiaRed = new HashMap<>();
        this.bufferIngesta = new ArrayDeque<>();
    }

    // =========================================================================
    // SECCIÓN 1: HISTORIAL Y EVENTOS
    // =========================================================================

    public void agregarEvento(String evento) {
        String fecha = new java.util.Date().toString();
        historialEventos.add(fecha + " - " + evento);
    }

    public List<String> obtenerEventosRecientes(int limite) {
        if (historialEventos.size() <= limite) return new ArrayList<>(historialEventos);
        return historialEventos.subList(historialEventos.size() - limite, historialEventos.size());
    }


    public void limpiarHistorialAntiguo(int mantener) {
        if (mantener < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        int eliminar = historialEventos.size() - mantener;

        if (eliminar > 0) {
            historialEventos.subList(0, eliminar).clear();
        }
    }


    // =========================================================================
    // SECCIÓN 2: NAVEGACIÓN DE ALERTAS
    // =========================================================================


    public void registrarAlerta(String alerta) {
        navegadorAlertas.addLast(alerta);

        if (navegadorAlertas.size() == 1) {
            indiceAlerta = 0;
            alertaActual = alerta;
        }
    }


    public String navegarAlSiguiente() {
        if (navegadorAlertas.isEmpty()) {
            return null;
        }

        if (indiceAlerta == -1) {
            indiceAlerta = 0;
        } else if (indiceAlerta < navegadorAlertas.size() - 1) {
            indiceAlerta++;
        } else {
            return null;
        }

        alertaActual = new ArrayList<>(navegadorAlertas).get(indiceAlerta);
        return alertaActual;
    }

    public String navegarAlAnterior() {
        if (navegadorAlertas.isEmpty() || indiceAlerta <= 0) {
            return null;
        }

        indiceAlerta--;

        alertaActual = new ArrayList<>(navegadorAlertas).get(indiceAlerta);

        return alertaActual;
    }

    public String getAlertaActual() { return alertaActual; }

    // =========================================================================
    // SECCIÓN 3: CICLO DE MONITOREO
    // =========================================================================


    public void programarConsulta(String idSensor) {
        if (!colaMonitoreo.contains(idSensor)) {
            colaMonitoreo.add(idSensor);
        }
    }



    public String obtenerSiguienteSensor() {
        if (colaMonitoreo.isEmpty()) {
            return null;
        }

        String sensor = colaMonitoreo.poll();

        colaMonitoreo.add(sensor);

        return sensor;
    }

    // =========================================================================
    // SECCIÓN 4: TOPOLOGÍA DE RED
    // =========================================================================


    public void registrarConexion(String sensorA, String sensorB) {
        topologiaRed.putIfAbsent(sensorA, new ArrayList<>());
        topologiaRed.putIfAbsent(sensorB, new ArrayList<>());

        if (!topologiaRed.get(sensorA).contains(sensorB)) {
            topologiaRed.get(sensorA).add(sensorB);
        }

        if (!topologiaRed.get(sensorB).contains(sensorA)) {
            topologiaRed.get(sensorB).add(sensorA);
        }
    }



    public List<String> obtenerVecinos(String idSensor) {
        return new ArrayList<>(
                topologiaRed.getOrDefault(idSensor, Collections.emptyList())
        );
    }



    public List<String> detectarEstacionesAisladas() {
        List<String> aisladas = new ArrayList<>();

        for (Map.Entry<String, List<String>> entrada : topologiaRed.entrySet()) {
            if (entrada.getValue().isEmpty()) {
                aisladas.add(entrada.getKey());
            }
        }

        return aisladas;
    }


    // =========================================================================
    // SECCIÓN 5: BUFFER DE INGESTA
    // =========================================================================

    public void recibirLectura(LecturaSensor lectura) {
        bufferIngesta.add(lectura);
    }


    public LecturaSensor procesarSiguienteLectura() {
        return bufferIngesta.poll();
    }


    public List<LecturaSensor> filtrarAlertasCriticas(double limitePm25) {
        // TODO EXTRA: Usar Streams para filtrar lecturas con PM2.5 superior al límite.
        // Debe retornar una lista con las lecturas filtradas.
        return null;
    }
}
