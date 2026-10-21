import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelCenario extends JPanel {

    private static final int CELULA = 44;
    private Drone drone;

    public PainelCenario(Drone drone) {
        setDrone(drone);
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
        Cenario c = drone.getCenario();
        setPreferredSize(new Dimension(c.getColunas() * CELULA, c.getLinhas() * CELULA));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Cenario c = drone.getCenario();

        for (int y = 0; y < c.getLinhas(); y++) {
            for (int x = 0; x < c.getColunas(); x++) {
                g.setColor((x + y) % 2 == 0 ? new Color(190, 225, 160) : new Color(178, 215, 148));
                g.fillRect(x * CELULA, y * CELULA, CELULA, CELULA);
            }
        }

        // rota automática (Bateria Baixa)
        List<Point> rota = drone.getRotaVisivel();
        if (rota != null) {
            g.setColor(new Color(255, 170, 0, 140));
            for (Point p : rota) {
                g.fillRoundRect(p.x * CELULA + 12, p.y * CELULA + 12, CELULA - 24, CELULA - 24, 8, 8);
            }
        }

        // estação
        Point e = c.getEstacao();
        g.setColor(new Color(60, 120, 220));
        g.fillRoundRect(e.x * CELULA + 4, e.y * CELULA + 4, CELULA - 8, CELULA - 8, 10, 10);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("⚡", e.x * CELULA + 12, e.y * CELULA + 30);

        // árvores
        for (int y = 0; y < c.getLinhas(); y++) {
            for (int x = 0; x < c.getColunas(); x++) {
                if (!c.isArvore(x, y)) continue;
                int px = x * CELULA, py = y * CELULA;
                g.setColor(new Color(110, 70, 30));
                g.fillRect(px + 19, py + 26, 6, 14);
                g.setColor(new Color(30, 110, 40));
                g.fillOval(px + 6, py + 4, 32, 30);
                g.setColor(new Color(45, 140, 55));
                g.fillOval(px + 12, py + 8, 18, 16);
            }
        }

        // pacote
        if (!c.isPacoteColetado()) {
            Point p = c.getPacote();
            g.setColor(new Color(170, 110, 50));
            g.fillRect(p.x * CELULA + 10, p.y * CELULA + 12, CELULA - 20, CELULA - 22);
            g.setColor(new Color(240, 220, 160));
            g.fillRect(p.x * CELULA + 20, p.y * CELULA + 12, 4, CELULA - 22);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(p.x * CELULA + 10, p.y * CELULA + 12, CELULA - 20, CELULA - 22);
        }

        desenharDrone(g);

        if (drone.isMissaoFinalizada()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, getHeight() / 2 - 40, getWidth(), 80);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 28));
            String t = "MISSÃO CONCLUÍDA!  (R = reiniciar)";
            int w = g.getFontMetrics().stringWidth(t);
            g.drawString(t, (getWidth() - w) / 2, getHeight() / 2 + 10);
        }
    }

    private void desenharDrone(Graphics2D g) {
        int cx = drone.getX() * CELULA + CELULA / 2;
        int cy = drone.getY() * CELULA + CELULA / 2;
        boolean noAr = drone.getEstado() instanceof Voando || drone.getEstado() instanceof BateriaBaixa;
        int off = noAr ? -6 : 0;

        if (noAr) { // sombra
            g.setColor(new Color(0, 0, 0, 70));
            g.fillOval(cx - 12, cy + 8, 24, 8);
        }
        Color corpo = drone.getEstado() instanceof BateriaBaixa ? new Color(220, 50, 40)
                : drone.getEstado() instanceof Carregando ? new Color(240, 200, 40)
                  : new Color(50, 50, 60);
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(3));
        g.drawLine(cx - 13, cy - 13 + off, cx + 13, cy + 13 + off);
        g.drawLine(cx - 13, cy + 13 + off, cx + 13, cy - 13 + off);
        g.setColor(noAr ? new Color(120, 120, 120, 200) : new Color(120, 120, 120, 90));
        for (int[] r : new int[][]{{-13, -13}, {13, -13}, {-13, 13}, {13, 13}}) {
            g.fillOval(cx + r[0] - 7, cy + r[1] - 7 + off, 14, 14);
        }
        g.setColor(corpo);
        g.fillOval(cx - 8, cy - 8 + off, 16, 16);
        g.setColor(Color.WHITE);
        g.fillOval(cx - 3, cy - 3 + off, 6, 6);
    }
}