import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class Main extends JFrame {

    private Drone drone;
    private final PainelCenario painel;
    private final JLabel lblEstado = new JLabel();
    private final JLabel lblPacote = new JLabel();
    private final JProgressBar barraBateria = new JProgressBar(0, Drone.BATERIA_MAXIMA);
    private final JTextArea areaLog = new JTextArea(12, 24);

    public Main() {
        super("Simulador de Drone - Padrão State");
        drone = novoDrone();
        painel = new PainelCenario(drone);

        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 20));
        barraBateria.setStringPainted(true);
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setFocusable(false);

        JTextArea ajuda = new JTextArea(
                "CONTROLES\n" +
                        "↑ ↓ ← →  mover (↑ decola)\n" +
                        "ESPAÇO   coletar pacote\n" +
                        "P        pousar\n" +
                        "R        reiniciar");
        ajuda.setEditable(false);
        ajuda.setFocusable(false);
        ajuda.setOpaque(false);

        lateral.add(new JLabel("Estado atual:"));
        lateral.add(lblEstado);
        lateral.add(Box.createVerticalStrut(10));
        lateral.add(new JLabel("Bateria:"));
        lateral.add(barraBateria);
        lateral.add(Box.createVerticalStrut(10));
        lateral.add(lblPacote);
        lateral.add(Box.createVerticalStrut(10));
        lateral.add(ajuda);
        lateral.add(Box.createVerticalStrut(10));
        lateral.add(new JLabel("Log:"));
        lateral.add(new JScrollPane(areaLog));

        setLayout(new BorderLayout());
        add(painel, BorderLayout.CENTER);
        add(lateral, BorderLayout.EAST);

        configurarTeclas();
        ligarLogger();

        new Timer(150, e -> { drone.tick(); atualizarTela(); }).start();

        atualizarTela();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private Drone novoDrone() { return new Drone(new Cenario()); }

    private void ligarLogger() {
        drone.setLogger(msg -> {
            areaLog.append(msg + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }

    private void reiniciar() {
        drone = novoDrone();
        areaLog.setText("");
        ligarLogger();
        painel.setDrone(drone);
        atualizarTela();
    }

    private void configurarTeclas() {
        tecla("UP", Comando.CIMA);
        tecla("DOWN", Comando.BAIXO);
        tecla("LEFT", Comando.ESQUERDA);
        tecla("RIGHT", Comando.DIREITA);
        tecla("SPACE", Comando.ACAO);
        tecla("P", Comando.POUSAR);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("R"), "reiniciar");
        getRootPane().getActionMap().put("reiniciar", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { reiniciar(); }
        });
    }

    private void tecla(String tecla, Comando comando) {
        String nome = "cmd_" + tecla;
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(tecla), nome);
        getRootPane().getActionMap().put(nome, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                drone.receberComando(comando);   // contexto delega ao estado atual
                atualizarTela();
            }
        });
    }

    private void atualizarTela() {
        lblEstado.setText(drone.getEstado().getNome());
        int b = drone.getBateria();
        barraBateria.setValue(b);
        barraBateria.setString(b + "%");
        barraBateria.setForeground(drone.bateriaCritica() ? new Color(210, 40, 40) : new Color(40, 170, 70));
        lblPacote.setText("Pacote: " + (drone.getCenario().isPacoteColetado() ? "COLETADO" : "não coletado"));
        painel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}