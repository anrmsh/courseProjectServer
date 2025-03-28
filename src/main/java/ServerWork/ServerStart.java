package ServerWork;

import DB.Configs;

public class ServerStart {
    public static final int PORT_WORK = 2525;

    public static void main(String[] args) {
        Server server = new Server(PORT_WORK);
        new Thread(server).start();
//        server.stop();
    }
}