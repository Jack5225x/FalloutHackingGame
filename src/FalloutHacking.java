

/**
 * @author Jfeather
 **/

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FalloutHacking extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel header, attempts, background;
	private JLabel[] wordsLabels, result;
	private JButton[] boxes;
	public int numWords, wordLength;
	public String correct;
	public String[] sequence;

	private final int MAX_WORDS = 14;
	private final int MIN_WORDS = 6;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FalloutHacking frame = new FalloutHacking();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FalloutHacking() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(750, 450));
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);
		setTitle("RobCo Terminal");
		setResizable(false);
		pack();
		
		header = new JLabel("<html> <font size="+5+" color='green'>ROBCO INDUSTRIES (TM) TERMALINK PROTOCOL <br>ENTER PASSWORD NOW");
		header.setBounds(10, 30, 500, 50);
		contentPane.add(header);
		
		attempts = new JLabel("<html> <font size="+4+" color='green'> 4 ATTEMPTS LEFT: █   █   █   █");
		attempts.setBounds(10, 100, 250, 25);
		contentPane.add(attempts);
		
		Random rand = new Random();
	
		numWords = rand.nextInt(MAX_WORDS - MIN_WORDS) + MIN_WORDS;
		wordsLabels = new JLabel[numWords];
		boxes = new JButton[numWords];
		
		wordLength = rand.nextInt(8) + 4;
		int row = 145, column = 60;
		int length = wordLength * 9 + 80;
		String[] words = readWords(numWords, wordLength);
		
		sequence = genSeq(numWords);
		correct = words[rand.nextInt(numWords - 1)];
		
		/*
		// Make sure there is at least one other word with >3 similar letters
		boolean done = false;
		while (!done) {
			for (String k: words) {
				if (likeness(k, correct) > 3 && likeness(k, correct) < wordLength - 1)
					done = true;
			}
			words = readWords(numWords, wordLength);
			correct = words[rand.nextInt(numWords - 1)];
		} */
		
		for (int i = 0; i < numWords; i++) {
			wordsLabels[i] = new JLabel("<html> <font size="+4+" color='green'>"+sequence[i]+"    "+words[i]);
			wordsLabels[i].setBounds(column, row, length, 25);
			contentPane.add(wordsLabels[i]);
			wordsLabels[i].addMouseListener(this);
			
			boxes[i] = new JButton();
			boxes[i].setBounds(column, row, length, 25);
			contentPane.add(boxes[i]);
			boxes[i].setBackground(Color.GREEN);
			boxes[i].setVisible(false);
			boxes[i].setEnabled(false);
			
			row += 26;
			if (row > 374) {
				column = 300;
				row = 145;
			}
		}
		result = new JLabel[4];
		column = 540;
		row = 140;
		for (int i = 0; i < 4; i++) {
			result[i] = new JLabel();
			result[i].setBounds(column, row, 200, 50);
			contentPane.add(result[i]);
			row += 65;
		}
		background = new JLabel(new ImageIcon("background.png"));
		background.setBounds(0, 0, 750, 450);
		contentPane.add(background);
		

	}

	public String[] readWords(int numWords, int wordLength) {
		String[] words = new String[numWords];
		Random rand = new Random();
		try {
			int i = 0;
			String line;
			BufferedReader br;
			while (i < numWords) {
				FileReader fr = new FileReader("enable1.txt");
				br = new BufferedReader(fr);
				int lineNum = rand.nextInt(172818);
				for (int k = 0; k < lineNum; k++) {
					br.readLine();
				}
				line = br.readLine().toUpperCase();
				if (line.length() == wordLength) {
					words[i] = line;
					i++;
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return words;
	}
	
	public boolean contains(String[] arr, String str) {
		for (String i: arr) {
			if (i.equals(str))
				return true;
		}
		return false;
	}
	
	public String[] genSeq(int numWords) {
		Random rand = new Random();
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String[] sequence = new String[numWords];
		int n;
		String start = "0x" + alphabet.substring(n = rand.nextInt(35), n + 1) + alphabet.substring(n = rand.nextInt(35), n + 1);
		for (int i = 0; i < sequence.length; i++) {
			sequence[i] = start + alphabet.substring(n = rand.nextInt(35), n + 1) + alphabet.substring(n = rand.nextInt(35), n + 1);
		}
		return sequence;
	}
	
	public int likeness(String str1, String str2) {
		int counter = 0;
		for (int i = 0; i < str1.length(); i++) {
			if (str1.substring(i, i+1).equals(str2.substring(i, i+1)))
				counter++;
		}
		return counter;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Component c = e.getComponent();
		int index = 0;
		if (e.getButton() == MouseEvent.BUTTON1) {
			String clicked = "";
			for (int i = 0; i < numWords; i++) {
				if (c.equals(wordsLabels[i])) {
					clicked = wordsLabels[i].getText().substring(wordsLabels[i].getText().length() - wordLength, wordsLabels[i].getText().length());
					boxes[i].setVisible(true);
					wordsLabels[i].removeMouseListener(this);
					index = i;
				}
			}
			if (clicked.equals(correct)) {
				wordsLabels[index].setText("<html> <font size="+4+" color='gray'>" + sequence[index] + " " + correct);
				result[0].setText(result[1].getText());
				result[1].setText(result[2].getText());
				result[2].setText(result[3].getText());
				result[3].setText("<html> <font size="+4+" color='green'> &gt; " + clicked +"<br> &gt; Access granted. ");
				for (int i = 0; i < numWords; i++) {
					wordsLabels[i].removeMouseListener(this);
				}
			} else {
				int tries = Integer.parseInt(attempts.getText().substring(35, 36));
				tries--;
				String attemptsText = "<html> <font size="+4+" color='green'> " + tries+" ATTEMPTS LEFT: ";
				for (int i = 0; i < tries; i++) {
					attemptsText = attemptsText + "█ ";
				}
				attempts.setText(attemptsText);
				if (tries == 0) {
					for (int i = 0; i < numWords; i++) {
						wordsLabels[i].removeMouseListener(this);
					}
					result[0].setText(result[1].getText());
					result[1].setText(result[2].getText());
					result[2].setText(result[3].getText());
					result[3].setText("<html> <font size="+4+" color='green'> &gt; Access denied.<br> &gt; User locked out.");
				} else {
					result[0].setText(result[1].getText());
					result[1].setText(result[2].getText());
					result[2].setText(result[3].getText());
					result[3].setText("<html> <font size="+4+" color='green'> &gt; " + clicked +"<br> &gt; Access denied. <br>&gt; "+likeness(clicked, correct)+"/"+wordLength+" correct");
	
				}
				
			}
		} else {
			if (e.getButton() == MouseEvent.BUTTON3) {
				for (int i = 0; i < numWords; i++) {
					if (c.equals(wordsLabels[i])) {
						if (boxes[i].getBackground() == Color.GREEN) {
							boxes[i].setVisible(true);
							boxes[i].setBackground(Color.LIGHT_GRAY);
						} else {
							boxes[i].setVisible(false);
							boxes[i].setBackground(Color.GREEN);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		Component c = e.getComponent();
		for (int i = 0; i < numWords; i++) {
			if (c.equals(wordsLabels[i]))
				boxes[i].setVisible(true);
				//System.out.println(wordsLabels[i].getText());
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		Component c = e.getComponent();
		for (int i = 0; i < numWords; i++) {
			if (c.equals(wordsLabels[i]))
				boxes[i].setVisible(false);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
}
