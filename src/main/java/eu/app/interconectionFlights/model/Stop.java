package eu.app.interconectionFlights.model;



public class Stop {
    private Route from;
    private Route to;

    public Stop() {
        super();
    }

    public Stop(Route from, Route to) {
        super();
        this.from = from;
        this.to = to;
    }

    public Route getFrom() {
        return from;
    }

    public Route getTo() {
        return to;
    }
    
    
}