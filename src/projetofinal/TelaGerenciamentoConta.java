package projetofinal;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TelaGerenciamentoConta extends JFrame {
    private ContaBancaria conta;
    private Agencia agencia;
    private String arquivoBanco;
    private String arquivoExtrato;

    private JLabel lblSaldo;
    private JLabel lblLimiteOuTaxa;
    private JLabel lblTelefone;

    private final Color azulescuro = new Color(0, 90, 165);
    private final Color azul = new Color(13, 110, 253);
    private final Color FUNDO_CINZA = new Color(245, 245, 245);

    public TelaGerenciamentoConta(ContaBancaria conta, Agencia agencia, String arquivoBanco) {
        this.conta = conta;
        this.agencia = agencia;
        this.arquivoBanco = arquivoBanco;
        
        String numLimpo = conta.getNumeroConta().replace("/", "-");
        this.arquivoExtrato = "extrato_" + numLimpo + ".txt";

        setTitle("Painel da Conta - " + conta.getNomeTitular());
        setSize(480, 450); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO_CINZA);

        inicializarArquivoExtrato();

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(azulescuro);
        painelTopo.setPreferredSize(new Dimension(480, 50));
        
        JLabel lblTituloTopo = new JLabel("  Painel da Conta - Titular: " + conta.getNomeTitular(), SwingConstants.LEFT);
        lblTituloTopo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTituloTopo.setForeground(Color.WHITE);
        painelTopo.add(lblTituloTopo, BorderLayout.CENTER);
        add(painelTopo, BorderLayout.NORTH);

        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(FUNDO_CINZA);
        painelCentral.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel painelDados = new JPanel(new GridLayout(5, 1, 8, 8));
        painelDados.setBackground(Color.WHITE);
        painelDados.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), "Informações da Conta"));
        
        JLabel lblDadosPessoais = new JLabel(" Titular: " + conta.getNomeTitular() + "  |  CPF: " + conta.getCpfTitular());
        lblDadosPessoais.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JLabel lblContaNum = new JLabel(" Conta: " + conta.getNumeroConta());
        lblContaNum.setFont(new Font("Arial", Font.PLAIN, 13));
        
        lblTelefone = new JLabel(" Telefone: " + conta.getTelefone());
        lblTelefone.setFont(new Font("Arial", Font.PLAIN, 13));
        
        lblSaldo = new JLabel(" Saldo Atual: R$ " + conta.getSaldo());
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldo.setForeground(azulescuro);
        
        lblLimiteOuTaxa = new JLabel();
        lblLimiteOuTaxa.setFont(new Font("Arial", Font.PLAIN, 13));

        painelDados.add(lblDadosPessoais);
        painelDados.add(lblContaNum);
        painelDados.add(lblTelefone);
        painelDados.add(lblSaldo);
        painelDados.add(lblLimiteOuTaxa);
        
        atualizarLabels(); 
        painelCentral.add(painelDados, BorderLayout.CENTER);
        add(painelCentral, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new GridLayout(2, 3, 10, 10));
        painelBotoes.setBackground(FUNDO_CINZA);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        JButton btnDepositar = customizarBotao("Depositar");
        JButton btnSacar = customizarBotao("Sacar");
        JButton btnExtrato = customizarBotao("Ver Extrato");
        JButton btnEditarTel = customizarBotao("Editar Tel.");
        JButton btnAlterarLimite = customizarBotao("Alterar Limite");
        JButton btnImposto = customizarBotao("Pagar Imposto");

        painelBotoes.add(btnDepositar);
        painelBotoes.add(btnSacar);
        painelBotoes.add(btnExtrato);
        painelBotoes.add(btnEditarTel);
        painelBotoes.add(btnAlterarLimite);
        painelBotoes.add(btnImposto);
        
        add(painelBotoes, BorderLayout.SOUTH);

        btnDepositar.addActionListener(e -> {
            String valStr = JOptionPane.showInputDialog(this, "Digite o valor do depósito:");
            if (valStr != null && !valStr.isEmpty()) {
                try {
                    double valor = Double.parseDouble(valStr.replace(",", "."));
                    conta.depositar(valor);
                    salvarEAtualizar("Depósito de R$ " + valor + " realizado!");
                } catch (Exception ex) { erro("Valor inválido."); }
            }
        });

        btnSacar.addActionListener(e -> {
            String valStr = JOptionPane.showInputDialog(this, "Digite o valor do saque:");
            if (valStr != null && !valStr.isEmpty()) {
                try {
                    double valor = Double.parseDouble(valStr.replace(",", "."));
                    conta.sacar(valor);
                    salvarEAtualizar("Saque de R$ " + valor + " realizado!");
                } catch (Exception ex) { erro(ex.getMessage()); }
            }
        });

        btnExtrato.addActionListener(e -> {
            JTextArea textArea = new JTextArea(lerArquivoExtrato());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(350, 250));
            JOptionPane.showMessageDialog(this, scrollPane, "Extrato Bancário", JOptionPane.PLAIN_MESSAGE);
        });

        btnEditarTel.addActionListener(e -> {
            String novoTel = JOptionPane.showInputDialog(this, "Digite o novo telefone (apenas números com DDD):", conta.getTelefone());
            if (novoTel != null) {
                String apenasNumeros = novoTel.replaceAll("[^0-9]", "");
                if (apenasNumeros.isEmpty() || apenasNumeros.length() < 10 || apenasNumeros.length() > 11) {
                    JOptionPane.showMessageDialog(this, 
                        "Telefone inválido! Digite apenas números contendo o DDD (Ex: 11999998888).", 
                        "Erro de Validação", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String telFormatado;
                if (apenasNumeros.length() == 11) {
                    telFormatado = "(" + apenasNumeros.substring(0, 2) + ") " + apenasNumeros.substring(2, 7) + "-" + apenasNumeros.substring(7);
                } else {
                    telFormatado = "(" + apenasNumeros.substring(0, 2) + ") " + apenasNumeros.substring(2, 6) + "-" + apenasNumeros.substring(6);
                }
                conta.setTelefone(telFormatado);
                lblTelefone.setText(" Telefone: " + telFormatado);
                salvarEAtualizar("Telefone atualizado com sucesso!");
            }
        });

        btnAlterarLimite.addActionListener(e -> {
            if (conta instanceof ContaCorrente) {
                String valStr = JOptionPane.showInputDialog(this, "Digite o novo limite de crédito (Máx: 5000):");
                if (valStr != null && !valStr.isEmpty()) {
                    try {
                        double limite = Double.parseDouble(valStr.replace(",", "."));
                        if (limite < 0 || limite > 5000) {
                            JOptionPane.showMessageDialog(this, "Limite inválido! O limite deve ser de R$ 0.00 até R$ 5000.00.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        double dividaAtual = 0;
                        if (conta.getSaldo() < 0) {
                            dividaAtual = Math.abs(conta.getSaldo());
                        }
                        
                        if (limite < dividaAtual) {
                            JOptionPane.showMessageDialog(this, "Operação negada! O novo limite (R$ " + limite + ") não pode ser menor do que a sua dívida atual no limite (R$ " + dividaAtual + ").", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        ((ContaCorrente) conta).setLimiteCredito(limite);
                        salvarEAtualizar("Limite de crédito atualizado!");
                    } catch (Exception ex) { erro("Valor inválido."); }
                }
            } else {
                erro("Esta operação só é permitida para Contas Correntes.");
            }
        });

        btnImposto.addActionListener(e -> {
            if (conta instanceof Tributavel) {
                try {
                    ((Tributavel) conta).calcularImpostos();
                    salvarEAtualizar("Impostos recolhidos com sucesso!");
                } catch (Exception ex) {
                    erro(ex.getMessage());
                }
            } else {
                erro("Esta conta não possui tarifas tributáveis.");
            }
        });
    }

    private JButton customizarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(azul);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return botao;
    }

    private void inicializarArquivoExtrato() {
        java.io.File arquivo = new java.io.File(arquivoExtrato);
        if (!arquivo.exists()) {
            try {
                arquivo.createNewFile();
                registrarTransacaoExtrato("--- HISTÓRICO DE EXTRATO INICIALIZADO ---");
            } catch (java.io.IOException e) {
                System.err.println("Erro ao criar arquivo de extrato: " + e.getMessage());
            }
        }
    }

    private void registrarTransacaoExtrato(String message) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(arquivoExtrato, true))) {
            writer.write(message);
            writer.newLine();
        } catch (java.io.IOException e) {
            System.err.println("Erro ao gravar histórico no extrato: " + e.getMessage());
        }
    }

    private String lerArquivoExtrato() {
        StringBuilder conteudo = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoExtrato))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                conteudo.append(linha).append("\n");
            }
        } catch (IOException e) {
            return "Histórico de extrato vazio ou não criado ainda.";
        }
        return conteudo.toString();
    }

    private void atualizarLabels() {
        lblSaldo.setText(" Saldo Atual: R$ " + conta.getSaldo());
        if (conta instanceof ContaCorrente) {
            ContaCorrente cc = (ContaCorrente) conta;
            lblLimiteOuTaxa.setText(" Limite de Crédito: R$ " + cc.getLimiteCredito());
        } else if (conta instanceof ContaPoupanca) {
            ContaPoupanca cp = (ContaPoupanca) conta;
            lblLimiteOuTaxa.setText(" Taxa de Rendimento: " + cp.getTaxaRendimento() + "%");
        }
    }

    private void salvarEAtualizar(String messageSucesso) {
        agencia.salvarDadosEmArquivo(arquivoBanco);
        registrarTransacaoExtrato(messageSucesso);
        atualizarLabels();
        JOptionPane.showMessageDialog(this, messageSucesso, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}