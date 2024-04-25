package practicaMockito.practicaSensores;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class SensorTest {

    private GestorSensores gestorSensores;
    private ISensorTemperatura sensorMock;

    @BeforeEach
    void setUp() {
        gestorSensores = new GestorSensores();
        sensorMock = mock(ISensorTemperatura.class);
    }
    @AfterEach
    void tearDown(){
        gestorSensores=null;
        sensorMock=null;
    }

    @Test
    void inicialmenteElNumeroDeSensoresDelGestorEsCero() {
        assertEquals(0, gestorSensores.getNumeroSensores());
    }

    @Test
    void siSeBorraUnSensorNoExistenteSeElevaExcepcion() {
        assertThrows(SensorExcepcion.class, () -> gestorSensores.borrarSensor("No existente"));
    }

    @Test
    void siSeObtieneLaTemperaturaMediaEnUnGestorVacioSeElevaExcepcion() {
        assertThrows(SensorExcepcion.class, () -> gestorSensores.getTemperaturaMedia());
    }

    @Test
    void siSeIntroduceUnSensorEnUnGestorLlenoSeElevaExcepcion() {
        for (int i = 0; i < 100; i++) {
            ISensorTemperatura sensor = mock(ISensorTemperatura.class);
            gestorSensores.introducirSensor(sensor);
        }
        assertThrows(SensorExcepcion.class, () -> gestorSensores.introducirSensor(sensorMock));
    }

    @Test
    void siSeBorraUnSensorDelGestorSeDecrementaEnUnoElNumeroDeSensores() {
        when(sensorMock.getNombre()).thenReturn("Sensor");
        gestorSensores.introducirSensor(sensorMock);
        gestorSensores.borrarSensor(sensorMock.getNombre());
        assertEquals(0, gestorSensores.getNumeroSensores());
    }

    @Test
    void siAlgunSensorTieneTemperaturaFueraDeRangoObtenerLaTemperaturaMediaElevaUnaExcepcion() {
        when(sensorMock.getNombre()).thenReturn("Sensor1");
        when(sensorMock.disponible()).thenReturn(true);
        when(sensorMock.getTemperaturaCelsius()).thenReturn(100.0f);
        gestorSensores.introducirSensor(sensorMock);
        assertThrows(SensorExcepcion.class, () -> gestorSensores.getTemperaturaMedia());
    }

    @Test
    void laTemperaturaMediaDeTresSensoresObtenidaATravesDelGestorEsCorrecta() {
        ISensorTemperatura sensor1 = mock(ISensorTemperatura.class);
        ISensorTemperatura sensor2 = mock(ISensorTemperatura.class);
        ISensorTemperatura sensor3 = mock(ISensorTemperatura.class);
        when(sensor1.disponible()).thenReturn(true);
        when(sensor2.disponible()).thenReturn(true);
        when(sensor3.disponible()).thenReturn(true);
        when(sensor1.getTemperaturaCelsius()).thenReturn(20.0f);
        when(sensor2.getTemperaturaCelsius()).thenReturn(25.0f);
        when(sensor3.getTemperaturaCelsius()).thenReturn(30.0f);
        gestorSensores.introducirSensor(sensor1);
        gestorSensores.introducirSensor(sensor2);
        gestorSensores.introducirSensor(sensor3);
        assertEquals(25.0f, gestorSensores.getTemperaturaMedia());
    }

    @Test
    void siSeContactaTresVecesConSensoresDisponiblesNoSeBorraNinguno() {
        ISensorTemperatura sensor1 = mock(ISensorTemperatura.class);
        ISensorTemperatura sensor2 = mock(ISensorTemperatura.class);
        when(sensor1.disponible()).thenReturn(true);
        when(sensor2.disponible()).thenReturn(true);
        gestorSensores.introducirSensor(sensor1);
        gestorSensores.introducirSensor(sensor2);
        for (int i = 0; i<3; i++){
            gestorSensores.contactarSensores();
        }
        assertEquals(2, gestorSensores.getNumeroSensores());
    }

    @Test
    void siSeContactaTresVecesConUnSensorNoDisponibleSeBorraDelGestor() {
        ISensorTemperatura sensor1 = mock(ISensorTemperatura.class);
        when(sensor1.disponible()).thenReturn(false);
        gestorSensores.introducirSensor(sensor1);
        for (int i = 0; i<3; i++){
            gestorSensores.contactarSensores();
        }
        assertEquals(0, gestorSensores.getNumeroSensores());
    }
}
