import java.awt.Point;
import java.util.*;

public class Cenario {

    // '.' livre | 'T' árvore | 'E' estação (início do drone) | 'P' pacote
    private static final String[] MAPA = {
            "...T.......T...",
            ".T.....T...T...",
            ".....TT........",
            "..T.....T..TT..",
            "....T..T.T.....",
            "TT...T.....T..P",
            "...T...TT......",
            ".T...T.....T...",
            "...T..T..T.....",
            ".T.....T....T..",
            "E..T.......T..."
    };

    private final int colunas = MAPA[0].length();
    private final int linhas = MAPA.length;
    private final boolean[][] arvore = new boolean[colunas][linhas];
    private Point estacao;
    private Point pacote;
    private boolean pacoteColetado = false;

    public Cenario() {
        for (int y = 0; y < linhas; y++) {
            for (int x = 0; x < colunas; x++) {
                char c = MAPA[y].charAt(x);
                if (c == 'T') arvore[x][y] = true;
                else if (c == 'E') estacao = new Point(x, y);
                else if (c == 'P') pacote = new Point(x, y);
            }
        }
    }

    public boolean isLivre(int x, int y) {
        return x >= 0 && y >= 0 && x < colunas && y < linhas && !arvore[x][y];
    }

    public boolean pacoteEm(int x, int y) {
        return !pacoteColetado && pacote.x == x && pacote.y == y;
    }

    public List<Point> menorRota(Point origem, Point destino) {
        Map<Point, Point> anterior = new HashMap<>();
        Deque<Point> fila = new ArrayDeque<>();
        fila.add(origem);
        anterior.put(origem, null);
        int[][] dirs = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        while (!fila.isEmpty()) {
            Point atual = fila.poll();
            if (atual.equals(destino)) break;
            for (int[] d : dirs) {
                Point p = new Point(atual.x + d[0], atual.y + d[1]);
                if (isLivre(p.x, p.y) && !anterior.containsKey(p)) {
                    anterior.put(p, atual);
                    fila.add(p);
                }
            }
        }
        LinkedList<Point> rota = new LinkedList<>();
        if (!anterior.containsKey(destino)) return rota;
        for (Point p = destino; p != null && !p.equals(origem); p = anterior.get(p)) {
            rota.addFirst(p);
        }
        return rota;
    }

    public boolean isArvore(int x, int y) { return arvore[x][y]; }
    public int getColunas() { return colunas; }
    public int getLinhas() { return linhas; }
    public Point getEstacao() { return estacao; }
    public Point getPacote() { return pacote; }
    public boolean isPacoteColetado() { return pacoteColetado; }
    public void coletarPacote() { pacoteColetado = true; }
}