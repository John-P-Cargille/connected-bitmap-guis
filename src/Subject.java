import java.util.ArrayList;

//Represents a Subject in the Observer pattern
public class Subject {

    //A collection of attached Observers
    private ArrayList < Observer > allObservers;

    //Public constructor initializes Observer list
    public Subject() {
        allObservers = new ArrayList<>();
    }

    //Attaches an observer
    public void attach(Observer observer) {
        allObservers.add(observer);
    }

    //Detaches an observer
    public void detach(Observer observer) {
        allObservers.removeIf(givenObserver -> givenObserver.equals(observer));
    }

    //Notifies Observers of a change
    public void notifyObservers() {
        for(Observer givenObserver : allObservers) {
            //Calls update for each observer
            givenObserver.update();
        }
    }
}
