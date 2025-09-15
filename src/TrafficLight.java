import java.awt.*;

public class TrafficLight {
    private int x;
    private int y;
    private int width;
    private int height;
    private int currentState; // 0 - красный, 1 - желтый, 2 - зеленый, 3 - мигающий зеленый
    private int ticksFromStart;
    private int[] durations; // длительности в тиках: красный, желтый, зеленый, мигающий зеленый, желтый2

    public TrafficLight(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currentState = 0;
        this.ticksFromStart = 0;
        // Длительности в тиках (100ms = 1 тик):
        // красный 30сек=300тиков, желтый 3сек=30тиков, зеленый 17сек=170тиков,
        // мигающий зеленый 3сек=30тиков, желтый2 3сек=30тиков
        this.durations = new int[]{30, 30, 17, 30, 30};
    }

    public void update(int ticks) {
        this.ticksFromStart = ticks;
        int totalCycleTime = getTotalCycleTime();
        int cycleTime = ticksFromStart % totalCycleTime;

        if (cycleTime < durations[0]) {
            currentState = 0; // красный
        } else if (cycleTime < durations[0] + durations[1]) {
            currentState = 1; // желтый
        } else if (cycleTime < durations[0] + durations[1] + durations[2]) {
            currentState = 2; // зеленый
        } else if (cycleTime < durations[0] + durations[1] + durations[2] + durations[3]) {
            currentState = 3; // мигающий зеленый
        } else {
            currentState = 4; // желтый2
        }
    }

    private int getTotalCycleTime() {
        int total = 0;
        for (int duration : durations) {
            total += duration;
        }
        return total;
    }

    public boolean isGreenLight() {
        return currentState == 2 || currentState == 3;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    void draw(final Graphics gr) {
        Graphics2D g = (Graphics2D) gr;

        // Рисуем корпус светофора
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        // Вычисляем размер и положение каждого сигнала
        int signalHeight = height / 3;
        int signalWidth = (int)(width * 0.8);
        int signalX = x + (width - signalWidth) / 2;

        // Красный сигнал
        int redY = y;
        g.setColor(currentState == 0 ? Color.RED : new Color(100, 0, 0));
        g.fillOval(signalX, redY, signalWidth, signalHeight);
        g.setColor(Color.BLACK);
        g.drawOval(signalX, redY, signalWidth, signalHeight);

        // Желтый сигнал
        int yellowY = y + signalHeight;
        boolean yellowActive = currentState == 1 || currentState == 4;
        g.setColor(yellowActive ? Color.YELLOW : new Color(100, 100, 0));
        g.fillOval(signalX, yellowY, signalWidth, signalHeight);
        g.setColor(Color.BLACK);
        g.drawOval(signalX, yellowY, signalWidth, signalHeight);

        // Зеленый сигнал
        int greenY = y + 2 * signalHeight;
        boolean greenActive = currentState == 2;
        boolean greenBlinking = currentState == 3;

        // Мигающий зеленый: видимый только на четных тиках
        if (greenBlinking) {
            if ((ticksFromStart / 5) % 2 == 0) { // мигание каждые 5 тиков (0.5 секунды)
                g.setColor(Color.GREEN);
            } else {
                g.setColor(new Color(0, 100, 0));
            }
        } else {
            g.setColor(greenActive ? Color.GREEN : new Color(0, 100, 0));
        }

        g.fillOval(signalX, greenY, signalWidth, signalHeight);
        g.setColor(Color.BLACK);
        g.drawOval(signalX, greenY, signalWidth, signalHeight);

        // Дополнительно: рисуем опору светофора
        g.setColor(Color.GRAY);
        int poleWidth = width / 4;
        int poleX = x + (width - poleWidth) / 2;
        g.fillRect(poleX, y + height, poleWidth, 100);

        // Отладочная информация (можно убрать)
        //g.setColor(Color.BLACK);
        //g.drawString("Состояние: " + getStateName(), x, y + height + 120);
    }

    public String getStateName() {
        switch(currentState) {
            case 0: return "Красный";
            case 1: return "Желтый";
            case 2: return "Зеленый";
            case 3: return "Мигающий зеленый";
            case 4: return "Желтый";
            default: return "Неизвестно";
        }
    }
}