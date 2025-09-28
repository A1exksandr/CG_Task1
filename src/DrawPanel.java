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

    // Список для множества амогусов
    private List<Amogus> amogusList;
    private TrafficLight trafficLight;
    private List<Cloud> clouds;
    private List<Integer> amogusBaseX; // Отдельная базовая позиция для каждого амогуса

    public DrawPanel(final int timerDelay) {
        this.TIMER_DELAY = timerDelay;
        timer = new Timer(timerDelay, this);
        timer.start();

        initializeObjects();
    }

    private void initializeObjects() {
        // Создаем светофор в правой части экрана
        this.trafficLight = new TrafficLight(getWidth() - 150, 100, 80, 200);

        // Инициализируем списки
        this.amogusList = new ArrayList<>();
        this.amogusBaseX = new ArrayList<>();
        this.clouds = new ArrayList<>();

        // Создаем 12 амогусов с разными свойствами
        for (int i = 0; i < 12; i++) {
            // Разные цвета
            Color color = new Color(
                    (int)(Math.random() * 200 + 55),
                    (int)(Math.random() * 200 + 55),
                    (int)(Math.random() * 200 + 55)
            );

            // Разные размеры и позиции по Y
            int sizeVariation = (int)(Math.random() * 60 - 30); // -30 до +30
            int width = 200 + sizeVariation;
            int height = 260 + sizeVariation;
            int y = 150 + i * 35; // Распределяем по вертикали

            Amogus amogus = new Amogus(0, y, width, height, color);
            amogus.setTrafficLight(trafficLight);
            amogusList.add(amogus);
            amogusBaseX.add(50); // Начальная позиция для каждого
        }

        // Создаем облака с учетом размера окна
        createClouds();
    }

    private void createClouds() {
        clouds.clear();
        int cloudCount = getWidth() / 200; // Количество облаков зависит от ширины окна

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

        // Рисуем землю (1/4 высоты окна)
        gr.setColor(new Color(34, 139, 34));
        int groundHeight = getHeight() / 4;
        gr.fillRect(0, getHeight() - groundHeight, getWidth(), groundHeight);

        // Обновляем и рисуем облака
        for (Cloud cloud : clouds) {
            cloud.update();
            // Если облако ушло за левый край, перемещаем его вправо за пределы экрана
            if (cloud.getX() + cloud.getWidth() < 0) {
                cloud.setX(getWidth() + 50);
            }
            cloud.draw(gr);
        }

        // Обновляем позицию светофора при изменении размера окна
        trafficLight.setX(getWidth() - 150);
        trafficLight.setY(100);

        // Обновляем и рисуем светофор
        trafficLight.update(ticksFromStart);
        trafficLight.draw(gr);

        // Двигаем амогусов только на зеленый свет с разной скоростью
        for (int i = 0; i < amogusList.size(); i++) {
            if (trafficLight.isGreenLight()) {
                int newX = amogusBaseX.get(i) + (int)(Math.random() * 3 + 1); // Разная скорость
                // Если амогус дошел до светофора, возвращаем в начало
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

        // Отладочная информация (можно убрать)
        gr.setColor(Color.BLACK);
        gr.drawString("Амогусов: " + amogusList.size() + " | Размер окна: " +
                getWidth() + "x" + getHeight(), 10, 20);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == timer) {
            repaint();
            ++ticksFromStart;
        }
    }

    // Метод для обработки изменения размера окна
    public void onResize() {
        createClouds(); // Пересоздаем облака при изменении размера
        // Светофор автоматически переместится в paintComponent
    }
}