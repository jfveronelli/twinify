package ar.org.crossknight.twinify;

import java.awt.EventQueue;

import ar.org.crossknight.twinify.ui.AppFrame;

public final class App implements Runnable {

	private App() {}

    public static final void main(String[] args) {
        EventQueue.invokeLater(new App());
    }

    @Override
    public void run() {
        new AppFrame().setVisible(true);
    }

}