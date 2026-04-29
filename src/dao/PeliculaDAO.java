package dao;

import modelo.Actor;
import modelo.Pelicula;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PeliculaDAO {
    String url = "jdbc:mysql://localhost:3306/ud6_act2";
    String user = "root";
    String pass = "1234";


    public void insertarPelicula(Pelicula p) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "INSERT INTO peliculas(titulo, genero, duracion, presupuesto) VALUES(?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getGenero());
            ps.setInt(3, p.getDuracion());
            ps.setDouble(4, p.getPresupuesto());
            ps.executeUpdate();
            System.out.println("Se insertó la película");
        } catch (
                SQLException e) {
            System.out.println("Error de conexión" + e.getMessage());
        }

    }

    public void actualizarPelicula(Pelicula p, int id) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "UPDATE peliculas SET titulo = ?, genero = ?, duracion = ?, presupuesto = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getGenero());
            ps.setInt(3, p.getDuracion());
            ps.setDouble(4, p.getPresupuesto());
            ps.setInt(5, id);
            ps.executeUpdate();
            System.out.println("Se actualizó la película con id:" + id);
        } catch (
                SQLException e) {
            System.out.println("Error de conexión" + e.getMessage());
        }
    }

    public void eliminarPelicula(int id) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "DELETE FROM peliculas WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Se eliminó una película");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Map<Pelicula, Integer> obtenerPeliculasConActores() {
        Map<Pelicula, Integer> resultado = new HashMap<>();
    
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "SELECT p.*, COUNT(r.actor_id) AS total_actores FROM peliculas p " +
                         "LEFT JOIN reparto r ON p.id = r.pelicula_id " +
                         "GROUP BY p.id, p.titulo, p.genero, p.duracion, p.presupuesto";
    
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                Pelicula p = new Pelicula(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("genero"),
                        rs.getInt("duracion"),
                        rs.getDouble("presupuesto")
                );
    
                int totalActores = rs.getInt("total_actores");
    
                resultado.put(p, totalActores);
    
                System.out.println(p.getTitulo() + " | Total Actores: " + totalActores);
            }
    
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    
        return resultado;
    }

    public List<Actor> obtenerActoresPeliculaPorId(int idPelicula) {
        List<Actor> listaActores = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "SELECT a.* FROM actores a JOIN reparto r ON a.id = r.actor_id " +
                    "WHERE r.pelicula_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idPelicula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idActor = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String nacionalidad = rs.getString("nacionalidad");
                int edad = rs.getInt("edad");

                Actor a = new Actor(idActor, nombre, nacionalidad, edad, new ArrayList<>());
                listaActores.add(a);
                System.out.println("Actor: " + nombre + " | Id: " + idActor );
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return listaActores;
    }

    public List<Pelicula> obtenerPeliculasConMasDe3Actores() {
        List<Pelicula> peliculas = new ArrayList<>();
        HashMap<Integer, Pelicula> peliculasYa = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "SELECT p.* FROM peliculas p JOIN reparto r ON p.id = r.pelicula_id " +
                    "GROUP BY p.id, p.titulo, p.genero, p.duracion, p.presupuesto HAVING COUNT(r.actor_id) > 3";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                if(!peliculasYa.containsKey(id)){
                    Pelicula p = new Pelicula(
                            id,
                            rs.getString("titulo"),
                            rs.getString("genero"),
                            rs.getInt("duracion"),
                            rs.getDouble("presupuesto")
                    );
                    peliculas.add(p);
                    peliculasYa.put(id, p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return peliculas;
    }

    public List<Pelicula> obtener3PeliculasConMayorPresupuesto() {
        List<Pelicula> lista3Peliculas = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "SELECT * FROM peliculas ORDER BY presupuesto DESC LIMIT 3";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista3Peliculas.add(new Pelicula(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("duracion"),
                    rs.getDouble("presupuesto")
                    ));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return lista3Peliculas;
    }

    public Pelicula obtenerPeliculaMasLargaPorGenero(String genero) {
        Pelicula pelicula = null;
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "SELECT * FROM peliculas WHERE genero = ? ORDER BY duracion DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, genero);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pelicula = new Pelicula(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("genero"),
                        rs.getInt("duracion"),
                        rs.getDouble("presupuesto")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return pelicula;
    }

}


