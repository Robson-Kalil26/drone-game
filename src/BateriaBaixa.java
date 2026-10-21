import java.awt.Point;
import java.util.List;


public class BateriaBaixa implements EstadoDrone {

    private List<Point> rota;

    @Override
    public String getNome() { return "Bateria Baixa"; }

    @Override
    public void aoEntrar(Drone drone) {
        Cenario c = drone.getCenario();
        rota = c.menorRota(new Point(drone.getX(), drone.getY()), c.getEstacao());
        drone.setRotaVisivel(rota);
        drone.log("Retornando automaticamente à estação (" + rota.size() + " passos).");
    }

    @Override
    public void tratarComando(Drone drone, Comando comando) {
        // Ignora os comandos do jogador.
    }

    @Override
    public void atualizar(Drone drone) {
        if (!rota.isEmpty()) {
            Point proximo = rota.remove(0);
            drone.teletransportarPara(proximo.x, proximo.y);
        }
        if (rota.isEmpty()) {
            drone.log("Pouso automático na estação.");
            drone.setRotaVisivel(null);
            drone.setEstado(new Carregando());             // TRANSIÇÃO: BateriaBaixa -> Carregando
        }
    }
}