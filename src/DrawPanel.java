import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel implements ActionListener {

    private final int PANEL_WIDTH;
    private final int PANEL_HEIGHT;
    private final int TIMER_DELAY;
    private Timer timer;
    private int ticksFromStart = 0;

    private Amogus amogus;
    private TrafficLight trafficLight;
    private List<Cloud> clouds;
    private int amogusBaseX; // Базовая позиция амогуса для плавного движения

    public DrawPanel(final int width, final int height, final int timerDelay) {
        this.PANEL_WIDTH = width;
        this.PANEL_HEIGHT = height;
        this.TIMER_DELAY = timerDelay;
        timer = new Timer(timerDelay, this);
        timer.start();

        this.trafficLight = new TrafficLight(500, 100, 80, 200);
        this.amogusBaseX = 50; // Начальная позиция
        this.amogus = new Amogus(amogusBaseX, 200, 200, 260, Color.GREEN);
        this.amogus.setTrafficLight(trafficLight);

        // Создаем несколько облаков
        this.clouds = new ArrayList<>();
        clouds.add(new Cloud(100, 50, 60, 40, 1));
        clouds.add(new Cloud(300, 80, 80, 50, 2));
        clouds.add(new Cloud(600, 120, 70, 45, 1));
        clouds.add(new Cloud(800, 70, 90, 55, 3));
    }

    @Override
    public void paint(final Graphics gr) {
        super.paint(gr);

        // Рисуем небо
        gr.setColor(new Color(135, 206, 235)); // Голубое небо
        gr.fillRect(0, 0, getWidth(), getHeight());

        // Рисуем землю
        gr.setColor(new Color(34, 139, 34)); // Зеленая трава
        gr.fillRect(0, getHeight() - 100, getWidth(), 100);

        // Обновляем и рисуем облака
        for (Cloud cloud : clouds) {
            cloud.update();
            cloud.draw(gr);
        }

        // Обновляем и рисуем светофор
        trafficLight.update(ticksFromStart);
        trafficLight.draw(gr);

        // Плавное движение амогуса только на зеленый свет
        if (trafficLight.isGreenLight()) {
            amogusBaseX += 2; // Медленно двигаем базовую позицию
        }

        // Устанавливаем позицию амогуса (он сам решит, двигаться или нет)
        amogus.setX(amogusBaseX);
        amogus.draw(gr);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == timer) {
            repaint();
            ++ticksFromStart;
        }
    }
}