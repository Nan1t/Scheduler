package edu.zieit.scheduler.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class ConversionUtil {

    private ConversionUtil() { }

    public static byte[] renderHtml(String html) {
        JEditorPane jep = new JEditorPane();
        jep.setEditable(false);
        jep.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        jep.setFont(new Font(Font.SANS_SERIF, 0, 18));
        jep.setContentType("text/html");
        jep.setText(html.replaceAll("<head>(.*?)</head>", " "));

        jep.setSize(1024, Integer.MAX_VALUE);
        Dimension size = jep.getPreferredScrollableViewportSize();

        try {
            BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            Container c = new Container();
            SwingUtilities.paintComponent(g, jep, c, 0, 0, size.width, size.height);
            g.dispose();

            return toByteArray(image, "png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static byte[] toByteArray(BufferedImage bi, String format) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(bi, format, out);
        return out.toByteArray();
    }

}
