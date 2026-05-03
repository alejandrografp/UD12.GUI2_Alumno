package dao;

import config.ConfigMySql;
import excepciones.BDException;
import modelo.Trabajador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static config.ConfigMySql.abrirConexion;

public class AccesoPuestos {
    public static List<String> consultarPuestos() throws BDException {
        Connection conexion = null;
        ResultSet rs = null;
        List<String> puestos = new ArrayList<>();
        try {
            conexion = abrirConexion();

            String sentenciaConsultarPuestos = "SELECT * FROM puestos;";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaConsultarPuestos);
            rs = sentencia.executeQuery();

            while(rs.next()) {
                puestos.add(rs.getString("nombre"));
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
        return puestos;
    }

    public static int consultarIdPuesto(String nombre) throws BDException {
        Connection conexion = null;
        ResultSet rs = null;
        try {
            conexion = abrirConexion();

            String sentenciaConsultarPuestos = "SELECT * FROM puestos where nombre = ?;";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaConsultarPuestos);
            sentencia.setString(1, nombre);
            rs = sentencia.executeQuery();

            if(rs.next()) {
                return rs.getInt("id");
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
        return 1;
    }
}
