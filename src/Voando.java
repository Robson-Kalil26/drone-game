public class Voando implements EstadoDrone {

    @Override
    public String getNome() { return "Voando"; }

    @Override
    public void tratarComando(Drone drone, Comando comando) {
        switch (comando) {
            case CIMA:     mover(drone, 0, -1); break;
            case BAIXO:    mover(drone, 0, 1);  break;
            case ESQUERDA: mover(drone, -1, 0); break;
            case DIREITA:  mover(drone, 1, 0);  break;
            case POUSAR:
                if (drone.estaNaEstacao()) {
                    drone.log("Pouso na estação de recarga.");
                    drone.setEstado(new Carregando());     // TRANSIÇÃO: Voando -> Carregando
                } else {
                    drone.log("Pouso fora da estação.");
                    drone.setEstado(new Repouso());        // TRANSIÇÃO: Voando -> Repouso
                }
                break;
            default:
                drone.log("Não é possível coletar enquanto voa. Pouse sobre o pacote.");
                break;
        }
    }

    private void mover(Drone drone, int dx, int dy) {
        if (!drone.tentarMover(dx, dy)) {
            drone.log("Bloqueado por árvore/limite do mapa.");
            return;
        }
        drone.consumirBateria();
        if (drone.bateriaCritica()) {
            drone.log("BATERIA CRÍTICA (" + drone.getBateria() + "%)!");
            drone.setEstado(new BateriaBaixa());
        }
    }

    @Override
    public void atualizar(Drone drone) { }
}