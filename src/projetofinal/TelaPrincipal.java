package projetofinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaPrincipal extends JFrame {
    private Agencia agencia;
    private String arquivoBanco = "banco_dados.csv";

    private final Color AMARELO_BB = new Color(252, 222, 4);
    private final Color azul = new Color(0, 90, 165);
    private final Color FUNDO_CLARO = new Color(240, 240, 240);

    public TelaPrincipal() {
        agencia = new Agencia();
        agencia.carregarDadosDoArquivo(arquivoBanco);

        setTitle("Sistema Bancário - Banco do Brasil");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO_CLARO);

        JPanel painelBanner = new JPanel(new BorderLayout());
        painelBanner.setBackground(AMARELO_BB);
        painelBanner.setPreferredSize(new Dimension(650, 80));
        
        JLabel lblLogo = new JLabel("  BANCO DO BRASIL", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 26));
        lblLogo.setForeground(azul);
        painelBanner.add(lblLogo, BorderLayout.CENTER);
        add(painelBanner, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("Arial", Font.PLAIN, 14));

        abas.addTab("Cadastrar", criarPainelCadastro());
        abas.addTab("Entrar", criarPainelEntrar());
        abas.addTab("Listar Contas", criarPainelListar());
        abas.addTab("Encerrar Conta", criarPainelEncerrar());

        add(abas, BorderLayout.CENTER);
    }

    private JPanel criarPainelEntrar() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Entrar na Conta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel lblConta = new JLabel("Número da Conta:");
        lblConta.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField txtNumConta = new JTextField(15);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 14));
        JPasswordField txtSenha = new JPasswordField(15);

        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setBackground(Color.WHITE);
        btnEntrar.setForeground(Color.BLACK);
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        btnEntrar.setPreferredSize(new Dimension(100, 35));

        btnEntrar.addActionListener(e -> {
            String numConta = txtNumConta.getText();
            String senhaDigitada = new String(txtSenha.getPassword());

            if (!numConta.isEmpty() && !senhaDigitada.isEmpty()) {
                try {
                    ContaBancaria conta = agencia.buscarPorNumero(numConta);
                    if (!conta.getSenha().equals(senhaDigitada)) {
                        throw new Exception("Senha incorreta! Acesso negado.");
                    }
                    TelaGerenciamentoConta painelCliente = new TelaGerenciamentoConta(conta, agencia, arquivoBanco);
                    painelCliente.setVisible(true);
                    
                    txtNumConta.setText("");
                    txtSenha.setText("");
                } catch (ContaNaoEncontradaException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Acesso", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos para entrar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        painel.add(lblTitulo, gbc);
        
        gbc.gridy = 1; gbc.gridwidth = 1;
        painel.add(lblConta, gbc);
        gbc.gridx = 1;
        painel.add(txtNumConta, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(lblSenha, gbc);
        gbc.gridx = 1;
        painel.add(txtSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(btnEntrar, gbc);

        return painel;
    }

    private JPanel criarPainelCadastro() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNome = new JTextField(15);
        JPasswordField txtSenha = new JPasswordField(15);
        JTextField txtSaldo = new JTextField("0.0", 15);
        String[] tipos = {"Conta Corrente", "Conta Poupança"};
        JComboBox<String> cbTipo = new JComboBox<>(tipos);

        JFormattedTextField txtNumero = null;
        JFormattedTextField txtCpf = null;
        JFormattedTextField txtTelefone = null;
        try {
            javax.swing.text.MaskFormatter mascaraNum = new javax.swing.text.MaskFormatter("####");
            mascaraNum.setPlaceholderCharacter('_');
            txtNumero = new JFormattedTextField(mascaraNum);
            txtNumero.setColumns(15);

            javax.swing.text.MaskFormatter mascaraCpf = new javax.swing.text.MaskFormatter("###.###.###-##");
            mascaraCpf.setPlaceholderCharacter('_');
            txtCpf = new JFormattedTextField(mascaraCpf);
            txtCpf.setColumns(15);

            javax.swing.text.MaskFormatter mascaraTel = new javax.swing.text.MaskFormatter("(##) #####-####");
            mascaraTel.setPlaceholderCharacter('_');
            txtTelefone = new JFormattedTextField(mascaraTel);
            txtTelefone.setColumns(15);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        final JFormattedTextField campoNumero = txtNumero;
        final JFormattedTextField campoCpf = txtCpf;
        final JFormattedTextField campoTelefone = txtTelefone;

        JButton btnCadastrar = new JButton("CADASTRAR");
        btnCadastrar.setBackground(azul);
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 12));

        gbc.gridx = 0; gbc.gridy = 0; painel.add(new JLabel("Tipo de Conta:"), gbc);
        gbc.gridx = 1; painel.add(cbTipo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; painel.add(new JLabel("Número da Conta (4 dígitos):"), gbc);
        gbc.gridx = 1; painel.add(campoNumero, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painel.add(new JLabel("CPF do Titular:"), gbc);
        gbc.gridx = 1; painel.add(campoCpf, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painel.add(new JLabel("Nome do Titular:"), gbc);
        gbc.gridx = 1; painel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 4; painel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; painel.add(campoTelefone, gbc);

        gbc.gridx = 0; gbc.gridy = 5; painel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; painel.add(txtSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 6; painel.add(new JLabel("Saldo Inicial (R$):"), gbc);
        gbc.gridx = 1; painel.add(txtSaldo, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(btnCadastrar, gbc);

        btnCadastrar.addActionListener(e -> {
            try {
                String numDigitado = campoNumero.getText();
                String nome = txtNome.getText().trim();
                String senha = new String(txtSenha.getPassword()).trim();
                String cpfCompleto = campoCpf.getText();
                String telCompleto = campoTelefone.getText();
                
                // Remove pontos e traços para contar apenas os números do CPF
                String cpfApenasNumeros = cpfCompleto.replaceAll("[^0-9]", "");

                // Verifica se faltam dados básicos ou se o número da conta/telefone estão incompletos
                if (numDigitado.contains("_") || telCompleto.contains("_") || nome.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos corretamente com o tamanho exigido.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Nova trava exclusiva para o CPF (exatos 11 dígitos)
                if (cpfApenasNumeros.length() != 11) {
                    JOptionPane.showMessageDialog(this, "O CPF deve conter exatamente 11 números.", "CPF Inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String numeroFinal = numDigitado + "-1";

                double saldo = Double.parseDouble(txtSaldo.getText().replace(",", "."));
                ContaBancaria novaConta;

                if (cbTipo.getSelectedIndex() == 0) {
                    novaConta = new ContaCorrente(numeroFinal, cpfCompleto, nome, telCompleto, senha, saldo, 5000.0);
                } else {
                    novaConta = new ContaPoupanca(numeroFinal, cpfCompleto, nome, telCompleto, senha, saldo, 12.0);
                }

                agencia.cadastrarConta(novaConta);
                agencia.salvarDadosEmArquivo(arquivoBanco);

                JOptionPane.showMessageDialog(this, "Conta " + numeroFinal + " cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                campoNumero.setValue(null); campoCpf.setValue(null); txtNome.setText("");
                campoTelefone.setValue(null); txtSenha.setText(""); txtSaldo.setText("0.0");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro no cadastro: Verifique os dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return painel; // <-- O RETORNO E FECHAMENTO DO PAINEL QUE FALTAVAM ESTÃO AQUI!
    }
        
    private JPanel criarPainelListar() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextArea txtLista = new JTextArea();
        txtLista.setEditable(false);
        txtLista.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(txtLista);

        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.setBackground(azul);
        btnAtualizar.setForeground(Color.WHITE);

        Runnable atualizarLista = () -> {
            StringBuilder lista = new StringBuilder("Contas Registradas:\n\n");
            for (ContaBancaria c : agencia.listarTodas()) {
                lista.append("Conta: ").append(c.getNumeroConta())
                     .append(" | Titular: ").append(c.getNomeTitular())
                     .append(" | Saldo: R$ ").append(c.getSaldo()).append("\n");
            }
            txtLista.setText(lista.toString());
        };

        btnAtualizar.addActionListener(e -> atualizarLista.run());
        atualizarLista.run();

        painel.add(scroll, BorderLayout.CENTER);
        painel.add(btnAtualizar, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarPainelEncerrar() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblInfo = new JLabel("Digite o número da conta para encerrar permanentemente:");
        JTextField txtNum = new JTextField(15);
        
        JButton btnEncerrar = new JButton("ENCERRAR CONTA");
        btnEncerrar.setBackground(Color.RED);
        btnEncerrar.setForeground(Color.WHITE);
        btnEncerrar.setFont(new Font("Arial", Font.BOLD, 12));

        gbc.gridx = 0; gbc.gridy = 0; painel.add(lblInfo, gbc);
        gbc.gridy = 1; painel.add(txtNum, gbc);
        gbc.gridy = 2; painel.add(btnEncerrar, gbc);

        btnEncerrar.addActionListener(e -> {
            String numConta = txtNum.getText();
            if (numConta != null && !numConta.isEmpty()) {
                try {
                    ContaBancaria conta = agencia.buscarPorNumero(numConta);
                    
                    JPasswordField txtSenhaConfirmacao = new JPasswordField();
                    Object[] formularioSenha = {
                        "Para encerrar a conta, digite a senha de acesso:", txtSenhaConfirmacao
                    };
                    
                    int acaoSenha = JOptionPane.showConfirmDialog(this, formularioSenha, "Confirmação de Segurança", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    
                    if (acaoSenha == JOptionPane.OK_OPTION) {
                        String senhaDigitada = new String(txtSenhaConfirmacao.getPassword());
                        
                        if (conta.getSenha().equals(senhaDigitada)) {
                            int confirmacaoFinal = JOptionPane.showConfirmDialog(this, "Tem certeza absoluta que deseja encerrar a conta " + numConta + "?\nEsta ação não poderá ser desfeita.", "Confirmação Final", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                            
                            if (confirmacaoFinal == JOptionPane.YES_OPTION) {
                                agencia.encerrarConta(numConta, arquivoBanco);
                                JOptionPane.showMessageDialog(this, "Conta " + numConta + " encerrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                                txtNum.setText("");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Senha incorreta! Operação cancelada.", "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    
                } catch (ContaNaoEncontradaException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, digite o número da conta.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        return painel;
    }
}