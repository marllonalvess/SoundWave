package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Cliente;
import model.Venda;

public class VendaDAO {

    Connection conn; // conexão
    PreparedStatement st; // tratamento dos dados SQL
    ResultSet rs;

    // construtor simples
    public VendaDAO() {
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

    public int salvar(Venda v) {
        try {
            conectar();

            this.st = this.conn.prepareStatement(
                    "INSERT INTO vendas(dataVenda, id_vendedor, id_cliente, total) VALUES(?,?,?,?)"
            );

            java.sql.Date dataSQL = new java.sql.Date(v.getData().getTime());
            this.st.setDate(1, dataSQL);

            this.st.setInt(2, v.getidVendedor());
            this.st.setInt(3, v.getidCliente());
            this.st.setDouble(4, v.getValor());

            int status = this.st.executeUpdate();
            return status;
        } catch (SQLException var5) {
            System.out.println("Erro ao salvar arquivo: " + var5.getMessage());
            var5.printStackTrace(); // <- sempre bom ver o erro completo
            return var5.getErrorCode();
        }
    }

    public void listar(JTable tabela) {

        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0);

        try {
            conectar();

            String sql = """
            SELECT 
                v.id,
                v.dataVenda,
                u.login AS vendedor,
                c.nome AS cliente,
                v.total
            FROM vendas v
            JOIN usuarios u ON v.id_vendedor = u.id
            JOIN clientes c ON v.id_cliente = c.id
            ORDER BY v.id
        """;

            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getDate("dataVenda"),
                    rs.getString("vendedor"),
                    rs.getString("cliente"),
                    rs.getDouble("total")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao listar vendas: " + e.getMessage()
            );
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
