package vigenere;

import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Diese Klasse geniert sowohl das graphische Fenster der Vigenere-Verschluesselung, als auch die interne Ver- und Entschluesselung.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class Vigenere {
	
	private JFrame frame1 = new JFrame("Vigenère-Verschlüsselung");
	private String umbruch = System.getProperty("line.separator");
	private Button buttonverschluesseln = new Button("-->");
	private Button buttonentschluesseln = new Button("<--");
	private JTextField schluesselfeld = new JTextField();
	private JTextArea klartext = new JTextArea();
	private JScrollPane klartextScrollPane = new JScrollPane(klartext);
	private JTextArea geheimtext = new JTextArea();
	private JScrollPane geheimtextScrollPane = new JScrollPane(geheimtext);

	private String klartextstring = new String();
	private String geheimtextstring = new String();
	private char[] schluessel;
	
	public Vigenere() {
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(800,600));
		frame1.setMinimumSize(new Dimension(600,400));
		frame1.setResizable(true);
		
		buttonverschluesseln.setVisible(true);
		buttonverschluesseln.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonverschluesseln_ActionPerformed();
			}
		});
		buttonentschluesseln.setVisible(true);
		buttonentschluesseln.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonentschluesseln_ActionPerformed();
			}
		});
		klartext.setText("Heizölrückstoßabdämpfung");
		klartext.setLineWrap(true);
		klartext.setWrapStyleWord(true);
		klartext.setToolTipText("Klartext");
		geheimtext.setLineWrap(true);
	    geheimtext.setWrapStyleWord(true);
	    geheimtext.setToolTipText("Geheimtext");
	    schluesselfeld.setToolTipText("Schlüssel");
		schluesselfeld.setHorizontalAlignment(JTextField.CENTER);
		schluesselfeld.setVisible(true);
		
		JPanel linkeflaeche = new JPanel();
		linkeflaeche.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        linkeflaeche.add(klartextScrollPane,new GridBagFelder(0,0,1,1,1,0.99));
        linkeflaeche.add(schluesselfeld,new GridBagFelder(0,1,1,1,1,0.01));
        
		JPanel mittelflaeche = new JPanel();
		mittelflaeche.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridy = 0;
        mittelflaeche.add(buttonverschluesseln, c);
        c.gridy = 1;
        mittelflaeche.add(buttonentschluesseln, c);
        
		Container cp = frame1.getContentPane();
		cp.setLayout(new GridBagLayout());
		cp.add(linkeflaeche,new GridBagFelder(1,1,1,1,0.49,1));
		cp.add(mittelflaeche,new GridBagFelder(2,1,1,1,0.02,1));
		cp.add(geheimtextScrollPane,new GridBagFelder(3,1,1,1,0.49,1));
		
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}

	/**
	 * Diese Methode wird ausgefuehrt, wenn der Button zum Verschluesseln angeklickt wird.<br>
	 * Sie liest den darin enthaltenen String aus, speichert ihn in einem charArray und stoesst, wenn der String nicht leer ist die Verschluesselung an.
	 */
	private void buttonverschluesseln_ActionPerformed() {
		klartextstring = klartext.getText();
		char[] verschluesselarr = klartextstring.toCharArray();
		schluessel = schluesseleinlesen();
		if(schluessel.length!=0) {
			char[] verschluesselarrfertig = verschluesseln(verschluesselarr);
			geheimtextstring = String.valueOf(verschluesselarrfertig);
			geheimtext.setText(geheimtextstring);
		} else {
			JOptionPane.showMessageDialog(null, "Dein Schlüssel ist ungültig."+umbruch+"Bitte gib eine gültige Abfolge ein.", "Falscher Schlüssel", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	/**
	 * Diese Methode fuehrt die Verschluesselung durch. Sie verschiebt alle chars des Ursprungstextes um den eingelesenen Schluessel.
	 * @param original Nimmt den Ursprungstext entgegen.
	 * @return Gibt den verschluesselten Text aus.
	 */
	private char[] verschluesseln(char[] original) {
		char[] cryptArray = new char[original.length];
		for(int i=0; i<original.length; i++) {
			int result = (original[i] + schluessel[i%schluessel.length]) % 256;
			cryptArray[i] = (char) result;
        }
        return cryptArray;
    }
	
	/**
	 * Diese Methode wird ausgefuehrt, wenn der Button zum Entschluesseln angeklickt wird.<br>
	 * Sie liest den darin enthaltenen String aus, speichert ihn in einem charArray und stoesst, wenn der String nicht leer ist die Entschluesselung an.
	 */
	private void buttonentschluesseln_ActionPerformed() {
		geheimtextstring = geheimtext.getText();
		char[] entschluesselarr = geheimtextstring.toCharArray();
		schluessel = schluesseleinlesen();
		if(schluessel.length!=0) {
			char[] entschluesselarrfertig = entschluesseln(entschluesselarr);
			klartextstring = String.valueOf(entschluesselarrfertig);
			klartext.setText(klartextstring);
		} else {
			JOptionPane.showMessageDialog(null, "Dein Schlüssel ist ungültig."+umbruch+"Bitte gib eine gültige Abfolge ein.", "Falscher Schlüssel", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	/**
	 * Diese Methode fuehrt die Entschluesselung durch. Sie verschiebt alle chars des verschluesselten Textes um den eingelesenen Schluessel.
	 * @param verschluesselt Nimmt den verschluesselten Text entgegen.
	 * @return Gibt den Ursprungstext aus.
	 */
	private char[] entschluesseln(char[] verschluesselt) {
		char[] cryptArray = new char[verschluesselt.length];
		for(int i = 0; i < verschluesselt.length; i++) {
			int result;
            if(verschluesselt[i] - schluessel[i%schluessel.length] < 0) {
            	result =  (verschluesselt[i] - schluessel[i%schluessel.length]) + 256;
            } else {
            	result = (verschluesselt[i] - schluessel[i%schluessel.length]) % 256;
            }	
            cryptArray[i] = (char) result;
        }
        return cryptArray;
    }
	
	/**
	 * Diese Methode liest den Schluessel ein und speichert ihn in einem charArray.
	 * @return Gibt den Schluessel als charArray aus.
	 */
	private char[] schluesseleinlesen() {
		String schluesselstr = schluesselfeld.getText();
		return schluesselstr.toCharArray();
	}

	public static void main(String[] args) {
		new Vigenere();
	}
}