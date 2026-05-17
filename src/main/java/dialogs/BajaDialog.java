/**
 * 
 */
package dialogs;

import dao.AccesoTrabajador;
import excepciones.BDException;
import modelo.Trabajador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * 
 * @author usuario
 *
 */
public class BajaDialog extends JDialog implements ActionListener {

	JButton eliminar;
	JButton cancelar;
	JPanel panel;
	JPanel panelBotones;
	JLabel texto;

	static JTable tabla;
	Object[] columnasTabla;
	DefaultTableModel modelo;

	public BajaDialog() {
		iniciarComponentes();
	}

	private void iniciarComponentes() {

		//Personalizacion del JDialog
		setResizable(false);
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
		modelo = new DefaultTableModel(null, columnasTabla) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tabla = new JTable(modelo);
		panel.add(tabla);
		add(new JScrollPane(tabla));

		//bloqueamos que se puedan mover las columnas
		tabla.getTableHeader().setReorderingAllowed(false);

		//Aceptar
		eliminar = new JButton("Eliminar");
		eliminar.setName("btnAceptar");
		eliminar.addActionListener(this);
		panelBotones.add(eliminar);

		//Cancelar
		cancelar = new JButton("Cancelar");
		cancelar.addActionListener(this);
		panelBotones.add(cancelar);

		//Añadimos los datos a la tabla
		try {
			datosTabla(columnasTabla, modelo);
		} catch (BDException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		//Añadimos los JPanel al JDialog
		add(panel);
		add(panelBotones);

		//Visible
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private static void datosTabla(Object[] columnasTabla, DefaultTableModel modelo) throws BDException {
		modelo.setRowCount(0);
		tabla.setModel(modelo);
		tabla.setRowHeight(24);
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

		if (e.getSource() == eliminar) {
			if (tabla.getSelectedRows().length == 1) {
				try {
					String dni = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);
					int respuesta = JOptionPane.showConfirmDialog(null, "Desea dar de baja el trabajador?", "Borrar",
							JOptionPane.YES_NO_OPTION);
					switch (respuesta) {
						case JOptionPane.YES_OPTION:
							if (AccesoTrabajador.eliminarTrabajador(dni)) {
								JOptionPane.showMessageDialog(this, "El trabajador se ha eliminado correctamente");
								datosTabla(columnasTabla, modelo);
							}
							break;
						case JOptionPane.NO_OPTION:
							break;
					}

				} catch (BDException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else if (tabla.getSelectedRows().length > 1) {
				JOptionPane.showMessageDialog(null, "Debe seleccionar solamente una fila", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Debe seleccionar una fila", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == cancelar) {
			dispose();
		}

	}

}
