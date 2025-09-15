import java.awt.*;

public class Cloud {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private int speed;

    public Cloud(final int x, final int y, final int width, final int height, final int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = new Color(173, 216, 230); // Голубой цвет
        this.speed = speed;
    }

    public void update() {
        x -= speed; // Двигаем облако слева направо
        // Если облако ушло за левый край, возвращаем его справа
        if (x + width < 0) {
            x = 800; // Возвращаем за правый край экрана
            y = (int)(Math.random() * 150 + 50); // Случайная высота
        }
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
        g.setColor(color);

        // Рисуем пушистое облако из нескольких кругов
        g.fillOval(x, y, width, height);
        g.fillOval(x + width/3, y - height/4, width, height);
        g.fillOval(x + width/2, y + height/4, width, height);
        g.fillOval(x - width/4, y + height/6, width/2, height/2);
    }
}