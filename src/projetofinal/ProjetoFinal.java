package projetofinal;

import javax.swing.SwingUtilities;

public class ProjetoFinal {
    public static void main(String[] args) {
   
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}