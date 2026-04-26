package dao;

import modelo.Actor;
import modelo.Pelicula;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActorDAO {
    String url = "jdbc:mysql://localhost:3306/ud6_act2";
    String user = "root";
    String pass = "1234";

    public void insertarActor(Actor a) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "INSERT INTO actores(nombre, nacionalidad, edad) VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getNacionalidad());
            ps.setInt(3, a.getEdad());
            ps.executeUpdate();
            System.out.println("Se insertó un actor");
        } catch (SQLException e) {
            System.out.println("Error de conexión" + e.getMessage());
        }
    }

    public void actualizarActor(Actor a, int id) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "UPDATE actores SET nombre = ?, nacionalidad = ?, edad = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getNacionalidad());
            ps.setInt(3, a.getEdad());
            ps.setInt(4, id);
            ps.executeUpdate();
            System.out.println("Se actualizó el actor con id:" + id);
        } catch (
                SQLException e) {
            System.out.println("Error de conexión" + e.getMessage());
        }
    }

    public void eliminarActor(int id) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "DELETE FROM actores WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Se eliminó un actor");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void asignarActorAPelicula(int actor_id, int pelicula_id, String personaje) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "INSERT INTO reparto (actor_id, pelicula_id, personaje) VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, actor_id);
            ps.setInt(2, pelicula_id);
            ps.setString(3, personaje);
            ps.executeUpdate();
            System.out.println(" Actor asignado a la película");
        } catch (SQLException e) {
            System.out.println("Error de conexión" + e.getMessage());
        }
    }

    public void eliminarActorDePelicula(int actor_id, int pelicula_id) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "DELETE FROM reparto WHERE actor_id = ? AND pelicula_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, actor_id);
            ps.setInt(2, pelicula_id);
            ps.executeUpdate();
            System.out.println("Actor id=" + actor_id + " eliminado de película id=" + pelicula_id);
        } catch (SQLException e) {
            System.out.println("Error de conexión" + e.getMessage());
        }
    }


    public HashMap<String, Integer> obtenerActoresPorNacionalidad() {
        HashMap<String, Integer> resultado = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "SELECT nacionalidad, COUNT(*) AS total FROM actores GROUP BY nacionalidad";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultado.put(
                        rs.getString("nacionalidad"),
                        rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return resultado;
    }

        public double edadMediaActores () {
            double media = 0;
                try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                String sql = "SELECT AVG(edad) As media FROM actores";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    media = rs.getDouble("media");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
            return media;

        }

    public List<Actor> obtenerActoresSinPeliculas() {
        List<Actor> listaActores = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "SELECT a.* FROM actores a LEFT JOIN reparto r ON a.id = r.actor_id " +
                    "WHERE r.actor_id IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Actor a = new Actor(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("nacionalidad"),
                    rs.getInt("edad"),
                    new ArrayList<>()
                );
                listaActores.add(a);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return listaActores;
    }

    }



