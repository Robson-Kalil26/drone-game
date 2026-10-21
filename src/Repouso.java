public class Repouso implements EstadoDrone {

    @Override
    public String getNome() { return "Repouso"; }

    @Override
    public void tratarComando(Drone drone, Comando comando) {
        switch (comando) {
            case CIMA:
                drone.log("Decolando...");
                drone.setEstado(new Voando());
                break;
            case ACAO:
                if (drone.getCenario().pacoteEm(drone.getX(), drone.getY())) {
                    drone.getCenario().coletarPacote();
                    drone.log("Pacote coletado!");
                } else {
                    drone.log("Não há pacote neste espaço.");
                }
                break;
            default:
                // Setas laterais, seta para baixo e pouso são ignorados no solo.
                break;
        }
    }

    @Override
    public void atualizar(Drone drone) {  }
}