/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetofinal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author CG3040224
 */
public class Agencia {
    private ArrayList<ContaBancaria> contas;

    public Agencia() {
        this.contas = new ArrayList<>();
    }
    
    public void cadastrarConta(ContaBancaria novaConta) {
        contas.add(novaConta);
        System.out.println("Conta número " + novaConta.getNumeroConta() + " cadastrada com sucesso!");
    }

    public ContaBancaria buscarPorNumero(String numeroConta) throws ContaNaoEncontradaException {
        for (ContaBancaria conta : contas) {
            if (conta.getNumeroConta().equals(numeroConta)) {
                return conta;
            }
        }
        throw new ContaNaoEncontradaException("Erro: Conta número " + numeroConta + " não foi encontrada.");
    }

    public ContaBancaria buscarPorCpf(String cpf) throws ContaNaoEncontradaException {
        for (ContaBancaria conta : contas) {
            if (conta.getCpfTitular().equals(cpf)) {
                return conta;
            }
        }
        throw new ContaNaoEncontradaException("Erro: Nenhuma conta vinculada ao CPF " + cpf + " foi encontrada.");
    }

    public ArrayList<ContaBancaria> listarTodas() {
        return this.contas;
    }

    public void editarContato(String numeroConta, String novoTelefone) throws ContaNaoEncontradaException {
        ContaBancaria conta = buscarPorNumero(numeroConta);
        conta.setTelefone(novoTelefone);
        System.out.println("Telefone do titular da conta " + numeroConta + " atualizado com sucesso!");
    }

    public void editarLimiteCredito(String numeroConta, double novoLimite) throws ContaNaoEncontradaException {
        ContaBancaria conta = buscarPorNumero(numeroConta);
        
        if (conta instanceof ContaCorrente) {
            ContaCorrente cc = (ContaCorrente) conta;
            cc.setLimiteCredito(novoLimite);
            System.out.println("Limite de crédito da conta corrente " + numeroConta + " alterado para R$ " + novoLimite);
        } else {
            System.out.println("Aviso: A conta " + numeroConta + " não é uma Conta Corrente. Operação cancelada.");
        }
    }

    public void encerrarConta(String numeroConta, String nomeArquivo) throws ContaNaoEncontradaException {

    ContaBancaria conta = buscarPorNumero(numeroConta);

    contas.remove(conta);
    System.out.println("Conta " + numeroConta + " removida da memória.");

    salvarDadosEmArquivo(nomeArquivo);
    System.out.println("Arquivo atualizado com sucesso. Conta excluída permanentemente.");
}
    
    public void salvarDadosEmArquivo(String nomeArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            
            for (ContaBancaria conta : contas) {
               
                if (conta instanceof ContaCorrente) {
                    ContaCorrente cc = (ContaCorrente) conta;
                    writer.write("CC;" + cc.getNumeroConta() + ";" + cc.getCpfTitular() + ";" +
                                 cc.getNomeTitular() + ";" + cc.getTelefone() + ";" + cc.getSenha() + ";" + cc.getSaldo() + ";" +
                                 cc.getLimiteCredito());
                } else if (conta instanceof ContaPoupanca) {
                    ContaPoupanca cp = (ContaPoupanca) conta;
                    writer.write("CP;" + cp.getNumeroConta() + ";" + cp.getCpfTitular() + ";" +
                                 cp.getNomeTitular() + ";" + cp.getTelefone() + ";" + cp.getSenha() + ";" + cp.getSaldo() + ";" +
                                 cp.getTaxaRendimento());
                }
                writer.newLine(); 
            }
            System.out.println("Dados salvos em arquivo com sucesso!");
            
        } catch (IOException e) {
            System.err.println("Erro crítico ao salvar o arquivo: " + e.getMessage());
        }
    }

    public void carregarDadosDoArquivo(String nomeArquivo) {
       
        this.contas.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            
            while ((linha = reader.readLine()) != null) {
             
                if (linha.trim().isEmpty()) continue;

                String[] dados = linha.split(";");
                
               String tipo = dados[0];
                String numero = dados[1];
                String cpf = dados[2];
                String nome = dados[3];
                String telefone = dados[4];
                String senha = dados[5]; 
                double saldo = Double.parseDouble(dados[6]);

                if (tipo.equals("CC")) {
                    double limite = Double.parseDouble(dados[7]);
                    ContaCorrente cc = new ContaCorrente(numero, cpf, nome, telefone, senha, saldo, limite);
                    contas.add(cc);
                } else if (tipo.equals("CP")) {
                    double taxa = Double.parseDouble(dados[7]);
                    ContaPoupanca cp = new ContaPoupanca(numero, cpf, nome, telefone, senha, saldo, taxa);
                    contas.add(cp);
                }
            }
            System.out.println("Dados carregados do arquivo com sucesso! Total de contas: " + contas.size());
            
        } catch (IOException e) {
            System.err.println("Aviso: Arquivo de dados não encontrado ou inacessível. Iniciando com banco vazio. " + e.getMessage());
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Erro: O arquivo de dados está corrompido ou em formato inválido.");
        }
    }
}

