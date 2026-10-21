public interface EstadoDrone {

    String getNome();

    void tratarComando(Drone drone, Comando comando);

    void atualizar(Drone drone);

    default void aoEntrar(Drone drone) { }
}