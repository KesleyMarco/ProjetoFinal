/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetofinal;

public abstract class ContaBancaria {
    private String numeroConta;
    private String cpfTitular;
    private String nomeTitular;
    private String telefone; 
    private String senha;
    protected double saldo;

    public ContaBancaria(String numeroConta, String cpfTitular, String nomeTitular, String telefone,String senha, double saldo) {
        this.numeroConta = numeroConta;
        this.cpfTitular = cpfTitular;
        this.nomeTitular = nomeTitular;
        this.telefone = telefone;
        this.senha = senha;
        this.saldo = saldo;
       
    }

    public String getNumeroConta() { return numeroConta; }
    public String getCpfTitular() { return cpfTitular; }
    public String getNomeTitular() { return nomeTitular; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public double getSaldo() { return saldo; }
    public String getSenha() { return senha; }
    
    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
        }
    }

    public void sacar(double valor) throws Exception {
        if (valor > 0 && this.saldo >= valor) {
            this.saldo -= valor;
        } else {
            throw new Exception("Saldo insuficiente para o saque.");
        }
    }
}