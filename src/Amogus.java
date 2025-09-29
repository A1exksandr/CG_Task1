import java.awt.*;

public class Amogus {

    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private TrafficLight trafficLight;
    private int lastX; // Запоминаем последнюю позицию

    public Amogus(final int x, final int y, final int width, final int height, final Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.lastX = x;
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public void setX(int newX) {
        // Запоминаем текущую позицию
        this.lastX = this.x;

        // Двигаемся только на зеленый свет
        if (trafficLight == null || trafficLight.isGreenLight()) {
            this.x = newX;
        }
        // На красный/желтый остаемся на месте (lastX сохраняет предыдущее значение)
    }

    public int getX() {
        return x;
    }
    public Color getColor() {
        return color;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public void setColor(Color color) {
        this.color = color;
    }

    void draw(final Graphics gr) {
        Graphics2D g = (Graphics2D) gr;

        // Всегда используем оригинальный цвет амогуса
        g.setColor(this.color);

        // bag
        g.fillOval(this.x, (int)(this.y + 0.25 * this.height), (int)(0.33 * this.width), (int)(0.5 * this.height));
        g.setColor(Color.BLACK);
        g.drawOval(this.x, (int)(this.y + 0.25 * this.height), (int)(0.33 * this.width), (int)(0.5 * this.height));

        // head
        g.setColor(this.color);
        g.fillOval((int)(this.x + 0.2 * this.width), this.y, (int)(0.75 * this.width), (int)(0.5 * height));
        g.setColor(Color.BLACK);
        g.drawOval((int)(this.x + 0.2 * this.width), this.y, (int)(0.75 * this.width), (int)(0.5 * height));

        // legs
        g.setColor(this.color);
        g.fillRect((int)(this.x + 0.2 * this.width), (int)(this.y + 0.5 * this.height),
                (int)(0.3 * this.width), (int)(0.5 * this.height));
        g.fillRect((int)(this.x + 0.65 * this.width), (int)(this.y + 0.5 * this.height),
                (int)(0.3 * this.width), (int)(0.5 * this.height));
        g.setColor(Color.BLACK);
        g.drawRect((int)(this.x + 0.2 * this.width), (int)(this.y + 0.5 * this.height),
                (int)(0.3 * this.width), (int)(0.5 * this.height));
        g.drawRect((int)(this.x + 0.65 * this.width), (int)(this.y + 0.5 * this.height),
                (int)(0.3 * this.width), (int)(0.5 * this.height));

        // body
        g.setColor(this.color);
        g.fillRect((int)(this.x + 0.2 * this.width), (int)(this.y + 0.25 * this.height),
                (int)(0.75 * this.width), (int)(0.5 * this.height));
        g.setColor(Color.BLACK);
        g.drawLine((int)(this.x + 0.2 * this.width), (int)(this.y + 0.25 * this.height),
                (int)(this.x + 0.2 * this.width), (int)(this.y + 0.75 * this.height));
        g.drawLine((int)(this.x + 0.95 * this.width), (int)(this.y + 0.25 * this.height),
                (int)(this.x + 0.95 * this.width), (int)(this.y + 0.75 * this.height));
        g.drawLine((int)(this.x + 0.45 * this.width), (int)(this.y + 0.75 * this.height),
                (int)(this.x + 0.7 * this.width),(int)(this.y + 0.75 * this.height));

        // eyes
        g.setColor(new Color(135,206,250));
        g.fillOval((int)(this.x + 0.5 * this.width), (int)(this.y + 0.16 * this.height),
                (int)(0.55 * this.width), (int)(0.25 * this.height));
        g.setColor(Color.BLACK);
        g.drawOval((int)(this.x + 0.5 * this.width), (int)(this.y + 0.16 * this.height),
                (int)(0.55 * this.width), (int)(0.25 * this.height));

        // Убрали красную точку - теперь ее нет совсем
    }

}