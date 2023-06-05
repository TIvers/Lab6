import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MovingShapes extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;
    private Ellipse2D circle;
    private Rectangle2D rectangle;
    private Path2D polygon;
    private Thread circleThread, rectangleThread, polygonThread;
    private double circleDx, circleDy, rectangleDx, rectangleDy, polygonDx, polygonDy;
    private int width, height;
    private Random random = new Random();
    public MovingShapes(int width, int height) {
        this.setBackground(Color.WHITE);
        this.width = width;
        this.height = height;

        
        circle = new Ellipse2D.Double(15, 15, 50, 50);
        circleThread = new Thread(this);
        circleThread.start();

        
        rectangle = new Rectangle2D.Double(90, 90, 80, 40);
        rectangleThread = new Thread(this);
        rectangleThread.start();

        
        polygon = new Path2D.Double();
        polygon.moveTo(150, 150);
        polygon.lineTo(200, 200);
        polygon.lineTo(250, 150);
        polygon.lineTo(150, 150);
        polygonThread = new Thread(this);
        polygonThread.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

       
        g2d.setColor(Color.BLACK);
        g2d.fill(circle);

       
        g2d.setColor(Color.YELLOW);
        g2d.fill(rectangle);

       
        g2d.setColor(Color.GREEN);
        g2d.fill(polygon);
    }

    @Override
    public void run() {
        while (true) {
            int sleepTime = 20 + random.nextInt(50);

            if (Thread.currentThread() == circleThread) {
                circleDx = random.nextDouble() * 5;
                circleDy = random.nextDouble() * 5;
                moveShape(circle, circleDx, circleDy);
            } else if (Thread.currentThread() == rectangleThread) {
                rectangleDx = random.nextDouble() * 5;
                rectangleDy = random.nextDouble() * 5;
                moveShape(rectangle, rectangleDx, rectangleDy);
            } else if (Thread.currentThread() == polygonThread) {
                polygonDx = random.nextDouble() * 5;
                polygonDy = random.nextDouble() * 5;
                moveShape(polygon, polygonDx, polygonDy);
            }

            repaint();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveShape(Object shape, double dx, double dy) {
        if (shape instanceof Ellipse2D) {
            Ellipse2D ellipse = (Ellipse2D) shape;
            if (ellipse.getX() + dx + ellipse.getWidth() >= width || ellipse.getX() + dx <= 0) {
                circleDx *= -1;
            }
            if (ellipse.getY() + dy + ellipse.getHeight() >= height || ellipse.getY() + dy <= 0) {
                circleDy *= -1;
            }
            ellipse.setFrame(ellipse.getX() + dx, ellipse.getY() + dy, ellipse.getWidth(), ellipse.getHeight());
        } else if (shape instanceof Rectangle2D) {
            Rectangle2D rect = (Rectangle2D) shape;
            if (rect.getX() + dx + rect.getWidth() >= width || rect.getX() + dx <= 0) {
                rectangleDx *= -1;
            }
            if (rect.getY() + dy + rect.getHeight() >= height || rect.getY() + dy <= 0) {
                rectangleDy *= -1;
            }
            rect.setFrame(rect.getX() + dx, rect.getY() + dy, rect.getWidth(), rect.getHeight());
        } else if (shape instanceof Path2D) {
            Path2D path = (Path2D) shape;
            if (path.getBounds().getMaxX() + dx >= width || path.getBounds().getMinX() + dx <= 0) {
                polygonDx *= -1;
            }
            if (path.getBounds().getMaxY() + dy >= height || path.getBounds().getMinY() + dy <= 0) {
                polygonDy *= -1;
            }
            path.transform(java.awt.geom.AffineTransform.getTranslateInstance(dx, dy));
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Движение фигур");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MovingShapes panel = new MovingShapes(600, 600);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}