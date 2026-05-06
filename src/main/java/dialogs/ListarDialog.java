/**
 *
 */
package dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;

import dao.AccesoTrabajador;
import excepciones.BDException;
import modelo.Empresa;
import modelo.Trabajador;

/**
 *
 * @author usuario
 *
 */
public class ListarDialog extends JDialog implements ActionListener {
	static JTable tabla;
	JButton cerrar;

	Object[] columnasTabla = {"DNI", "Nombre", "Apellidos", "Direccion", "Telefono", "Puesto"};
	DefaultTableModel modelo;

	JPanel panel;
	JScrollPane jsp;

	public ListarDialog() {

		setResizable(false);
		// t�tulo del di�log
		setTitle("Listado Trabajadores");
		// tama�o
		setSize(750, 700);
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
			modelo = new DefaultTableModel(null, columnasTabla);
			tabla = new JTable(modelo);
			tabla.setAutoCreateRowSorter(true);
			panel.add(tabla);
			jsp = new JScrollPane(tabla);
			jsp.setPreferredSize(new Dimension(700, 600));
			add(jsp);
			datosTabla(columnasTabla, modelo);
		} catch (BDException e) {
			throw new RuntimeException(e);
		}

		cerrar = new JButton("Cerrar");
		cerrar.addActionListener(this);
		add(cerrar);

		setVisible(true);
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
