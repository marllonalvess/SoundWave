package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Usuario;
import model.Venda;

public class UsuarioDAO {

    Connection conn; // conexão
    PreparedStatement st; // tratamento dos dados SQL
    ResultSet rs;

    // construtor simples
    public UsuarioDAO() {
    }

    // metodo conectar
    public boolean conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/soundwave", "root", "Iandra123@");
            System.out.println("Conectado.");
            return true;

        } catch (SQLException | ClassNotFoundException var2) {
            System.out.println("Erro ao conectar: " + var2.getMessage());
            return false;
        }
    }

    public int salvar(Usuario u) {
        try {
            conectar();

            this.st = this.conn.prepareStatement(
                    "INSERT INTO usuarios(login, senha) VALUES(?,?)"
            );
            this.st.setString(1, u.getLogin());
            this.st.setString(2, u.getSenha());

            return this.st.executeUpdate(); // sucesso → retorna 1

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                System.out.println("Usuário já existe!");
                return 1062;
            }

            System.out.println("Erro ao salvar usuário: " + e.getMessage());
            return -1;

        } finally {
            desconectar(); // garante fechar conexão
        }
    }

    public void listar(JTable tabela) {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setNumRows(0);

        try {
            this.conectar();
            this.st = this.conn.prepareStatement("SELECT * FROM usuarios");
            this.rs = this.st.executeQuery();

            while (this.rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("senha")
                });
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar: " + e.getMessage());
        } finally {
            this.desconectar();
        }
    }

    public boolean login(String login, String senha) {

        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";

        try {
            conectar();
            st = conn.prepareStatement(sql);

            st.setString(1, login);
            st.setString(2, senha);

            rs = st.executeQuery();

            return rs.next(); // true se achou usuário

        } catch (Exception e) {
            System.out.println("Erro login: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {

        String sql = "DELETE FROM usuarios WHERE id = ?";

        try {
            conectar();

            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            int linhasAfetadas = st.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir: " + e.getMessage());
            return false;

        } finally {
            desconectar();
        }
    }

    public int atualizar(Usuario u) {
        try {
            conectar();
            this.st = this.conn.prepareStatement(
                    "UPDATE usuarios SET login = ?, senha = ? WHERE id = ?"
            );

            this.st.setString(1, u.getLogin());
            this.st.setString(2, u.getSenha());
            this.st.setInt(3, u.getId());
            return this.st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuario: " + e.getMessage());
            return 0;

        } finally {
            desconectar();
        }
    }

    public int pegarIdUsuario(String usuarioLogado) {
        int id = -1;
        try {
            conectar();
            st = conn.prepareStatement("SELECT id FROM usuarios WHERE login = ?");
            st.setString(1, usuarioLogado);
            rs = st.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao pegar ID do usuário: " + e.getMessage());
        } finally {
            desconectar();
        }
        return id;
    }

    public void desconectar() {
        try {
            conn.close();
        } catch (SQLException var2) {
        }
    }
}
