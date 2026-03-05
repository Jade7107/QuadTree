import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageQuadTree {
    private QuadTree quadTree;
    private int width;
    private int height;

    public ImageQuadTree(String imagePath) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        width = originalImage.getWidth();
        height = originalImage.getHeight();
        
        BufferedImage binaryImage = convertToBinary(originalImage);
        initializeQuadTree(binaryImage);
    }

    private BufferedImage convertToBinary(BufferedImage original) {
        BufferedImage binary = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = binary.createGraphics();
        g2d.drawImage(original, 0, 0, null);
        g2d.dispose();
        return binary;
    }

    private void initializeQuadTree(BufferedImage binaryImage) {
        int maxDim = Math.max(width, height);
        int size = 0;
        while (QuadTree.power[size] < maxDim) size++;
        
        quadTree = new QuadTree(size);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = binaryImage.getRGB(x, y);
                int value = (pixel & 0xFF) > 127 ? 1 : 0;
                if (value == 1) {
                    quadTree.set(x, y, x, y, 1);
                }
            }
        }
    }

    public void saveProcessedImage(String outputPath, String operation) throws IOException {
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        
        switch (operation.toLowerCase()) {
            case "compress":
                quadTree.resize(quadTree.size() - 1);
                break;
            case "complement":
                quadTree.complement();
                break;
        }
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = quadTree.get(x, y);
                int rgb = value == 1 ? Color.WHITE.getRGB() : Color.BLACK.getRGB();
                output.setRGB(x, y, rgb);
            }
        }
        
        ImageIO.write(output, "PNG", new File(outputPath));
    }

    public void visualizeQuadTree(String outputPath) throws IOException {
        BufferedImage visualization = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = visualization.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        
        drawQuadTreeStructure(g2d, quadTree, 0, 0, width, height);
        g2d.dispose();
        
        ImageIO.write(visualization, "PNG", new File(outputPath));
    }

    private void drawQuadTreeStructure(Graphics2D g2d, QuadTree node, int x, int y, int w, int h) {
        if (node == null) return;
        
        g2d.setColor(Color.GRAY);
        g2d.drawRect(x, y, w, h);
        
        if (node.get(x, y) == 1) {
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fillRect(x, y, w, h);
        }
        
        int halfW = w / 2;
        int halfH = h / 2;
        if (halfW > 1 && halfH > 1) {
            drawQuadTreeStructure(g2d, node.getQuadrant(1), x, y, halfW, halfH);
            drawQuadTreeStructure(g2d, node.getQuadrant(2), x + halfW, y, halfW, halfH);
            drawQuadTreeStructure(g2d, node.getQuadrant(3), x, y + halfH, halfW, halfH);
            drawQuadTreeStructure(g2d, node.getQuadrant(4), x + halfW, y + halfH, halfW, halfH);
        }
    }

    public static void main(String[] args) {
        try {
            // Using your specific image path
            ImageQuadTree imageProcessor = new ImageQuadTree("D:\\QuadTreeProject\\images\\input\\alia.png");
            
            // Save processed versions to output folder
            imageProcessor.saveProcessedImage("D:\\QuadTreeProject\\output\\compressed.png", "compress");
            imageProcessor.saveProcessedImage("D:\\QuadTreeProject\\output\\inverted.png", "complement");
            
            // Save visualization
            imageProcessor.visualizeQuadTree("D:\\QuadTreeProject\\output\\quadtree_visualization.png");
            
            System.out.println("Processing complete! Check the output folder for results.");
            
        } catch (IOException e) {
            System.err.println("Error processing image: " + e.getMessage());
            e.printStackTrace();
        }
    }
}