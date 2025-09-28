import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainWindow extends JFrame {
    private final DrawPanel panel;

    public MainWindow() throws HeadlessException {
        panel = new DrawPanel(100);
        this.add(panel);

        // Добавляем слушатель изменения размера окна
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.onResize();
                panel.repaint();
            }
        });
    }
}