class ConfigurationManager {
    private static ConfigurationManager instance;

    private ConfigurationManager() {
        System.out.println("Loading configuration...");
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public void showConfig() {
        System.out.println("App Configurations Loaded Successfully.");
    }
}

// Client
public class SingletonDemo {
    public static void main(String[] args) {
        ConfigurationManager config1 = ConfigurationManager.getInstance();
        ConfigurationManager config2 = ConfigurationManager.getInstance();

        config1.showConfig();
        System.out.println("Same instance? " + (config1 == config2));
    }
}
