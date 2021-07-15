package com.SitStayCreate.Serialosc;

import java.io.IOException;

public abstract class MonomeController {

    protected DecoratedOSCPortIn decoratedOSCPortIn;
    protected DecoratedOSCPortOut decoratedOSCPortOut;
    protected String id, prefix;
    //Where are we calling setMonomeApp in the old version?
    private MonomeApp monomeApp;
    private static int count = 1;

    public MonomeController(MonomeApp monomeApp,
                            DecoratedOSCPortIn decoratedOSCPortIn,
                            DecoratedOSCPortOut decoratedOSCPortOut) {
        this.monomeApp = monomeApp;
        this.id = String.format("ssc-0%d", count);

        this.decoratedOSCPortIn = decoratedOSCPortIn;
        this.decoratedOSCPortOut = decoratedOSCPortOut;
        count++;
    }

    public String getId() {
        return id;
    }

    public MonomeApp getMonomeApp() {
        return monomeApp;
    }

    public void setMonomeApp(MonomeApp monomeApp) throws IOException {
        this.monomeApp = monomeApp;
        decoratedOSCPortOut.setOscPortOut(monomeApp.getPortNumber());
    }

    public DecoratedOSCPortIn getDecoratedOSCPortIn() {
        return decoratedOSCPortIn;
    }

    public DecoratedOSCPortOut getDecoratedOSCPortOut() { return decoratedOSCPortOut;}

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    };

    public abstract void addSysListeners();

    public abstract void close();
}
