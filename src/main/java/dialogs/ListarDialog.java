package dialogs;


import dao.AccesoTrabajador;
import excepciones.BDException;
import ficheros.FicheroDatos;
import modelo.Trabajador;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

public class ListarDialog extends JDialog implements ActionListener {

	//Elementos filtrado y busqueda
	JTextField txtBusqueda;
	JComboBox<Object> comboFiltro;
	TableRowSorter<DefaultTableModel> filtrado;
	JLabel lblSinResultados;


	//Botones
	JButton cerrar;
	JButton elegirArchivoJSON;
	JButton elegirArchivoCSV;

	//Elementos Tabla
	static JTable tabla;
	Object[] columnasTabla;
	DefaultTableModel modelo;

	//JPanel
	JPanel panel;
	JPanel panelFiltradoBusqueda;
	JPanel panelBotones;

	JFileChooser selector;

	public ListarDialog() {
		iniciarComponentes();
	}

	private void iniciarComponentes() {

		//Personalizacion del JDialog
		setResizable(false);
		setTitle("Listado Trabajadores");
		setSize(600, 560);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setLocationRelativeTo(null);

		//Inicializamos los JPanel
		panel = new JPanel();
		panelFiltradoBusqueda = new JPanel();
		panelBotones = new JPanel();

		//Inicializamos lo que falta
		columnasTabla = new Object[]{"DNI", "Nombre", "Apellidos", "Direccion", "Telefono", "Puesto"};
		comboFiltro = new JComboBox<>(columnasTabla);
		txtBusqueda = new JTextField(15);
		lblSinResultados = new JLabel("No se encontraron coincidencias");
		selector = new JFileChooser();
		modelo = new DefaultTableModel(null, columnasTabla) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tabla = new JTable(modelo);
		cerrar = new JButton("Cerrar");
		elegirArchivoJSON = new JButton("Exportar JSON");
		elegirArchivoCSV = new JButton("Exportar CSV");

		//bloqueamos que se puedan mover las columnas
		tabla.getTableHeader().setReorderingAllowed(false);

		//personalizacion JLabel: busqueda sin resultados
		lblSinResultados.setForeground(Color.RED);
		lblSinResultados.setVisible(false);

		//Filtrado y busqueda de datos en la tabla
		filtradoBusqueda();
		filtrado = new TableRowSorter<>(modelo);
		tabla.setRowSorter(filtrado);

		//Añadimos un listener a los botones
		cerrar.addActionListener(this);
		elegirArchivoJSON.addActionListener(this);
		elegirArchivoCSV.addActionListener(this);

		//Añadimos cada componente con su JPanel
		panelFiltradoBusqueda.add(comboFiltro);
		panelFiltradoBusqueda.add(txtBusqueda);
		panelFiltradoBusqueda.add(lblSinResultados);
		panel.add(tabla);
		panel.add(new JScrollPane(tabla));
		panelBotones.add(elegirArchivoJSON);
		panelBotones.add(elegirArchivoCSV);
		panelBotones.add(cerrar);

		//Cargamos los datos en la tabla
		try {
			datosTabla(columnasTabla, modelo);
		} catch (BDException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		//Añadimos los JPanel al JDialog
		add(panelFiltradoBusqueda);
		add(panel);
		add(panelBotones);

		//Visible
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void filtradoBusqueda() {
		comboFiltro.addActionListener(e -> {
			try {
				aplicarFiltro();
			} catch (BDException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					aplicarFiltro();
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					aplicarFiltro();
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
					aplicarFiltro();
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

	private void aplicarFiltro() throws BDException {
		datosTabla(columnasTabla, modelo);
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
			} catch (PatternSyntaxException e) {
				return;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == elegirArchivoJSON) {
			try {
				datosTabla(columnasTabla, modelo);
			} catch (BDException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			String nombreArchivoExportarJSON = "empleados.json";
			selector.setSelectedFile(new File(nombreArchivoExportarJSON));
			FileNameExtensionFilter filtro = new FileNameExtensionFilter(".json", "json");
			selector.setFileFilter(filtro);
			int resultado = selector.showOpenDialog(this);
			if (resultado == JFileChooser.APPROVE_OPTION) {
				File archivoSeleccionado = selector.getSelectedFile();
				ArrayList<Trabajador> datosTrabajadores = null;
				try {
					datosTrabajadores = AccesoTrabajador.consultarTrabajadores();
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				FicheroDatos.escribirTrabajadoresEnJson(archivoSeleccionado.getAbsolutePath(), datosTrabajadores);
				JOptionPane.showMessageDialog(null, "Lista de trabajadores exportada correctamente");
			}
		} else if (e.getSource() == elegirArchivoCSV) {
			try {
				datosTabla(columnasTabla, modelo);
			} catch (BDException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			String nombreArchivoExportarCSV = "empleados.csv";
			selector.setSelectedFile(new File(nombreArchivoExportarCSV));
			FileNameExtensionFilter filtro = new FileNameExtensionFilter(".csv", "csv");
			selector.setFileFilter(filtro);
			int resultado = selector.showOpenDialog(this);
			if (resultado == JFileChooser.APPROVE_OPTION) {
				File archivoSeleccionado = selector.getSelectedFile();
				ArrayList<Trabajador> datosTrabajadores = null;
				try {
					datosTrabajadores = AccesoTrabajador.consultarTrabajadores();
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				FicheroDatos.escribirTrabajadoresEnCSV(archivoSeleccionado.getAbsolutePath(), datosTrabajadores);
				JOptionPane.showMessageDialog(null, "Lista de trabajadores exportada correctamente");
			}
		}else if (e.getSource() == cerrar) {
			dispose();
		}
	}
}
