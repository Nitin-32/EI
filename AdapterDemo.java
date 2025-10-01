// Target interface
interface PaymentProcessor {
    void processPayment(double amount);
}

// Existing system
class CreditCardProcessor implements PaymentProcessor {
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment of $" + amount);
    }
}

// Third-party class (incompatible interface)
class CryptoGateway {
    public void makeTransaction(double coins) {
        System.out.println("Processing crypto payment of " + coins + " BTC");
    }
}

// Adapter
class CryptoAdapter implements PaymentProcessor {
    private CryptoGateway cryptoGateway;

    public CryptoAdapter(CryptoGateway gateway) {
        this.cryptoGateway = gateway;
    }

    public void processPayment(double amount) {
        double btc = amount / 30000; // example conversion
        cryptoGateway.makeTransaction(btc);
    }
}

// Client
public class AdapterDemo {
    public static void main(String[] args) {
        PaymentProcessor card = new CreditCardProcessor();
        card.processPayment(500);

        PaymentProcessor crypto = new CryptoAdapter(new CryptoGateway());
        crypto.processPayment(60000);
    }
}
