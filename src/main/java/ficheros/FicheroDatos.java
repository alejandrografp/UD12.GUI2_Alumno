package ficheros;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import dao.AccesoTrabajador;
import excepciones.BDException;
import gui.EmpresaGUI;
import modelo.Trabajador;

/**
 * @author alumno
 *
 */
public class FicheroDatos {
	
	/**
	 * Escribe un ArrayList en el fichero
	 * @param ruta
	 * @param trabajadores
	 */
	public static void escribirTrabajadoresEnCSV(String ruta, ArrayList<Trabajador> trabajadores){
		
		BufferedWriter fichero = null;
		FileWriter escribir = null;
		try {
			escribir = new FileWriter(ruta);
			fichero = new BufferedWriter(escribir);
			for(int i=0; i<trabajadores.size(); i++){
				fichero.write(trabajadores.get(i).toStringWithSeparators());
			}		
		} 
		catch (FileNotFoundException e1){
			System.out.printf("Error al abrir fichero para escritura");
		}
		catch (IOException e){ 
			System.out.printf("Error al escribir en el fichero%n"); 
		} 
		finally{ 
			try{
			fichero.close();
			} 
			catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}		
	}
	
	/**
	 * Devuelve un arraylist con los trabajadores del fichero
	 * @param rutaFichero
	 * @return
	 */
	public static ArrayList<Trabajador> obtenerTrabajadoresDeCSV(String rutaFichero) {

		BufferedReader ficheroDatos = null;
		ArrayList<Trabajador> trabajadoresLeidos = new ArrayList <Trabajador>();
		Trabajador t = null;
		FileReader fr = null;
		try {
			fr = new FileReader(rutaFichero);
			ficheroDatos = new BufferedReader(fr);
			String linea = ficheroDatos.readLine();

			while (linea != null){

				String[] trabajadores = linea.split(";");


				int id = Integer.parseInt(trabajadores[0]);
				String dni = trabajadores[1];
				String nombre = trabajadores[2];
				String apellidos = trabajadores[3];
				String direccion = trabajadores[4];
				String telefono = trabajadores[5];
				String puesto = trabajadores[6];
				t = new Trabajador(id,dni,nombre,apellidos,direccion,telefono,puesto);

				trabajadoresLeidos.add(t);
				linea = ficheroDatos.readLine();
			}			
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				ficheroDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		return trabajadoresLeidos;
	}


	/**
	 * Escribe un ArrayList en el fichero
	 * @param ruta
	 * @param trabajadores
	 */
	public static void escribirTrabajadoresEnDat(String ruta, ArrayList<Trabajador> trabajadores){

		DataOutputStream fichero = null;
		try {
			fichero = new DataOutputStream (new FileOutputStream(ruta));
			for(int i=0; i<trabajadores.size(); i++){
				fichero.writeInt(trabajadores.get(i).getIdentificador());
				fichero.writeUTF(trabajadores.get(i).getDni());
				fichero.writeUTF(trabajadores.get(i).getNombre());
				fichero.writeUTF(trabajadores.get(i).getApellidos());
				fichero.writeUTF(trabajadores.get(i).getDireccion());
				fichero.writeUTF(trabajadores.get(i).getTelefono());
				fichero.writeUTF(trabajadores.get(i).getPuesto());
			}
		}
		catch (FileNotFoundException e1){
			System.out.printf("Error al abrir fichero para escritura");
		}
		catch (IOException e){
			System.out.printf("Error al escribir en el fichero%n");
		}
		finally{
			try{
				fichero.close();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * Devuelve un arraylist con los trabajadores del fichero
	 * @param rutaFichero
	 * @return
	 */
	public static ArrayList<Trabajador> obtenerTrabajadoresDeDat(String rutaFichero) {

		DataInputStream ficheroDatos=null;
		ArrayList<Trabajador> trabajadoresLeidos = new ArrayList <Trabajador>();
		Trabajador t = null;
		try {
			ficheroDatos=new DataInputStream(new FileInputStream(rutaFichero));
			while (true){
				int id = ficheroDatos.readInt();
				System.out.println(id);
				String dni = ficheroDatos.readUTF();
				String nombre = ficheroDatos.readUTF();
				String apellidos = ficheroDatos.readUTF();
				String direccion = ficheroDatos.readUTF();
				String telefono = ficheroDatos.readUTF();
				String puesto = ficheroDatos.readUTF();
				t = new Trabajador(id,dni,nombre,apellidos,direccion,telefono,puesto);
				trabajadoresLeidos.add(t);
			}
		}
		catch (EOFException e){

		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				ficheroDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return trabajadoresLeidos;
	}

}
