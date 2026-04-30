package dao;

import config.ConfigMySql;
import excepciones.BDException;
import modelo.Trabajador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static config.ConfigMySql.abrirConexion;

public class AccesoTrabajador {
    public static boolean insertarTrabajador(Trabajador trabajador) throws BDException {
        Connection conexion = null;
        int columnasInsertadas;

        try {
            conexion = abrirConexion();

            String sentenciaInsertarTrabajador = "INSERT INTO trabajador(dni, nombre, apellidos, direccion, telefono, puesto) VALUES(?,?,?,?,?,?);";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaInsertarTrabajador);
            sentencia.setString(1, trabajador.getDni());
            sentencia.setString(2, trabajador.getNombre());
            sentencia.setString(3, trabajador.getApellidos());
            sentencia.setString(4, trabajador.getDireccion());
            sentencia.setString(5, trabajador.getTelefono());
            sentencia.setString(6, trabajador.getPuesto());
            columnasInsertadas = sentencia.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (BDException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }
        return columnasInsertadas > 0;
    }

    public static boolean insertarTrabajadorConId(Trabajador trabajador) throws BDException {
        Connection conexion = null;
        int columnasInsertadas;

        try {
            conexion = abrirConexion();

            String sentenciaInsertarTrabajador = "INSERT INTO trabajador(id, dni, nombre, apellidos, direccion, telefono, puesto) VALUES(?,?,?,?,?,?,?);";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaInsertarTrabajador);
            sentencia.setInt(1, trabajador.getIdentificador());
            sentencia.setString(2, trabajador.getDni());
            sentencia.setString(3, trabajador.getNombre());
            sentencia.setString(4, trabajador.getApellidos());
            sentencia.setString(5, trabajador.getDireccion());
            sentencia.setString(6, trabajador.getTelefono());
            sentencia.setString(7, trabajador.getPuesto());
            columnasInsertadas = sentencia.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (BDException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }
        return columnasInsertadas > 0;
    }

    public static void insertarTrabajadores(ArrayList<Trabajador> trabajadores) throws BDException {
        for (int i = 0; i< trabajadores.size(); i++) {
            try {
                AccesoTrabajador.insertarTrabajadorConId(trabajadores.get(i));
            } catch (BDException e) {
                AccesoTrabajador.modificarTrabajador(trabajadores.get(i));
            }
        }
    }
    public static boolean eliminarTrabajador(int id) throws BDException {
        Connection conexion = null;
        int columnasInsertadas;

        try {
            conexion = abrirConexion();

            String sentenciaEliminarTrabajador = "DELETE FROM trabajador WHERE id = ?;";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaEliminarTrabajador);
            sentencia.setInt(1, id);
            columnasInsertadas = sentencia.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (BDException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }
        return columnasInsertadas > 0;
    }

    public static boolean consultarTrabajador(int id) throws BDException {
        Connection conexion = null;
        ResultSet rs = null;
        List <Trabajador> trabajadores = new ArrayList<>();
        try {
            conexion = abrirConexion();

            String sentenciaConsultarTrabajador = "SELECT * FROM trabajador WHERE id = ?;";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaConsultarTrabajador);
            sentencia.setInt(1, id);
            rs = sentencia.executeQuery();

            while (rs.next()) {
                Trabajador t = new Trabajador(rs.getInt("id"), rs.getString("dni"), rs.getString("nombre"), rs.getString("apellidos"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("puesto"));
                trabajadores.add(t);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (BDException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }
        return true;
    }

    public static List <Trabajador> consultarTrabajadores() throws BDException {
        Connection conexion = null;
        ResultSet rs = null;
        List <Trabajador> trabajadores = new ArrayList<>();

        try {
            conexion = abrirConexion();

            String sentenciaConsultarTrabajadores = "SELECT * FROM trabajador;";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaConsultarTrabajadores);
            rs = sentencia.executeQuery();

            while (rs.next()) {
                Trabajador t = new Trabajador(rs.getInt("id"), rs.getString("dni"), rs.getString("nombre"), rs.getString("apellidos"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("puesto"));
                trabajadores.add(t);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (BDException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }
        return trabajadores;
    }

    public static boolean modificarTrabajador(Trabajador trabajador) throws BDException {
        Connection conexion = null;
        int columnasInsertadas;

        try {
            conexion = abrirConexion();

            String sentenciaModificarTrabajador = "UPDATE trabajador SET dni = ?, nombre = ?, apellidos = ?, direccion = ?, telefono = ?, puesto = ? WHERE id = ?;";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaModificarTrabajador);
            sentencia.setString(1, trabajador.getDni());
            sentencia.setString(2, trabajador.getNombre());
            sentencia.setString(3, trabajador.getApellidos());
            sentencia.setString(4, trabajador.getDireccion());
            sentencia.setString(5, trabajador.getTelefono());
            sentencia.setString(6, trabajador.getPuesto());
            sentencia.setInt(7, trabajador.getIdentificador());
            columnasInsertadas = sentencia.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (BDException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }
        return columnasInsertadas > 0;
    }

    static void main(String[] args) {
        Trabajador trabajador = new Trabajador(1, "1", "a", "a", "a", "2", "hola");
        try {
            insertarTrabajador(trabajador);
        } catch (BDException e) {
            throw new RuntimeException(e);
        }

    }

}
