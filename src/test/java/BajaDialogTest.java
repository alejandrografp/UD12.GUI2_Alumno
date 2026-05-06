import dialogs.BajaDialog;
import gui.EmpresaGUI;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.timing.Pause;
import org.assertj.swing.timing.Timeout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BajaDialogTest {
    private FrameFixture windowPrincipal;

    @BeforeEach
    void setUp() {
        // Inicialización correcta en el EDT
        EmpresaGUI frame = GuiActionRunner.execute(() -> new EmpresaGUI());
        windowPrincipal = new FrameFixture(frame);
        windowPrincipal.show();
    }

    @Test
    void testAbrirVentanaBajaDialog() {
        windowPrincipal.button("bajaTrabajador").click();
    }

    @Test
    void testEliminarFilaTablaYBaseDatosSeleccionada() {
        windowPrincipal.button("bajaTrabajador").click();

        DialogFixture ventanaBjaDialog = WindowFinder.findDialog(BajaDialog.class)
                .using(windowPrincipal.robot());

        ventanaBjaDialog.table("TablaBajaDialog").cell("4").click();
        ventanaBjaDialog.button("btnAceptar").click();

        JOptionPaneFixture confirmDialog = ventanaBjaDialog.optionPane(Timeout.timeout(2000));
        confirmDialog.requireMessage("Desea dar de baja el trabajador?");
        confirmDialog.yesButton().click();

        JOptionPaneFixture exitoDialog = ventanaBjaDialog.optionPane(Timeout.timeout(2000));
        exitoDialog.requireMessage("El trabajador se ha eliminado correctamente");
        exitoDialog.okButton().click();



        ventanaBjaDialog.robot().waitForIdle();
        Pause.pause(1000);
    }

    @Test
    void testEliminarFilaTablaYBaseDatosNoSeleccionada() {
        windowPrincipal.button("bajaTrabajador").click();

        DialogFixture ventanaBjaDialog = WindowFinder.findDialog(BajaDialog.class)
                .using(windowPrincipal.robot());

        // Hacemos clic sin seleccionar nada en la tabla
        ventanaBjaDialog.button("btnAceptar").click();

        // Capturamos el JOptionPane de error
        JOptionPaneFixture errorDialog = ventanaBjaDialog.optionPane(Timeout.timeout(2000));
        errorDialog.requireMessage("Debe seleccionar una fila");

        errorDialog.okButton().click();
        ventanaBjaDialog.robot().waitForIdle();
        Pause.pause(1000);
    }

    @AfterEach
    void tearDown() {
        if (windowPrincipal != null) {
            windowPrincipal.cleanUp();
        }
    }
}