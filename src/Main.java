import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

  public static void main() {
    String url = "jdbc:mysql://localhost:3306/pruebas";
    String user = "root";
    String pass = "";

    try (Connection conn = DriverManager.getConnection(url, user, pass)) {
      System.out.println("Conexión establecida");
    } catch (SQLException e) {
      System.out.println("Error de conexión");
    }
  }
}
