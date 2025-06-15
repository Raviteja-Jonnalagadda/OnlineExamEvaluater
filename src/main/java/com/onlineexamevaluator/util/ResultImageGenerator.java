package com.onlineexamevaluator.util;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ResultImageGenerator {

	public static void generateSingleResultImage(Map<String, Object> result, HttpServletResponse response) {
	    try {
	        int width = 800, height = 600;
	        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g = image.createGraphics();
	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Add this

	        // Background
	        g.setColor(new Color(255, 255, 240)); // Light cream
	        g.fillRect(0, 0, width, height);

	        // Border
	        g.setColor(new Color(34, 139, 34));
	        g.setStroke(new BasicStroke(6));
	        g.drawRect(10, 10, width - 20, height - 20);

	        // Draw Logo Image (top-left)
	        BufferedImage logo = ImageIO.read(new File("C:/Users/RAVITEJA/Downloads/exam_portal_main_logo.png"));
	        int logoX = 30, logoY = 30, logoW = 100, logoH = 100;
	        g.drawImage(logo, logoX, logoY, logoW, logoH, null);
	        g.setColor(Color.BLACK);
	        //g.drawRect(logoX - 2, logoY - 2, logoW + 4, logoH + 4);

	        // Title
	        g.setFont(new Font("Georgia", Font.BOLD, 30));
	        String title = "Online Exam Certificate";
	        int titleWidth = g.getFontMetrics().stringWidth(title);
	        g.setColor(Color.BLACK);
	        g.drawString(title, (width - titleWidth) / 2, 80);

	        // Subtitle
	        g.setFont(new Font("Serif", Font.PLAIN, 18));
	        g.drawString("This is to certify that", width / 2 - 100, 150);

	        // Student Name
	        String studentId = String.valueOf(result.get("student_id"));
	        g.setFont(new Font("Serif", Font.BOLD, 26));
	        int studentWidth = g.getFontMetrics().stringWidth(studentId);
	        g.drawString(studentId, (width - studentWidth) / 2, 190);

	        // Description
	        g.setFont(new Font("Serif", Font.PLAIN, 18));
	        g.drawString("has successfully completed the examination", width / 2 - 180, 230);

	        // Details Block
	        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
	        int infoY = 280;
	        g.drawString("Result ID    : " + result.get("result_id"), 100, infoY);
	        g.drawString("Exam ID      : " + result.get("exam_id"), 100, infoY + 30);
	        g.drawString("Marks        : " + result.get("obtained_marks"), 100, infoY + 60);

	        // Result status
	        String status = "Y".equals(result.get("result_status")) ? "PASS" : "FAIL";
	        g.setColor("PASS".equals(status) ? new Color(0, 153, 0) : Color.RED);
	        g.drawString("Result Status: " + status, 100, infoY + 90);
	        g.setColor(Color.BLACK);
	        g.drawString("Date         : " + result.get("result_date"), 100, infoY + 120);

	        // Signature Box and Signature Image
	        int signX = width - 240, signY = height - 140, signW = 120, signH = 40;

	        BufferedImage signImg = ImageIO.read(new File("C:/Users/RAVITEJA/Downloads/20250507_181228.jpg"));
	        g.drawImage(signImg, signX, signY, signW, signH, null);
	        g.setColor(Color.BLACK);
//	        //g.drawRect(signX - 2, signY - 2, signW + 4, signH + 4);

	        // Signature Label
	        g.setFont(new Font("SansSerif", Font.ITALIC, 16));
	        g.drawString("Authorized Signature", signX - 10, signY + signH + 25);

	        // Footer: Generated On
	        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
	        String now = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());
	        g.drawString("Generated On: " + now, width - 250, height - 20);

	        g.dispose();

	        response.setContentType("image/jpeg");
	        response.setHeader("Content-Disposition", "attachment; filename=result_" + result.get("result_id") + ".jpg");

	        OutputStream os = response.getOutputStream();
	        ImageIO.write(image, "jpg", os);
	        os.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static void generateFullResultImage(List<Map<String, Object>> results, HttpServletResponse response) {
	    try {
	        int rowHeight = 80;
	        int boxPadding = 20;
	        int height = 200 + results.size() * (rowHeight + boxPadding);
	        int width = 900;

	        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g = image.createGraphics();
	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Add this

	        // White background
	        g.setColor(Color.WHITE);
	        g.fillRect(0, 0, width, height);

	        // Anti-aliasing
	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	        // Draw logo
	        BufferedImage logo = ImageIO.read(new File("C:/Users/RAVITEJA/Downloads/exam_portal_main_logo.png"));
	        g.drawImage(logo, 30, 20, 80, 80, null); // top-left corner

	        // Title
	        g.setColor(Color.BLACK);
	        g.setFont(new Font("Arial", Font.BOLD, 28));
	        g.drawString("Online Exam Portal", 140, 60);

	        // Timestamp
	        g.setFont(new Font("Arial", Font.PLAIN, 12));
	        String now = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());
	        g.drawString("Generated On: " + now, width - 240, 30);

	        // Draw each result in a box
	        int y = 120;
	        for (Map<String, Object> row : results) {
	            // Box border
	            g.setColor(Color.LIGHT_GRAY);
	            g.fillRoundRect(40, y, width - 80, rowHeight, 15, 15);
	            g.setColor(Color.BLACK);
	            g.drawRoundRect(40, y, width - 80, rowHeight, 15, 15);

	            // Result Text
	            g.setFont(new Font("Arial", Font.PLAIN, 16));
	            String statusText = "Y".equals(row.get("result_status")) ? "PASS" : "FAIL";
	            String line = String.format("Result ID: %s, Exam ID: %s, Marks: %s, Status: %s, Date: %s",
	                    row.get("result_id"), row.get("exam_id"), row.get("obtained_marks"),
	                    statusText, row.get("result_date"));

	            g.drawString(line, 60, y + 45); // center line in the box

	            y += rowHeight + boxPadding;
	        }

	        // Draw signature
	        BufferedImage sign = ImageIO.read(new File("C:/Users/RAVITEJA/Downloads/20250507_181228.jpg"));
	        int signW = 150, signH = 50;
	        int signX = width - signW - 50;
	        int signY = height - signH - 50;
	        g.drawImage(sign, signX, signY, signW, signH, null);

	        g.setFont(new Font("Arial", Font.ITALIC, 12));
	        g.drawString("Authorized Signature", signX, signY + signH + 15);

	        g.dispose();

	        // Set response
	        response.setContentType("image/jpeg");
	        response.setHeader("Content-Disposition", "attachment; filename=all_results.jpg");

	        OutputStream os = response.getOutputStream();
	        ImageIO.write(image, "jpg", os);
	        os.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

    @SuppressWarnings("unused")
	private static void drawCommonHeader(Graphics2D g, int width) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, 1000);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Online Exam Portal", (width / 2) - 100, 40);

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        String now = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());
        g.drawString("Generated On: " + now, width - 200, 20);
    }
}
