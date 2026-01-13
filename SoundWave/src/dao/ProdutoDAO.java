package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Produto;

public class ProdutoDAO {

    Connection conn; // conexão
    PreparedStatement st; // tratamento dos dados SQL
    ResultSet rs;

    // construtor simples
    public ProdutoDAO() {
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

    public void listar(JTable tabela) {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setNumRows(0); // limpa a tabela

        try {
            this.conectar();
            this.st = this.conn.prepareStatement("SELECT * FROM produtos");
            this.rs = this.st.executeQuery();

            while (this.rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("modelo"),
                    rs.getString("categoria"),
                    rs.getString("tipo"),
                    rs.getString("marca"),
                    rs.getDouble("valor"),
                    rs.getInt("estoque")
                });
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar: " + e.getMessage());
        } finally {
            this.desconectar();
        }
    }

    public int salvar(Produto p) {
        try {
            conectar();
            this.st = this.conn.prepareStatement(
                    "INSERT INTO produtos(modelo, categoria, tipo, marca, valor, estoque) VALUES(?,?,?,?,?,?)"
            );

            this.st.setString(1, p.getModelo());
            this.st.setString(2, p.getCategoria());
            this.st.setString(3, p.getTipo());
            this.st.setString(4, p.getMarca());
            this.st.setDouble(5, p.getValor());
            this.st.setInt(6, p.getEstoque());

            return this.st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao salvar produto: " + e.getMessage());
            return 0;

        } finally {
            desconectar();
        }
    }

    public int atualizar(Produto p) {
        try {
            conectar();
            this.st = this.conn.prepareStatement(
                    "UPDATE produtos SET modelo=?, categoria=?, tipo=?, marca=?, valor=?, estoque=? WHERE id=?"
            );

            this.st.setString(1, p.getModelo());
            this.st.setString(2, p.getCategoria());
            this.st.setString(3, p.getTipo());
            this.st.setString(4, p.getMarca());
            this.st.setDouble(5, p.getValor());
            this.st.setInt(6, p.getEstoque());
            this.st.setInt(7, p.getId());

            return this.st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
            return 0;

        } finally {
            desconectar();
        }
    }

    public boolean excluir(int id) {

        String sql = "DELETE FROM produtos WHERE id = ?";

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

    public boolean atualizarEstoque(int idProduto, int qtdVendida) {
        try {
            conectar();
            PreparedStatement st = conn.prepareStatement(
                    "UPDATE produtos SET estoque = estoque - ? WHERE id = ?"
            );
            st.setInt(1, qtdVendida);
            st.setInt(2, idProduto);
            int linhasAfetadas = st.executeUpdate();
            desconectar();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar estoque: " + e.getMessage());
            return false;
        }
    }

    public void desconectar() {
        try {
            conn.close();
        } catch (SQLException var2) {
        }
    }

}
