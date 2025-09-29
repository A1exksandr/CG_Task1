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
    private List<Integer> amogusCurrentX;
    private List<Integer> amogusSpeeds;
    private List<Integer> amogusStartX;
    private List<Integer> amogusStartY;

    private int winnerIndex = -1; // Индекс победителя
    private boolean raceFinished = false; // Гонка завершена
    private int finishLine; // Линия финиша

    public DrawPanel(final int timerDelay) {
        this.TIMER_DELAY = timerDelay;
        timer = new Timer(timerDelay, this);
        timer.start();

        initializeObjects();
    }

    private void initializeObjects() {
        this.amogusList = new ArrayList<>();
        this.amogusCurrentX = new ArrayList<>();
        this.amogusSpeeds = new ArrayList<>();
        this.amogusStartX = new ArrayList<>();
        this.amogusStartY = new ArrayList<>();
        this.clouds = new ArrayList<>();

        createAmogusTeam();
        createClouds();

        this.trafficLight = new TrafficLight(getWidth() - 150, getHeight() - 320, 80, 200);
        this.finishLine = getWidth() - 200; // Финишная линия
    }

    private void createAmogusTeam() {
        amogusList.clear();
        amogusCurrentX.clear();
        amogusSpeeds.clear();
        amogusStartX.clear();
        amogusStartY.clear();

        int amogusCount = 12;
        int panelHeight = getHeight();

        // Поднимаем амогусов выше - располагаем в верхней части экрана
        int columns = 4;
        int baseY = 100; // Высокая позиция вместо нижней части

        for (int i = 0; i < amogusCount; i++) {
            Color color = new Color(
                    (int)(Math.random() * 200 + 55),
                    (int)(Math.random() * 200 + 55),
                    (int)(Math.random() * 200 + 55)
            );

            int sizeVariation = (int)(Math.random() * 40 - 20);
            int width = 160 + sizeVariation;
            int height = 220 + sizeVariation;

            int col = i % columns;
            int row = i / columns;
            int startX = 50 + col * 100;
            int startY = baseY + row * 80; // Располагаем выше

            Amogus amogus = new Amogus(startX, startY, width, height, color);
            amogus.setTrafficLight(trafficLight);

            amogusList.add(amogus);
            amogusStartX.add(startX);
            amogusStartY.add(startY);
            amogusCurrentX.add(startX);
            amogusSpeeds.add((int)(Math.random() * 2 + 2));
        }

        // Сбрасываем состояние гонки
        winnerIndex = -1;
        raceFinished = false;
    }

    private void updateAmogusPositions() {
        int panelHeight = getHeight();
        int panelWidth = getWidth();

        // Обновляем позицию светофора
        if (trafficLight != null) {
            trafficLight.setX(panelWidth - 150);
            trafficLight.setY(panelHeight - 420);
        }

        // Обновляем финишную линию
        finishLine = panelWidth - 200;

        // Обновляем стартовые позиции амогусов
        int columns = 4;
        int baseY = 500;
        for (int i = 0; i < amogusList.size(); i++) {
            int col = i % columns;
            int row = i / columns;
            int newStartX = 50 + col * 100;
            int newStartY = baseY + row * 80;

            amogusStartX.set(i, newStartX);
            amogusStartY.set(i, newStartY);

            if (amogusCurrentX.get(i).equals(amogusStartX.get(i))) {
                amogusCurrentX.set(i, newStartX);
                amogusList.get(i).setY(newStartY);
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

        // Рисуем землю (теперь только внизу, амогусы выше)
        gr.setColor(new Color(34, 139, 34));
        int groundHeight = getHeight() / 4;
        int groundY = getHeight() - groundHeight;
        gr.fillRect(0, groundY, getWidth(), groundHeight);

        // Тень для объема земли
        for (int i = 0; i < 10; i++) {
            int alpha = 100 - i * 10;
            gr.setColor(new Color(20, 100, 20, alpha));
            gr.fillRect(0, groundY - i, getWidth(), 1);
        }

        // Обновляем позиции объектов
        updateAmogusPositions();

        // Обновляем и рисуем облака
        for (Cloud cloud : clouds) {
            cloud.update();
            if (cloud.getX() + cloud.getWidth() < 0) {
                cloud.setX(getWidth() + 50);
            }
            cloud.draw(gr);
        }

        // Обновляем светофор
        trafficLight.update(ticksFromStart);
        trafficLight.draw(gr);

        // Двигаем амогусов и проверяем победителя
        if (trafficLight.isGreenLight() && !raceFinished) {
            for (int i = 0; i < amogusList.size(); i++) {
                int newX = amogusCurrentX.get(i) + amogusSpeeds.get(i);

                // Проверяем, достиг ли амогус финиша
                if (newX > finishLine && winnerIndex == -1) {
                    winnerIndex = i; // Первый амогус, достигший финиша
                    raceFinished = true;
                }

                amogusCurrentX.set(i, newX);
            }
        }

        // Устанавливаем позиции амогусам
        for (int i = 0; i < amogusList.size(); i++) {
            amogusList.get(i).setX(amogusCurrentX.get(i));
            amogusList.get(i).setY(amogusStartY.get(i));
        }

        // Рисуем всех амогусов
        for (Amogus amogus : amogusList) {
            amogus.draw(gr);
        }

        // Отображаем информацию о гонке
        gr.setColor(Color.BLACK);
        gr.setFont(new Font("Arial", Font.BOLD, 16));

        if (raceFinished && winnerIndex != -1) {
            // Показываем победителя
            Color winnerColor = amogusList.get(winnerIndex).getColor();
            gr.setColor(winnerColor);
            gr.drawString("🏆 ПОБЕДИТЕЛЬ: Амогус " + (winnerIndex + 1) + "! 🏆",
                    getWidth() / 2 - 150, 50);

            // Подсвечиваем победителя
            Amogus winner = amogusList.get(winnerIndex);
            gr.setColor(new Color(255, 215, 0, 100)); // Золотая подсветка
            gr.fillOval(winner.getX() - 10, winner.getY() - 10,
                    winner.getWidth() + 20, winner.getHeight() + 50);
        } else if (trafficLight.isGreenLight()) {
            gr.setColor(Color.GREEN);
            gr.drawString("ГОНКА ИДЕТ! ЗЕЛЕНЫЙ СВЕТ!", getWidth() / 2 - 100, 50);
        } else {
            gr.setColor(Color.RED);
            gr.drawString("ЖДЕМ ЗЕЛЕНОГО СВЕТА...", getWidth() / 2 - 100, 50);
        }

        // Статистика
        gr.setColor(Color.BLACK);
        gr.setFont(new Font("Arial", Font.PLAIN, 12));
        gr.drawString("Амогусов в гонке: " + amogusList.size(), 10, 20);
        if (winnerIndex != -1) {
            gr.drawString("Победитель определен! Нажмите F5 для новой гонки", 10, 35);
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
        updateAmogusPositions();
        repaint();
    }

    // Метод для сброса гонки (можно вызвать по нажатию клавиши)
    public void resetRace() {
        createAmogusTeam();
        repaint();
    }
}