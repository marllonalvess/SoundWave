package model;

public class Produto {

    private int id;
    private String modelo;
    private String categoria;
    private String tipo;
    private String marca;
    private double valor;
    private int estoque;

    // CONSTRUTOR
    public Produto() {

    }


    public String toTXT() {
        return modelo + ";" + categoria + ";" + tipo + ";"
                + marca + ";" + valor + ";" + estoque;
    }

    // GETTER E SETTER
    public String getModelo() {
        return modelo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

}
