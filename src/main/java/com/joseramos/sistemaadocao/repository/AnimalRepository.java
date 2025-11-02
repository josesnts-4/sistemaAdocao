import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// Importe suas classes de modelo (Animal, Cachorro, Gato)
import com.joseramos.sistemaadocao.connection.ConnectionFactory;
import com.joseramos.sistemaadocao.entidades.Animal;
import com.joseramos.sistemaadocao.entidades.Cachorro;
import com.joseramos.sistemaadocao.entidades.Gato;

public class AnimalRepository {

    // --- CADASTRAR (Create) ---
    public void cadastrar(Animal animal) {
        String sql = "INSERT INTO animais (nome, idade, raca, tipo, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, animal.getNome());
            pstmt.setInt(2, animal.getIdade());
            pstmt.setString(3, animal.getRaca());

            // Diferencia o tipo na hora de salvar
            if (animal instanceof Cachorro) {
                pstmt.setString(4, "CACHORRO");
            } else if (animal instanceof Gato) {
                pstmt.setString(4, "GATO");
            }

            pstmt.setString(5, animal.getStatus().toString()); // Assumindo que status é um Enum

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar animal: " + e.getMessage());
        }
    }

    // --- LISTAR (Read) ---
    public List<Animal> listar() {
        List<Animal> animais = new ArrayList<>();
        String sql = "SELECT * FROM animais";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Animal animal;
                String tipo = rs.getString("tipo");

                // Recria o objeto correto (Cachorro ou Gato)
                if ("CACHORRO".equals(tipo)) {
                    animal = new Cachorro(); // Use o construtor adequado
                } else {
                    animal = new Gato(); // Use o construtor adequado
                }

                animal.setId(rs.getInt("id"));
                animal.setNome(rs.getString("nome"));
                animal.setIdade(rs.getInt("idade"));
                animal.setRaca(rs.getString("raca"));
                // animal.setStatus(StatusEnum.valueOf(rs.getString("status")));

                animais.add(animal);
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar animais: " + e.getMessage());
        }
        return animais;
    }

    // --- ATUALIZAR (Update) ---
    public void atualizar(Animal animal) {
        // Crie o SQL para UPDATE (ex: "UPDATE animais SET nome = ?, status = ? WHERE id = ?")
        // ...
    }

    // --- REMOVER (Delete) ---
    public void remover(int id) {
        // Crie o SQL para DELETE (ex: "DELETE FROM animais WHERE id = ?")
        // ...
    }
}