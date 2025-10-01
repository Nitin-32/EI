// Strategy Interface
interface ShippingStrategy {
    void ship(String product);
}

// Concrete Strategies
class StandardShipping implements ShippingStrategy {
    public void ship(String product) {
        System.out.println("Shipping " + product + " via Standard Shipping.");
    }
}

class ExpressShipping implements ShippingStrategy {
    public void ship(String product) {
        System.out.println("Shipping " + product + " via Express Shipping.");
    }
}

class DroneShipping implements ShippingStrategy {
    public void ship(String product) {
        System.out.println("Shipping " + product + " via Drone Delivery.");
    }
}

// Context
class ShippingContext {
    private ShippingStrategy strategy;

    public void setStrategy(ShippingStrategy strategy) {
        this.strategy = strategy;
    }

    public void shipOrder(String product) {
        strategy.ship(product);
    }
}

// Client
public class StrategyDemo {
    public static void main(String[] args) {
        ShippingContext context = new ShippingContext();

        context.setStrategy(new StandardShipping());
        context.shipOrder("Laptop");

        context.setStrategy(new ExpressShipping());
        context.shipOrder("Mobile Phone");

        context.setStrategy(new DroneShipping());
        context.shipOrder("Medicines");
    }
}
