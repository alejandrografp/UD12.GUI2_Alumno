package dialogs;


import dao.AccesoPuestos;
import dao.AccesoTrabajador;
import excepciones.BDException;
import modelo.Trabajador;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static dialogs.AltaDialog.validarTelefono;

public class ModificaDialog extends JDialog implements ActionListener {
	//Botones
	JButton cancelar;

	//Elementos Tabla
	static JTable tabla;
	Object[] columnasTabla;
	DefaultTableModel modelo;

	//JPanel
	JPanel panel;
	JPanel panelBotones;

	//Valor original de la celda a modificar
	String valorOriginal;

	public ModificaDialog() {
		iniciarComponentes();
	}

	private void iniciarComponentes() {

		//Personalizacion del JDialog
		setResizable(false);
		setTitle("Modificar Trabajador");
		setSize(600, 560);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setLocationRelativeTo(null);

		//Inicializamos los JPanel
		panel = new JPanel();
		panelBotones = new JPanel();

		//inicializamos los componentes restantes
		columnasTabla = new Object[]{"DNI", "Nombre", "Apellidos", "Direccion", "Telefono", "puesto"};
		modelo = new DefaultTableModel(null, columnasTabla){
			@Override
			public boolean isCellEditable(int row, int column) {
				return column > 0;
			}
		};
		tabla = new JTable(modelo) {
			@Override
			public Component prepareEditor(TableCellEditor editor, int row, int column) {
				valorOriginal = getValueAt(row, column).toString();
				return super.prepareEditor(editor, row, column);
			}
		};
		cancelar = new JButton("Cancelar");

		//Preparar el combobox en la tabla con los valores
		List<String> puestos = null;
		try {
			puestos = AccesoPuestos.consultarPuestos();
		} catch (BDException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		JComboBox<String> comboColumnaPuesto = new JComboBox<>();
		for (int i = 0; i < puestos.size(); i++) {
			comboColumnaPuesto.addItem(puestos.get(i));
		}
		tabla.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboColumnaPuesto));

		//detecta si hay algun cambio en alguna celda de la tabla
		modificarCampoTrabajador();

		//Añadimos los componentes a los JPanel
		panel.add(tabla);
		panel.add(new JScrollPane(tabla));
		panelBotones.add(cancelar);

		//cargamos todos los datos a la tabla
		try {
			datosTabla(columnasTabla, modelo);
		} catch (BDException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		//Añadimos los JPanel al JDialog
		add(panel);
		add(panelBotones);

		//Visible
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void modificarCampoTrabajador() {
		modelo.addTableModelListener(e -> {
			if (e.getType() == TableModelEvent.UPDATE) {
				int fila = e.getFirstRow();
				int col = e.getColumn();
				Object nuevoValor = tabla.getValueAt(fila, col);

				if (valorOriginal != null && !nuevoValor.equals(valorOriginal)) {

					int respuesta = JOptionPane.showConfirmDialog(
							null,
							"Desea modificar el trabajador con el dni: " + tabla.getValueAt(fila, 0).toString(),
							"Modificar Trabajador",
							JOptionPane.YES_NO_OPTION
					);

					if (respuesta == JOptionPane.YES_OPTION) {
						try {
							if (comprobarErrores(
									(String) tabla.getValueAt(fila, 1),
									(String) tabla.getValueAt(fila, 2), (String) tabla.getValueAt(fila, 3),
									(String) tabla.getValueAt(fila, 4))) {

								modificarDatoTrabajador(fila);
								datosTabla(columnasTabla, modelo);
								JOptionPane.showMessageDialog(null, "Trabajador modificado correctamente");
							} else {
								datosTabla(columnasTabla, modelo);
							}
						} catch (BDException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						try {
							datosTabla(columnasTabla, modelo);
						} catch (BDException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}

				}
			}
		});
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

	private void modificarDatoTrabajador(int fila) {
		Trabajador trabajador = new Trabajador(0, (String) tabla.getValueAt(fila, 0), (String) tabla.getValueAt(fila, 1), (String) tabla.getValueAt(fila, 2), (String) tabla.getValueAt(fila, 3), (String) tabla.getValueAt(fila, 4), (String) tabla.getValueAt(fila, 5));
		try {
			AccesoTrabajador.modificarTrabajador(trabajador);
		} catch (BDException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}


	public boolean comprobarErrores(String nombre, String apellidos, String direccion, String telefono) throws BDException {
		if (nombre.trim().equals("")) {
			JOptionPane.showMessageDialog(null, "Debe introducir el nombre del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (apellidos.trim().equals("")) {
			JOptionPane.showMessageDialog(null, "Debe introducir los apellidos del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (direccion.trim().equals("")) {
			JOptionPane.showMessageDialog(null, "Debe introducir la direccion del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (!validarTelefono(telefono)) {
			JOptionPane.showMessageDialog(null, "El telefono no es valido", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelar) {
			dispose();
		}

	}
}
