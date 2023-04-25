package telran.employees.apllication.controller.graphics;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Semaphore;

import javax.swing.*;

import telran.view.InputOutput;

public class Frame extends JFrame implements InputOutput, ActionListener {
	private static final long serialVersionUID = 1L;
	JTextArea area;
	JTextField field;
	JButton button;
	JScrollPane scrollableTextArea;
	Image image;

	boolean flPrompt = false;

	public Frame() {
		this.setTitle("Company operations menu");
		image = new ImageIcon("backround.jpg").getImage();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new FlowLayout());

		addScrollableArea();
		addField();
		addButton();

		this.pack();
		this.setLocationRelativeTo(null);

	}

	private void addButton() {
		button = new JButton("Submit");
		button.addActionListener(this);
		this.add(button);
	}

	private void addField() {
		field = new JTextField();
		field.setPreferredSize(new Dimension(250, 30));
		field.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && flPrompt) {
					field.setText("");
					flPrompt = false;
				}
			}
		});
		field.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					button.doClick();
				} else if (flPrompt) {
					field.setText("");
					flPrompt = false;
				}
			}
		});
		this.add(field);
	}

	private void addScrollableArea() {
		area = new JTextArea();
		scrollableTextArea = new JScrollPane(area);
		scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollableTextArea.setPreferredSize(new Dimension(400, 300));
		scrollableTextArea.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				area.select(area.getHeight() + 1000000, 0);
			}
		});
		this.getContentPane().add(scrollableTextArea);
	}

	private Semaphore semaphore = new Semaphore(0);

	@Override
	public String readString(String prompt) {
		field.setText(prompt);
		flPrompt = true;
		while (semaphore.availablePermits() == 0) {

		}
		semaphore.drainPermits();
		return field.getText();
	}

	@Override
	public void writeString(Object obj) {
		area.append(obj.toString());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			semaphore.release();
		}
	}

}
