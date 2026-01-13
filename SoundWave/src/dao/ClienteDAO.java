package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Cliente;

public class ClienteDAO {

    Connection conn; // conexão
    PreparedStatement st; // tratamento dos dados SQL
    ResultSet rs;

    // construtor simples
    public ClienteDAO() {
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

    public int salvar(Cliente c) {
        try {
            this.st = this.conn.prepareStatement("INSERT INTO clientes(nome, cpf, telefone) VALUES(?,?,?)");
            this.st.setString(1, c.getNome());
            this.st.setString(2, c.getCpf());
            this.st.setString(3, c.getTelefone());
            int status = this.st.executeUpdate();
            return status;
        } catch (SQLException var5) {
            System.out.println("Erro ao salvar arquivo: " + var5.getMessage());
            return var5.getErrorCode();
        }
    }

    public void listar(JTable tabela) {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setNumRows(0);

        try {
            this.conectar();
            this.st = this.conn.prepareStatement("SELECT * FROM clientes");
            this.rs = this.st.executeQuery();

            while (this.rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),});
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar: " + e.getMessage());
        } finally {
            this.desconectar();
        }
    }

    public boolean cpfExiste(String cpf, int idIgnorar) {
        String sql = "SELECT 1 FROM clientes WHERE cpf = ? AND id <> ?";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            ps.setInt(2, idIgnorar);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return true;
        }
    }

    public boolean excluir(int id) {

        String sql = "DELETE FROM clientes WHERE id = ?";

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

    public int atualizar(Cliente c) {
        try {
            conectar();
            this.st = this.conn.prepareStatement(
                    "UPDATE clientes SET nome = ?, cpf = ?, telefone = ? WHERE id = ?"
            );

            this.st.setString(1, c.getNome());
            this.st.setString(2, c.getCpf());
            this.st.setString(3, c.getTelefone());
            this.st.setInt(4, c.getId());

            return this.st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
            return 0;

        } finally {
            desconectar();
        }
    }

    public void listarIDNOME(JTable tabela) {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setNumRows(0);

        try {
            this.conectar();
            this.st = this.conn.prepareStatement("SELECT id, nome FROM clientes");
            this.rs = this.st.executeQuery();

            while (this.rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nome")
                });
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar: " + e.getMessage());
        } finally {
            this.desconectar();
        }
    }

    public void desconectar() {
        try {
            conn.close();
        } catch (SQLException var2) {
        }
    }

}
