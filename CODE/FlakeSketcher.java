package my.fractal;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FlakeSketcher  {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("n-Flake Sketcher");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PolygonPanel polygonPanel = new PolygonPanel();
        f.add(polygonPanel, BorderLayout.CENTER);
        f.add(new ButtonPanel(polygonPanel), BorderLayout.EAST);
        f.pack();
        f.setVisible(true);
    }

}

class PolygonPanel extends JPanel implements ActionListener {
	
	// Placeholder for fractal creation
	JButton multiply;
	
	// Used in drawing
	ArrayList<polygon> polygonList = new ArrayList<polygon>();
	
	// To be made dynamic later.
	int sides = 6;
	int radius = 200;
	int[] center = new int[] {400,300};
	
	int fractalIterations = 0;
	
	
	public PolygonPanel() {
		setBorder(BorderFactory.createLineBorder(Color.black));
		initComponents();
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(800,600);
	}
	
	private void formFractals() {
		ArrayList<polygon> tempList = new ArrayList<polygon>();
		double angleDiff = 2*Math.PI/sides;
		
		// Calculate the scale factor for radius (can be geometrically justified). 
		double scaleFactor = 1/(1+1/(Math.sin(Math.PI/sides)));
		
		
		for (polygon poly: polygonList) {
			for (int i = 0; i < sides; i++) {				
				polygon newGon;
				newGon = new polygon(sides,poly.getRadius()*scaleFactor, new int[] {poly.getCenter()[0]+(int)Math.round(poly.getRadius()*Math.sin(i*angleDiff)*(1-scaleFactor)),poly.getCenter()[1]+(int)Math.round(poly.getRadius()*Math.cos(i*angleDiff)*(1-scaleFactor))} );
				tempList.add(newGon);
			}
			// Central nGon
			/*polygon newGon;
			newGon = new polygon(sides,poly.getRadius()*scaleFactor, new int[] {poly.getCenter()[0],poly.getCenter()[1]} );
			tempList.add(newGon);
			 */
		}
		polygonList = tempList;
			
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	
		
		//Draw Text
		g.drawString("Fractal pattern inc!", 10, 20);
		
		for (polygon poly : polygonList)
			poly.paintPolygon(g);
	}
	
	private void initComponents(){
		polygon ngon = new polygon(sides, radius, center);
		polygonList.add(ngon);
		
		multiply = new JButton("Multiply");
		multiply.setBackground(Color.RED);
		multiply.setActionCommand("multiply");
		
		multiply.addActionListener(this);
		
		this.add(multiply);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		// Only add more if we haven't hit cap, due to computation.
	    if ("multiply".equals(e.getActionCommand()) && fractalIterations < 6) {
	    	formFractals();
	    	fractalIterations++;
	    }
}

class polygon {
	private int sides;
	private double radius;
	private int[] center;
	private Polygon poly;
	
	
	
	public int getSides() {
		return sides;
	}
	public void setSides(int sides) {
		this.sides = sides;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public int[] getCenter() {
		return center;
	}
	public void setCenter(int[] center) {
		this.center = center;
	}
	public void formPolygon() {
		double angleDiff = 2*Math.PI/sides;
		
		int[] xPoly = new int[sides];
		int[] yPoly = new int[sides];
		
		for (int i=0; i<sides; i++){
			xPoly[i] = (int)Math.round(radius*Math.sin(i*angleDiff) + center[0]);
			yPoly[i] = (int)Math.round(radius*Math.cos(i*angleDiff) + center[1]);			
		}
		
		poly = new Polygon(xPoly,yPoly,xPoly.length);
				
	}
	
	public void paintPolygon(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillPolygon(poly);
        g.setColor(Color.RED);
        g.drawPolygon(poly);
		
	}
	
	public polygon(int sides, double radius, int[] center) {
		this.sides = sides;
		this.radius = radius;
		this.center = center;
		formPolygon();
	}
}
}

class ButtonPanel extends JPanel  {
	
	PolygonPanel polygonPanel;
	JButton b1;
	JButton b2;
	JButton b3;
	JButton b4;
	JSlider sidesSlider;
	
	public ButtonPanel(PolygonPanel polygonPanel) {
		
		this.polygonPanel = polygonPanel;
		
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new GridLayout(5,1,0,10));
		
		initComponents();
		
	}
	
	private void initComponents() {
		
		b1 = new JButton("button with larger text");
		b1.setBackground(Color.RED);
		
		b2 = new JButton("b2");
		b2.setBackground(Color.RED);
		
		b3 = new JButton("b3");
		b3.setBackground(Color.RED);
		
		b4 = new JButton("b4");
		b4.setBackground(Color.RED);
		
		sidesSlider = new JSlider(JSlider.HORIZONTAL,3,15,6);
		
		
		
		this.add(b1);
		this.add(b2);
		this.add(b3);
		this.add(b4);
		this.add(sidesSlider);
	}
}
