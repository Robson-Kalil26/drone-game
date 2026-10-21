public class Carregando implements EstadoDrone {

    private static final int RECARGA_POR_TICK = 4;

    @Override
    public String getNome() { return "Carregando"; }

    @Override
    public void aoEntrar(Drone drone) {
        // Se já tem o pacote e pousou na estação, a missão termina.
        if (drone.getCenario().isPacoteColetado()) {
            drone.finalizarMissao();
        }
    }

    @Override
    public void tratarComando(Drone drone, Comando comando) {
        // Não responde a comandos durante a recarga.
    }

    @Override
    public void atualizar(Drone drone) {
        drone.recarregar(RECARGA_POR_TICK);
        if (drone.getBateria() >= Drone.BATERIA_MAXIMA) {
            drone.log("Bateria cheia.");
            drone.setEstado(new Repouso());
        }
    }
}