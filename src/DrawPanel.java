import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel implements ActionListener {

    private final int TIMER_DELAY;
    private Timer timer;
    private int ticksFromStart = 0;

    private List<Amogus> amogusList;
    private TrafficLight trafficLight;
    private List<Cloud> clouds;
    private List<Integer> amogusBaseX;

    public DrawPanel(final int timerDelay) {
        this.TIMER_DELAY = timerDelay;
        timer = new Timer(timerDelay, this);
        timer.start();

        initializeObjects();
    }

    private void initializeObjects() {
        this.amogusList = new ArrayList<>();
        this.amogusBaseX = new ArrayList<>();
        this.clouds = new ArrayList<>();

        updatePositions(); // Инициализируем позиции

        createClouds();
    }

    private void updatePositions() {
        int panelHeight = getHeight();
        int panelWidth = getWidth();

        // Обновляем позицию светофора (привязываем к низу)
        if (trafficLight == null) {
            trafficLight = new TrafficLight(panelWidth - 150, panelHeight - 320, 80, 200);
        } else {
            trafficLight.setX(panelWidth - 150);
            trafficLight.setY(panelHeight - 420);
        }

        // Обновляем или создаем амогусов
        if (amogusList.isEmpty()) {
            // Создаем 12 амогусов с привязкой к низу окна
            for (int i = 0; i < 12; i++) {
                Color color = new Color(
                        (int)(Math.random() * 200 + 55),
                        (int)(Math.random() * 200 + 55),
                        (int)(Math.random() * 200 + 55)
                );

                int sizeVariation = (int)(Math.random() * 60 - 30);
                int width = 200 + sizeVariation;
                int height = 260 + sizeVariation;
                int y = panelHeight - 150 - (i * 35); // Привязываем к низу окна

                Amogus amogus = new Amogus(0, y, width, height, color);
                amogus.setTrafficLight(trafficLight);
                amogusList.add(amogus);
                amogusBaseX.add(50);
            }
        } else {
            // Обновляем позиции существующих амогусов
            for (int i = 0; i < amogusList.size(); i++) {
                int y = panelHeight - 150 - (i * 35);
                amogusList.get(i).setY(y);
            }
        }
    }

    private void createClouds() {
        clouds.clear();
        int cloudCount = getWidth() / 200;

        for (int i = 0; i < cloudCount; i++) {
            int x = (int)(Math.random() * getWidth());
            int y = (int)(Math.random() * 150 + 30);
            int width = (int)(Math.random() * 60 + 40);
            int height = (int)(Math.random() * 40 + 30);
            int speed = (int)(Math.random() * 3 + 1);
            clouds.add(new Cloud(x, y, width, height, speed));
        }
    }

    @Override
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);

        // Рисуем небо
        gr.setColor(new Color(135, 206, 235));
        gr.fillRect(0, 0, getWidth(), getHeight());

        // Рисуем землю (привязываем к низу окна)
        gr.setColor(new Color(34, 139, 34));
        int groundHeight = getHeight() / 4;
        int groundY = getHeight() - groundHeight;
        gr.fillRect(0, groundY, getWidth(), groundHeight);

        // Рисуем тень на земле для объема
        gr.setColor(new Color(20, 100, 20));
        for (int i = 0; i < 10; i++) {
            int alpha = 100 - i * 10;
            gr.setColor(new Color(20, 100, 20, alpha));
            gr.fillRect(0, groundY - i, getWidth(), 1);
        }

        // Обновляем и рисуем облака
        for (Cloud cloud : clouds) {
            cloud.update();
            if (cloud.getX() + cloud.getWidth() < 0) {
                cloud.setX(getWidth() + 50);
            }
            cloud.draw(gr);
        }

        // Обновляем позиции всех объектов
        updatePositions();

        // Обновляем светофор
        trafficLight.update(ticksFromStart);
        trafficLight.draw(gr);

        // Двигаем амогусов
        for (int i = 0; i < amogusList.size(); i++) {
            if (trafficLight.isGreenLight()) {
                int newX = amogusBaseX.get(i) + (int)(Math.random() * 3 + 1);
                if (newX > getWidth() - 250) {
                    newX = 50;
                }
                amogusBaseX.set(i, newX);
            }
            amogusList.get(i).setX(amogusBaseX.get(i));
        }

        // Рисуем всех амогусов
        for (Amogus amogus : amogusList) {
            amogus.draw(gr);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == timer) {
            repaint();
            ++ticksFromStart;
        }
    }

    public void onResize() {
        createClouds();
        updatePositions();
        repaint();
    }
}