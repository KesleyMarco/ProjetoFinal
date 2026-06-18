/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetofinal;

/**
 *
 * @author CG3040224
 */
public class ContaPoupanca extends ContaBancaria{
    double taxaRendimento;

    public ContaPoupanca(String numeroConta, String cpfTitular, String nomeTitular, String telefone,String senha, double saldo, double taxaRendimento) {
        super(numeroConta, cpfTitular, nomeTitular, telefone, senha,saldo);
        this.taxaRendimento = taxaRendimento;
    }

    public double getTaxaRendimento() { return taxaRendimento; }
    public void setTaxaRendimento(double taxaRendimento) { this.taxaRendimento = taxaRendimento; }
    public void aplicarRendimento() {
        this.saldo += this.saldo * (taxaRendimento / 100);
        System.out.println("Rendimento aplicado à poupança.");
    }

}
