
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

import static dialogs.AltaDialog.validarDNI;
import static dialogs.AltaDialog.validarTelefono;

/**
 *
 * @author usuario
 *
 */
public class ModificaDialog extends JDialog implements ActionListener {
	JButton cancelar;
	JPanel panel;
	JPanel panelBotones;
	static JTable tabla;

	Object[] columnasTabla = {"DNI", "Nombre", "Apellidos", "Direccion", "Telefono", "puesto"};
	DefaultTableModel modelo;

	String valorOriginal;

	boolean restaurando = false;

	public ModificaDialog() {

		setResizable(false);
		// t�tulo del di�log
		setTitle("Modificar Trabajador");
		setSize(600, 570);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setLocationRelativeTo(null);

		panel = new JPanel();
		panelBotones = new JPanel();
		add(panel);




		try {
			modelo = new DefaultTableModel(null, columnasTabla);
			tabla = new JTable(modelo) {
				@Override
				public Component prepareEditor(TableCellEditor editor, int row, int column) {
					valorOriginal = getValueAt(row, column).toString();
					return super.prepareEditor(editor, row, column);
				}
			};
			tabla.setName("TablaBajaDialog");
			List<String> puestos = AccesoPuestos.consultarPuestos();
			JComboBox<String> comboColumnaPuesto = new JComboBox<>();
			for (int i = 0; i < puestos.size(); i++) {
				comboColumnaPuesto.addItem(puestos.get(i));
			}

			tabla.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboColumnaPuesto));



			modelo.addTableModelListener(e -> {
				if (restaurando) return;

				if (e.getType() == TableModelEvent.UPDATE) {
					int fila = e.getFirstRow();
					int col = e.getColumn();
					Object nuevoValor = tabla.getValueAt(fila, col);

					// Comparamos con el valor que capturó prepareEditor
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
                                        (String) tabla.getValueAt(fila, 0), (String) tabla.getValueAt(fila, 1),
                                        (String) tabla.getValueAt(fila, 2), (String) tabla.getValueAt(fila, 3),
                                        (String) tabla.getValueAt(fila, 4), (String) tabla.getValueAt(fila, 5))) {

                                    modificarDatoTrabajador(fila);
                                    valorOriginal = nuevoValor.toString();
                                } else {
									restaurando = true;
									tabla.setValueAt(valorOriginal, fila, col);
									restaurando = false;
                                }
                            } catch (BDException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
							restaurando = true;
							tabla.setValueAt(valorOriginal, fila, col);
							restaurando = false;
						}
					}
				}
			});





			panel.add(tabla);
			JScrollPane scrollPane = new JScrollPane(tabla);
			add(scrollPane);
			datosTabla(columnasTabla, modelo);
		} catch (BDException e) {
			throw new RuntimeException(e);
		}

		add(panelBotones);

		cancelar = new JButton("Cancelar");
		cancelar.addActionListener(this);
		panelBotones.add(cancelar);
		// Visible
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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


	public boolean comprobarErrores(String dni, String nombre, String apellidos, String direccion, String telefono, String puesto) throws BDException {
		if (dni.trim().equals("") || !validarDNI(dni)) {
				JOptionPane.showMessageDialog(null, "El DNI debe ser valido", "Error", JOptionPane.ERROR_MESSAGE);

			return false;
		} else if (nombre.trim().equals("")) {
			JOptionPane.showMessageDialog(null, "Debe introducir el nombre del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (apellidos.trim().equals("")) {
			JOptionPane.showMessageDialog(null, "Debe introducir los apellidos del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (direccion.equals("")) {
			JOptionPane.showMessageDialog(null, "Debe introducir la direccion del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (telefono.trim().equals("") || !validarTelefono(telefono)) {
			JOptionPane.showMessageDialog(null, "El telefono no es valido", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (puesto.equals("")) {
			JOptionPane.showMessageDialog(null, "Debe introducir el puesto del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
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
		if (e.getSource() == cancelar) {
			dispose();
		}

	}

}
