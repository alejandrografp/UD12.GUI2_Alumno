import config.ConfigMySql;
import dialogs.BajaDialog;
import gui.EmpresaGUI;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import org.assertj.swing.edt.GuiActionRunner;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class ConfigMysqlTest {

    @Test
    void testabrirConexionExitoso() {
        Connection con = assertDoesNotThrow(() ->
                ConfigMySql.abrirConexion(), "No se pudo conectar a la base de datos."
        );
        assertNotNull("La conexion no deberia ser nula", con);
    }

    @Test
    void testcerrarConexionExitoso() {
        Connection con = assertDoesNotThrow(() ->
                ConfigMySql.abrirConexion(), "La conexion no es correcta."
        );
        assertDoesNotThrow(() ->
                ConfigMySql.cerrarConexion(con), "No se pudo conectar a la base de datos."
        );
    }



}
