package dialogs;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class pruebas extends JDialog implements ActionListener {

    JButton aceptar;
    JButton cancelar;
    JPanel panel;
    JPanel panelBotones;
    JLabel texto;

    static JTable tabla;
    Object[] columnasTabla;
    DefaultTableModel modelo;

    public pruebas() {
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        setResizable(false);
        // t�tulo del di�log
        setTitle("Baja Trabajador");
        setSize(600, 570);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        //Inicializamos los JPanel
        panel = new JPanel();
        panelBotones = new JPanel();

        //Texto
        texto = new JLabel("<html><div style='text-align: center;'>Seleccione al trabajador<br> que deseas dar de baja<br><br></div></html>", SwingConstants.CENTER);
        add(texto);

        //Inicializamos elementos Tabla
        columnasTabla = new Object[]{"DNI", "Nombre", "Apellidos", "Direccion", "Telefono", "puesto"};
        modelo = new DefaultTableModel(null, columnasTabla);
        tabla = new JTable(modelo);
        panel.add(tabla);
        add(new JScrollPane(tabla));

        add(panel);

        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    static void main(String[] args) {
        pruebas dialog = new pruebas();
    }
}
