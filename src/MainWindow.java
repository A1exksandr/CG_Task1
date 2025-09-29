import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {
    private final DrawPanel panel;

    public MainWindow() throws HeadlessException {
        panel = new DrawPanel(100);
        this.add(panel);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.onResize();
                panel.repaint();
            }
        });

        // Добавляем обработку клавиш для сброса гонки
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) { // R - reset
                    panel.resetRace();
                } else if (e.getKeyCode() == KeyEvent.VK_F5) { // F5
                    panel.resetRace();
                }
            }
        });

        this.setFocusable(true);
        this.requestFocus();
    }
}