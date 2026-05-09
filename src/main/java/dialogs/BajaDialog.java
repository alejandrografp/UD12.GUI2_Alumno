/**
 * 
 */
package dialogs;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import dao.AccesoTrabajador;
import excepciones.BDException;
import modelo.Empresa;
import modelo.Trabajador;

/**
 * 
 * @author usuario
 *
 */
public class BajaDialog extends JDialog implements ActionListener {

	JButton aceptar;
	JButton cancelar;
	JPanel panel;
	JPanel panelBotones;
	JLabel texto;
	static JTable tabla;



	Object[] columnasTabla = {"DNI", "Nombre", "Apellidos", "Direccion", "Telefono", "puesto"};
	DefaultTableModel modelo;



	public BajaDialog() {

		setResizable(false);
		// t�tulo del di�log
		setTitle("Baja Trabajador");
		setSize(600, 570);
		setLayout(new FlowLayout());
		setLocationRelativeTo(null);

		texto = new JLabel("<html><div style='text-align: center;'>Seleccione al trabajador<br> que deseas dar de baja<br><br></div></html>", SwingConstants.CENTER);
		add(texto);

		panel = new JPanel();
		panelBotones = new JPanel();
		add(panel);




        try {
        	modelo = new DefaultTableModel(null, columnasTabla);
			tabla = new JTable(modelo);
			panel.add(tabla);
			add(new JScrollPane(tabla));
			datosTabla(columnasTabla, modelo);
		} catch (BDException e) {
            throw new RuntimeException(e);
        }

		add(panelBotones);

		aceptar = new JButton("Aceptar");
		aceptar.setName("btnAceptar");
		aceptar.addActionListener(this);
		panelBotones.add(aceptar);

		cancelar = new JButton("Cancelar");
		cancelar.addActionListener(this);
		panelBotones.add(cancelar);
		// Visible
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
		if (e.getSource() == aceptar) {
			if (tabla.getSelectedRows().length == 1) {
				try {
					String dni = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);

					int filaSeleccionada = tabla.getSelectedRow();

					int respuesta = JOptionPane.showConfirmDialog(null, "Desea dar de baja el trabajador?", "Borrar",
							JOptionPane.YES_NO_OPTION);
					switch (respuesta) {
						case JOptionPane.YES_OPTION:
							// Operaciones en caso afirmativo
							if (AccesoTrabajador.eliminarTrabajador(dni)) {
								JOptionPane.showMessageDialog(this, "El trabajador se ha eliminado correctamente");
								DefaultTableModel modelo2 = (DefaultTableModel) tabla.getModel();
								modelo2.removeRow(filaSeleccionada);
							} else {
								JOptionPane.showMessageDialog(null, "El trabajador no se encuentra en la lista", "Error",
										JOptionPane.ERROR_MESSAGE);
							}

							break;

						case JOptionPane.NO_OPTION:
							// Operaciones en caso negativo
							break;
					}

				} catch (Exception ex) {

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
