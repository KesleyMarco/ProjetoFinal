/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetofinal;

/**
 *
 * @author CG3040224
 */
public class ContaCorrente extends ContaBancaria implements Tributavel {
    double limiteCredito;

    public ContaCorrente(String numeroConta, String cpfTitular, String nomeTitular, String telefone, String senha, double saldo, double limiteCredito) {
        super(numeroConta, cpfTitular, nomeTitular, telefone, senha, saldo);
        this.limiteCredito = limiteCredito;
    }

    public double getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(double limiteCredito) { this.limiteCredito = limiteCredito; }
    
   @Override
    public void sacar(double valor) throws Exception {
        if (valor > 0 && (this.saldo + this.limiteCredito) >= valor) {
            this.saldo -= valor;
        } else {
            throw new Exception("Saldo e limite insuficientes para o saque.");
        }
    }

    @Override
    public void calcularImpostos() {
        this.saldo -= 2.5;
        System.out.println("Impostos calculados e debitados da Conta Corrente.");
    }

    
    
}
