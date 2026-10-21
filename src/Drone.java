import java.awt.Point;
import java.util.List;
import java.util.function.Consumer;

public class Drone {

    public static final int BATERIA_MAXIMA = 100;
    public static final int NIVEL_CRITICO = 20;
    public static final int CONSUMO_POR_MOVIMENTO = 2;

    private final Cenario cenario;
    private int x, y;
    private int bateria = BATERIA_MAXIMA;
    private EstadoDrone estado;
    private boolean missaoFinalizada = false;
    private List<Point> rotaVisivel;
    private Consumer<String> logger = s -> { };

    public Drone(Cenario cenario) {
        this.cenario = cenario;
        this.x = cenario.getEstacao().x;
        this.y = cenario.getEstacao().y;
        this.estado = new Repouso();
    }

    public void receberComando(Comando comando) {
        if (missaoFinalizada) return;
        estado.tratarComando(this, comando);
    }

    public void tick() {
        estado.atualizar(this);
    }

    public void setEstado(EstadoDrone novo) {
        log("Estado: " + estado.getNome() + " -> " + novo.getNome());
        this.estado = novo;
        novo.aoEntrar(this);
    }

    public boolean tentarMover(int dx, int dy) {
        int nx = x + dx, ny = y + dy;
        if (!cenario.isLivre(nx, ny)) return false;
        x = nx; y = ny;
        return true;
    }

    public void teletransportarPara(int nx, int ny) { x = nx; y = ny; }

    public void consumirBateria() {
        bateria = Math.max(0, bateria - CONSUMO_POR_MOVIMENTO);
    }

    public void recarregar(int quantidade) {
        bateria = Math.min(BATERIA_MAXIMA, bateria + quantidade);
    }

    public boolean bateriaCritica() { return bateria <= NIVEL_CRITICO; }

    public boolean estaNaEstacao() {
        return x == cenario.getEstacao().x && y == cenario.getEstacao().y;
    }

    public void finalizarMissao() {
        missaoFinalizada = true;
        log("MISSÃO CONCLUÍDA! Pacote coletado e drone na estação.");
    }

    public void log(String msg) { logger.accept(msg); }


    public void setLogger(Consumer<String> logger) { this.logger = logger; }
    public Cenario getCenario() { return cenario; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getBateria() { return bateria; }
    public EstadoDrone getEstado() { return estado; }
    public boolean isMissaoFinalizada() { return missaoFinalizada; }
    public List<Point> getRotaVisivel() { return rotaVisivel; }
    public void setRotaVisivel(List<Point> rota) { this.rotaVisivel = rota; }
}