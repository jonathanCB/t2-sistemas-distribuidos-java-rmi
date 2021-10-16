import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;

public class Connections {

    String property;
    String registry;
    String remoteHostName;
    int salesNumber;

    public Connections(String property, String registry, String remoteHostName, String salesNumber) {
        this.property = property;
        this.registry = registry;
        this.remoteHostName = remoteHostName;
        this.salesNumber = Integer.parseInt(salesNumber);
    }
    
    public void connectCatalog() throws InterruptedException {
        try {
            System.setProperty("java.rmi.server.hostname", this.property);
            LocateRegistry.createRegistry(Integer.parseInt(this.registry));
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            System.out.println("java RMI registry already exists.");
        }

        try {
            String client = "rmi://" + this.property + ":" + this.registry + "/Catalog2";
            Naming.rebind(client, new Client());
            System.out.println("Catalog Server is ready.");
        } catch (Exception e) {
            System.out.println("Catalog Server failed: " + e);
        }

        String remoteHostName = this.remoteHostName;
        String connectLocation = "rmi://" + remoteHostName + ":20036/Catalog";

        CatalogInterface catalog = null;
        try {
            System.out.println("Connecting to server at : " + connectLocation);
            catalog = (CatalogInterface) Naming.lookup(connectLocation);
        } catch (Exception e) {
            System.out.println("Client failed: ");
            e.printStackTrace();
        }

        int aux = 1;

        while (aux <= salesNumber) {
            System.out.println("\n------- Sale number [" + aux + "] -------");
            int aux2 = 1;
            int qtdMethods = salesNumber;
            /*
             * "Repita o laço enquanto 'aux2=1' for menor ou igual a variável
             * (qtdMethods), que o usuário quer fazer."
             * Esse laço servirá para repetirmos os métodos n vezes em cada venda.
             */
            while (aux2 <= qtdMethods) {
                try {
                    catalog.saleControl(Integer.parseInt(this.registry));
                    System.out.println("\nCall to server...");
                    System.out.println("\n- Method [" + aux2 + "] -");
                    Thread.sleep(1000);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
                aux2++;
            }
            /*
             * Sair do programa quando o número de vendas setado pelo usuário for
             * contemplado.
             */
            if (aux == salesNumber) {
                System.exit(0);
            }
            aux++;
        }
    }
}