package ar.org.crossknight.twinify.ui;

import java.awt.EventQueue;
import java.util.Locale;

import javax.swing.UIManager;

public final class App implements Runnable {

	private App() {}

    public static final void main(String[] args) throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        EventQueue.invokeLater(new App());
    }

    @Override
    public void run() {
        new AppFrame().setVisible(true);
    }

}