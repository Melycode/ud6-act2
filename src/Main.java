import dao.ActorDAO;
import dao.PeliculaDAO;
import modelo.Actor;
import modelo.Pelicula;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;


public class Main {
  public static void main(String[] args) {
    PeliculaDAO pDAO = new PeliculaDAO();
    Pelicula p = new Pelicula(0, "Interestelar", "Ciencia ficción", 169, 17000000);
  //  pDAO.insertarPelicula(p);
  //  p.setPresupuesto(19000000);
  //  pDAO.actualizarPelicula(p, 11);
  //  pDAO.eliminarPelicula(11);
  //  pDAO.obtenerPeliculasConActores();
  //  pDAO.obtenerActoresPeliculaPorId(3);
  //  System.out.println(pDAO.obtenerPeliculasConMasDe3Actores());
  //    System.out.println(pDAO.obtener3PeliculasConMayorPresupuesto());
  //    System.out.println(pDAO.obtenerPeliculaMasLargaPorGenero("Drama"));


    ActorDAO aDAO = new ActorDAO();
    Actor a = new Actor(0, "Chris Evans", "EEUU", 46, new ArrayList<>());
  //  aDAO.insertarActor(a);
  //  a.setEdad(47);
  //  aDAO.actualizarActor(a, 11);
  //  aDAO.eliminarActor(11);
  //  System.out.println(aDAO.edadMediaActores());
  //  aDAO.asignarActorAPelicula(1,2, "Pikachu");
  //  aDAO.eliminarActorDePelicula(1,2);
  //  System.out.println(aDAO.obtenerActoresPorNacionalidad());
    System.out.println(aDAO.obtenerActoresSinPeliculas());
    }
  }
