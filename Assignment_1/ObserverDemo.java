import java.util.*;

// Observer
interface Subscriber {
    void update(String videoTitle);
}

// Concrete Observers
class MobileSubscriber implements Subscriber {
    public void update(String videoTitle) {
        System.out.println("Mobile Notification: New video uploaded - " + videoTitle);
    }
}

class WebSubscriber implements Subscriber {
    public void update(String videoTitle) {
        System.out.println("Web Notification: New video uploaded - " + videoTitle);
    }
}

// Subject
class YouTubeChannel {
    private List<Subscriber> subscribers = new ArrayList<>();

    public void subscribe(Subscriber s) {
        subscribers.add(s);
    }

    public void unsubscribe(Subscriber s) {
        subscribers.remove(s);
    }

    public void uploadVideo(String title) {
        System.out.println("Channel uploaded: " + title);
        notifySubscribers(title);
    }

    private void notifySubscribers(String title) {
        for (Subscriber s : subscribers) {
            s.update(title);
        }
    }
}

// Client
public class ObserverDemo {
    public static void main(String[] args) {
        YouTubeChannel channel = new YouTubeChannel();

        Subscriber mobile = new MobileSubscriber();
        Subscriber web = new WebSubscriber();

        channel.subscribe(mobile);
        channel.subscribe(web);

        channel.uploadVideo("Design Patterns Tutorial");
        channel.uploadVideo("Java Interview Tips");
    }
}
