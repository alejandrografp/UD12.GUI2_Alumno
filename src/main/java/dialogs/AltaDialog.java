package dialogs;

import dao.AccesoPuestos;
import dao.AccesoTrabajador;
import excepciones.BDException;
import modelo.Trabajador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class AltaDialog extends JDialog implements ActionListener, ItemListener {

	//Campos JDialog
	JLabel etiquetaDni;
	JTextField areaDni;
	JLabel etiquetaNombre;
	JTextField areaNombre;
	JLabel etiquetaApellidos;
	JTextField areaApellidos;
	JLabel etiquetaDireccion;
	JTextField areaDireccion;
	JLabel etiquetaTelefono;
	JTextField areaTelefono;
	JLabel etiquetaPuesto;
	JComboBox comboPuesto;
	JButton aceptar;
	JButton cancelar;

	//Declaramos campos que se usan para obtener datos de los jTextField
	String dni = "";
	String nombre = "";
	String apellidos = "";
	String direccion = "";
	String telefono = "";
	String puesto = "";

	//Declaramos todos los JPanel
	JPanel pDni;
	JPanel pNombre;
	JPanel pApellidos;
	JPanel pDireccion;
	JPanel pTelefono;
	JPanel pPuesto;
	JPanel pBotones;

	public AltaDialog() {
		iniciarComponentes();
	}

	private void iniciarComponentes() {

		//Personalizacion del JDialog
		setResizable(false);
		setTitle("Alta Trabajador");
		setSize(300, 350);
		setLayout(new FlowLayout());
		setLocationRelativeTo(null);

		//Inicializamos los JPanel
		pDni = new JPanel();
		pNombre = new JPanel();
		pApellidos = new JPanel();
		pDireccion = new JPanel();
		pTelefono = new JPanel();
		pPuesto = new JPanel();
		pBotones = new JPanel();

		//Dni
		etiquetaDni = new JLabel("DNI                 ");
		areaDni = new JTextField(15);
		pDni.add(etiquetaDni);
		pDni.add(areaDni);

		//Nombre
		etiquetaNombre = new JLabel("Nombre         ");
		areaNombre = new JTextField(15);
		pNombre.add(etiquetaNombre);
		pNombre.add(areaNombre);

		//Apellidos
		etiquetaApellidos = new JLabel("Apellidos      ");
		areaApellidos = new JTextField(15);
		pApellidos.add(etiquetaApellidos);
		pApellidos.add(areaApellidos);

		//Direccion
		etiquetaDireccion = new JLabel("Direccion      ");
		areaDireccion = new JTextField(15);
		pDireccion.add(etiquetaDireccion);
		pDireccion.add(areaDireccion);

		//Telefono
		etiquetaTelefono = new JLabel("Telefono       ");
		areaTelefono = new JTextField(15);
		pTelefono.add(etiquetaTelefono);
		pTelefono.add(areaTelefono);

		//Puesto
		etiquetaPuesto = new JLabel("Puesto                         ");
		comboPuesto = new JComboBox();
		puestosCombo();
		pPuesto.add(etiquetaPuesto);
		pPuesto.add(comboPuesto);

		//Aceptar
		aceptar = new JButton("Aceptar");
		aceptar.addActionListener(this);
		pBotones.add(aceptar);

		//Cancelar
		cancelar = new JButton("Cancelar");
		cancelar.addActionListener(this);
		pBotones.add(cancelar);

		//Añadimos JPanel al JDialog principal
		add(pDni);
		add(pNombre);
		add(pApellidos);
		add(pDireccion);
		add(pTelefono);
		add(pPuesto);
		add(pBotones);

		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void puestosCombo() {
		comboPuesto.addItem("Elija Puesto");
		List<String> puestos = null;
		try {
			puestos = AccesoPuestos.consultarPuestos();
		} catch (BDException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}

		for (int i = 0; i < puestos.size(); i++) {
			comboPuesto.addItem(puestos.get(i));
		}
		comboPuesto.addItemListener(this);
	}

	public static boolean validarDNI(String dni) {
		if (dni == null || dni.length() != 9) {
			return false;
		}

		String dniEnMayusculas = dni.toUpperCase();
		String numeros = dniEnMayusculas.substring(0, 8);
		char letra = dniEnMayusculas.charAt(8);

		if (!numeros.matches("\\d{8}")) {
			return false;
		}

		String letrasValidas = "TRWAGMYFPDXBNJZSQVHLCKE";
		int indice = Integer.parseInt(numeros) % 23;
		char letraEsperada = letrasValidas.charAt(indice);

		if (letra != letraEsperada) {
			return false;
		}
		return true;
	}

	public static boolean validarTelefono(String telefono) {
		// Comprueba null y vacío por separado para evitar NullPointerException
		if (telefono == null || telefono.isBlank()) {
			return false;
		}

		// Elimina espacios y guiones para normalizar: "+34 612-345-678" → "+34612345678"
		String telefonoLimpio = telefono.replaceAll("[\\s-]", "");

		String expresionRegular =
				"^"
						+ "(\\+34)?" // Prefijo +34 opcional (el \\ escapa el + para tratarlo como literal)
						+ "[6-9]"    // Un dígito entre 6 y 9 (primer dígito válido en España)
						+ "\\d{8}"   // Exactamente 8 dígitos más (\\d = cualquier dígito 0-9)
						+ "$";

		// Si NO coincide con el patrón, el teléfono está mal formado
		if (!telefonoLimpio.matches(expresionRegular)) {
			return false;
		}
		return true;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		puesto = comboPuesto.getSelectedItem().toString();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == aceptar) {
			try {
				dni = areaDni.getText();
				nombre = areaNombre.getText();
				apellidos = areaApellidos.getText();
				direccion = areaDireccion.getText();
				telefono = areaTelefono.getText();
				if (comprobarErrores()) {
					Trabajador t = new Trabajador(0, dni, nombre, apellidos, direccion, telefono, puesto);
					if (AccesoTrabajador.insertarTrabajador(t)) {
						JOptionPane.showMessageDialog(null, "Datos introducidos correctamente");
						limpiarDatos();
					} else {
						JOptionPane.showMessageDialog(null, "El ID del trabajador que quiere introducir ya existe",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}

			} catch (BDException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}

		} else if (e.getSource() == cancelar) {
			dispose();
		}
	}

	private void limpiarDatos() {
		areaDni.setText("");
		areaNombre.setText("");
		areaApellidos.setText("");
		areaDireccion.setText("");
		areaTelefono.setText("");
		comboPuesto.setSelectedIndex(0);
	}

	public boolean comprobarErrores() throws BDException {
		if (!validarDNI(dni) || AccesoTrabajador.consultarTrabajadorPorDni(dni)) {
			if (!AccesoTrabajador.consultarTrabajadorPorDni(dni)) {
				JOptionPane.showMessageDialog(null, "El DNI debe ser valido", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Ya existe un trabajador con el mismo dni", "Error", JOptionPane.ERROR_MESSAGE);
			}
			return false;
		} else if (nombre.trim().equals("")) {
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
		} else if (puesto.equals("")) {
			JOptionPane.showMessageDialog(null, "Debe introducir el puesto del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
