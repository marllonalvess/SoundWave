package model;

import java.util.Date;

public class Venda {
    private int id;
    private Date data;
    private int idVendedor;
    private int idCliente;
    private double valor;

    // CONSTRUTOR
    public Venda() {
      
    }

    // GETTER E SETTER
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getidVendedor() {
        return idVendedor;
    }

    public void setidVendedor(int vendedor) {
        this.idVendedor = vendedor;
    }

    public int getidCliente() {
        return idCliente;
    }

    public void setidCliente(int cliente) {
        this.idCliente = cliente;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

}
