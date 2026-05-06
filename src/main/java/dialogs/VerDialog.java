package dialogs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import dao.AccesoTrabajador;
import excepciones.BDException;
import modelo.Trabajador;

public class VerDialog extends JDialog implements ActionListener{

    static JTable tabla;
    JButton cerrar;

    Object[] columnasTabla = {"DNI", "Nombre", "Apellidos", "Direccion", "Telefono", "Puesto"};
    DefaultTableModel modelo;

    JPanel panel;
    JScrollPane jsp;

    JTextField txtBusqueda;
    JComboBox<Object> comboFiltro;
    TableRowSorter<DefaultTableModel> filtrado;
    JLabel lblSinResultados;



    public VerDialog() {

        setResizable(false);
        // t�tulo del di�log
        setTitle("Listado Trabajadores");
        // tama�o
        setSize(750, 710);
        setLayout(new FlowLayout());
        // colocaci�n en el centro de la pantalla
        setLocationRelativeTo(null);

        // Crea un JTable, cada fila será un trabajador
        ArrayList<Trabajador> datos = null;
        try {
            datos = AccesoTrabajador.consultarTrabajadores();
        } catch (BDException e) {
            throw new RuntimeException(e);
        }

        panel = new JPanel();
        add(panel);




        try {
            comboFiltro = new JComboBox<>(columnasTabla);
            txtBusqueda = new JTextField(15);
            add(comboFiltro);
            add(txtBusqueda);
            lblSinResultados = new JLabel("No se encontraron coincidencias");
            lblSinResultados.setForeground(Color.RED);
            lblSinResultados.setVisible(false);

            add(lblSinResultados);

            comboFiltro.addActionListener(e -> {
                aplicarFiltro();
            });

            txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    aplicarFiltro();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    aplicarFiltro();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    aplicarFiltro();
                }
            });
            modelo = new DefaultTableModel(null, columnasTabla);
            tabla = new JTable(modelo);
            tabla.setAutoCreateRowSorter(true);
            panel.add(tabla);
            jsp = new JScrollPane(tabla);
            jsp.setPreferredSize(new Dimension(700, 600));
            add(jsp);
            filtrado = new TableRowSorter<>(modelo);
            tabla.setRowSorter(filtrado);



            datosTabla(columnasTabla, modelo);

        } catch (BDException e) {
            throw new RuntimeException(e);
        }

        cerrar = new JButton("Cerrar");
        cerrar.addActionListener(this);
        add(cerrar);

        setVisible(true);
    }

    private void aplicarFiltro() {
        String texto = txtBusqueda.getText();
        int columnaIndices = comboFiltro.getSelectedIndex();

        if (texto.trim().isEmpty()) {
            filtrado.setRowFilter(null);
            lblSinResultados.setVisible(false);
        } else {
            try {
                filtrado.setRowFilter(RowFilter.regexFilter("(?i)" + texto, columnaIndices));
                if (filtrado.getViewRowCount() == 0) {
                    lblSinResultados.setVisible(true);
                } else {
                    lblSinResultados.setVisible(false);
                }

            } catch (java.util.regex.PatternSyntaxException e) {
                return;
            }
        }
    }

    private static void datosTabla(Object[] columnasTabla, DefaultTableModel modelo) throws BDException {
        tabla.setModel(modelo);
        Object[] fila;
        ArrayList<Trabajador> datosTabla;
        datosTabla = AccesoTrabajador.consultarTrabajadores();
        fila = new Object[columnasTabla.length];
        for(int i = 0; i < datosTabla.size(); i++) {
            fila[0] = datosTabla.get(i).getDni();
            fila[1] = datosTabla.get(i).getNombre();
            fila[2] = datosTabla.get(i).getApellidos();
            fila[3] = datosTabla.get(i).getDireccion();
            fila[4] = datosTabla.get(i).getTelefono();
            fila[5] = datosTabla.get(i).getPuesto();
            modelo.addRow(fila);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == cerrar) {
            dispose();
        }
    }
}

