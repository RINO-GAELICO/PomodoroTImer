/*
 * A desktop application that helps you with study sessions.
 * Inspired by Pomodoro technique.
 * Three different timers in three different tabs for 
 * Long, Medium and Short sessions of study.
 */

package Countdown;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class CountDown {

	private JFrame frame;
	private JPanel contentPane, longSessionPanel, mediumSessionPanel, shortSessionPanel;
	private JLabel lblTimeLong, lblTimeMedium, lblTimeShort, lblTime;
	private JLabel lblPomodoro;
	private JButton btnMStartButton, btnLStartButton, btnSStartButton, btnLStopButton, btnLResumeButton, btnMStopButton,
			btnSStopButton, btnMResumeButton, btnSResumeButton;
	private Font font1 = new Font("DIN Alternate", Font.PLAIN, 30);
	private Timer timerL, timerM, timerS; // Updates the count every second
	private int second = 0, minute = 25;
	boolean executedLong = false, executedShort = false, executedMedium = false;
	DecimalFormat dFormat = new DecimalFormat("00");
	String ddSecond, ddMinute;
	private ImageIcon imagePomodoro = new ImageIcon("pomodoro_icon.png");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CountDown window = new CountDown();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CountDown() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		/*
		 * Sets up the frame that host the panel
		 */
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		longSessionPanel = createLongSesssionPAnel();
		mediumSessionPanel = createMediumSessionPanel();
		shortSessionPanel = createShortSessionPanel();

		/*
		 * Attempt to create a pane with various tabs
		 */
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Long Session", null, longSessionPanel, "Long"); 
		tabbedPane.addTab("Medium Session", null, mediumSessionPanel, "Medium"); 
		tabbedPane.addTab("Short Session", null, shortSessionPanel, "Short");

		/*
		 * Sets up the Panel that hosts labels and buttons
		 */
		contentPane = new JPanel(new BorderLayout());

		contentPane.add(tabbedPane, BorderLayout.CENTER);

		frame.setContentPane(contentPane);
		frame.setSize(380, 230);
		frame.setMinimumSize(new Dimension(380, 230));

		/*
		 * Resizing the ImageIcon to fit the label
		 */
		Image image = imagePomodoro.getImage(); // transform it
		Image newimg = image.getScaledInstance(20, 20, Image.SCALE_FAST); // scale it the fast way
		imagePomodoro = new ImageIcon(newimg); // transform it back
		setLblPomodoro(new JLabel("Pomodoro Timer", imagePomodoro, JLabel.CENTER));
		lblPomodoro.setFont(new Font("DIN Alternate", Font.PLAIN, 28));
		lblPomodoro.setHorizontalAlignment(SwingConstants.CENTER);

		/*
		 * Locate the Label at the top
		 */
		lblPomodoro.setBounds(134, 28, 206, 36);
		contentPane.add(lblPomodoro, BorderLayout.PAGE_START);

	}

	private JPanel createShortSessionPanel() {

		JPanel ShortSessionPane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		/*
		 * Sets the Time Label
		 */
		lblTimeShort = new JLabel("10:00");
		lblTimeShort.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeShort.setFont(font1);
		lblTimeShort.setVerticalAlignment(JLabel.CENTER);
		lblTimeShort.setVerticalTextPosition(SwingConstants.TOP);
		lblTimeShort.setBounds(200, 200, 200, 100);

		/*
		 * Place the Label TIMER in the CENTER of the panel
		 */
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0; // make this component tall
		c.weightx = 1;
		c.gridwidth = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;

		ShortSessionPane.add(lblTimeShort, c);

		/*
		 * Place the button START
		 */
		c.fill = GridBagConstraints.NONE;
		c.ipady = 0; // reset to default
		c.weighty = 1; // request any extra vertical space
		c.anchor = GridBagConstraints.LAST_LINE_START; // Left of space
		c.gridx = 0; // aligned with button 2
		c.gridwidth = 0; // 1 columns wide
		c.gridy = 0; // third row
		btnSStartButton = new JButton("Start");

		/*
		 * Sets up the Start button and give the action to be performed
		 */
		btnSStartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!executedShort) {
					second = 0;
					minute = 10;
					countDownTimerShort();
					timerS.start();
				}

			}
		});

		ShortSessionPane.add(btnSStartButton, c);

		/*
		 * Place Stop Button
		 */
		c.fill = GridBagConstraints.NONE;
		c.ipady = 0; // reset to default
		c.weighty = 1; // request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; // Center of space
		c.gridx = 0; // aligned with button 2
		c.gridwidth = 0; // 1 columns wide
		c.gridy = 0; // third row
		btnSStopButton = new JButton("Stop");
		ShortSessionPane.add(btnSStopButton, c);

		/*
		 * Gives the action to Stop Button
		 */
		btnSStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				timerS.stop();

			}

		});

		/*
		 * Place Resume Button
		 */
		c.fill = GridBagConstraints.NONE;
		c.ipady = 0; // reset to default
		c.weighty = 1; // request any extra vertical space
		c.anchor = GridBagConstraints.LAST_LINE_END; // Right of space
		c.gridx = 0; // aligned with button 2
		c.gridwidth = 0; // 1 columns wide
		c.gridy = 0; // third row
		btnSResumeButton = new JButton("Resume");
		ShortSessionPane.add(btnSResumeButton, c);

		/*
		 * Gives the action to Resume Button
		 */
		btnSResumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (executedShort) {
					timerS.start();
				}
			}
		});

		return ShortSessionPane;
	}

	private JPanel createMediumSessionPanel() {

		JPanel MediumSessionPane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		/*
		 * Sets the Time Label
		 */
		lblTimeMedium = new JLabel("15:00");
		lblTimeMedium.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeMedium.setFont(font1);
		lblTimeMedium.setVerticalAlignment(JLabel.CENTER);
		lblTimeMedium.setVerticalTextPosition(SwingConstants.TOP);
		lblTimeMedium.setBounds(200, 200, 200, 100);

		/*
		 * Place the Label TIMER in the CENTER of the panel
		 */
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0; // make this component tall
		c.weightx = 1;
		c.gridwidth = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;

		MediumSessionPane.add(lblTimeMedium, c);

		/*
		 * Place the button START
		 */
		c.fill = GridBagConstraints.NONE;
		c.ipady = 0; // reset to default
		c.weighty = 1; // request any extra vertical space
		c.anchor = GridBagConstraints.LAST_LINE_START; // Left of space
		c.gridx = 0; // aligned with button 2
		c.gridwidth = 0; // 1 columns wide
		c.gridy = 0; // third row
		btnMStartButton = new JButton("Start");

		/*
		 * Sets up the Start button and give the action to be performed
		 */
		btnMStartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!executedMedium) {
					second = 0;
					minute = 15;
					countDownTimerMedium();
					timerM.start();
				}

			}
		});

		MediumSessionPane.add(btnMStartButton, c);

		/*
		 * Place Stop Button
		 */
		c.fill = GridBagConstraints.NONE;
		c.ipady = 0; // reset to default
		c.weighty = 1; // request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; // Center of space
		c.gridx = 0; // aligned with button 2
		c.gridwidth = 0; // 1 columns wide
		c.gridy = 0; // third row
		btnMStopButton = new JButton("Stop");
		MediumSessionPane.add(btnMStopButton, c);

		/*
		 * Gives the action to Stop Button
		 */
		btnMStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				timerM.stop();

			}

		});

		/*
		 * Place Resume Button
		 */
		c.fill = GridBagConstraints.NONE;
		c.ipady = 0; // reset to default
		c.weighty = 1; // request any extra vertical space
		c.anchor = GridBagConstraints.LAST_LINE_END; // Right of space
		c.gridx = 0; // aligned with button 2
		c.gridwidth = 0; // 1 columns wide
		c.gridy = 0; // third row
		btnMResumeButton = new JButton("Resume");
		MediumSessionPane.add(btnMResumeButton, c);

		/*
		 * Gives the action to Resume Button
		 */
		btnMResumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (executedMedium) {
					timerM.start();
				}
			}
		});

		return MediumSessionPane;
	}

	private JPanel createLongSesssionPAnel() {

		JPanel LongSessionPane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		/*
		 * Sets the Time Label
		 */
		lblTimeLong = new JLabel("25:00");
		lblTimeLong.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeLong.setFont(font1);
		lblTimeLong.setVerticalAlignment(JLabel.CENTER);
		lblTimeLong.setVerticalTextPosition(SwingConstants.TOP);
		lblTimeLong.setBounds(200, 200, 200, 100);

		/*
		 * Place the Label TIMER in the CENTER of the panel
		 */
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0; // make this component tall
		c.weightx = 1;
		c.gridwidth = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;

		LongSessionPane.add(lblTimeLong, c);

		/*
		 * Place the button START
		 */
		c.fill = GridBagConstraints.NONE;
		c.ipady = 0; // reset to default
		c.weighty = 1; // request any extra vertical space
		c.anchor = GridBagConstraints.LAST_LINE_START; // Left of space
		c.gridx = 0; // aligned with button 2
		c.gridwidth = 0; // 1 columns wide
		c.gridy = 0; // third row
		btnLStartButton = new JButton("Start");

		/*
		 * Sets up the Start button and give the action to be performed
		 */
		btnLStartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!executedLong) {
					second = 0;
					minute = 25;
					countDownTimerLong();
					timerL.start();
				}

			}
		});

		LongSessionPane.add(btnLStartButton, c);

		/*
		 * Place Stop Button
		 */
		c.fill = GridBagConstraints.NONE;
		c.ipady = 0; // reset to default
		c.weighty = 1; // request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; // Center of space
		c.gridx = 0; // aligned with button 2
		c.gridwidth = 0; // 1 columns wide
		c.gridy = 0; // third row
		btnLStopButton = new JButton("Stop");
		LongSessionPane.add(btnLStopButton, c);

		/*
		 * Gives the action to Stop Button
		 */
		btnLStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				timerL.stop();

			}

		});

		/*
		 * Place Resume Button
		 */
		c.fill = GridBagConstraints.NONE;
		c.ipady = 0; // reset to default
		c.weighty = 1; // request any extra vertical space
		c.anchor = GridBagConstraints.LAST_LINE_END; // Right of space
		c.gridx = 0; // aligned with button 2
		c.gridwidth = 0; // 1 columns wide
		c.gridy = 0; // third row
		btnLResumeButton = new JButton("Resume");
		LongSessionPane.add(btnLResumeButton, c);

		/*
		 * Gives the action to Resume Button
		 */
		btnLResumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (executedLong) {
					timerL.start();
				}
			}
		});

		return LongSessionPane;
	}

	/*
	 * Method that starts the timer and updates it every second
	 */

	public void countDownTimerLong() {

		lblTime = lblTimeLong;
		executedLong = true;

		timerL = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				/*
				 * Update the timer every second; seconds -1 and minute -1 when seconds reach
				 * zero
				 */
				second--;
				ddSecond = dFormat.format(second);
				ddMinute = dFormat.format(minute);
				lblTime.setText(ddMinute + ":" + ddSecond);
				/*
				 * Particular case when the second reach zero and also minutes are at zero. In
				 * this case we reached the end of the countdown.
				 */
				if (second == 0 && minute == 0) {

					ddSecond = dFormat.format(second);
					ddMinute = dFormat.format(minute);
					lblTime.setText(ddMinute + ":" + ddSecond);
					timerL.stop();
					/*
					 * Play a sound at the end of the countdown
					 */
					Clip clip = null;
					try {
						clip = AudioSystem.getClip();
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					File fileAudio = new File("Glass.aiff");
					AudioInputStream inputStream = null;
					try {
						inputStream = AudioSystem.getAudioInputStream(fileAudio);
					} catch (UnsupportedAudioFileException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					try {
						clip.open(inputStream);
					} catch (LineUnavailableException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					clip.start();

				} else if (second == -1) {
					/*
					 * Regular case when seconds reach -1
					 */
					minute--;
					second = 59;
					ddSecond = dFormat.format(second);
					ddMinute = dFormat.format(minute);
					lblTime.setText(ddMinute + ":" + ddSecond);
					System.out.println("Second == -1");

				}
			}
		});

	}

	public void countDownTimerMedium() {

		lblTime = lblTimeMedium;
		executedMedium = true;

		timerM = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				/*
				 * Update the timer every second; seconds -1 and minute -1 when seconds reach
				 * zero
				 */
				second--;
				ddSecond = dFormat.format(second);
				ddMinute = dFormat.format(minute);
				lblTime.setText(ddMinute + ":" + ddSecond);
				/*
				 * Particular case when the second reach zero and also minutes are at zero. In
				 * this case we reached the end of the countdown.
				 */
				if (second == 0 && minute == 0) {

					ddSecond = dFormat.format(second);
					ddMinute = dFormat.format(minute);
					lblTime.setText(ddMinute + ":" + ddSecond);
					timerM.stop();
					/*
					 * Play a sound at the end of the countdown
					 */
					Clip clip = null;
					try {
						clip = AudioSystem.getClip();
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					File fileAudio = new File("Glass.aiff");
					AudioInputStream inputStream = null;
					try {
						inputStream = AudioSystem.getAudioInputStream(fileAudio);
					} catch (UnsupportedAudioFileException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					try {
						clip.open(inputStream);
					} catch (LineUnavailableException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					clip.start();

				} else if (second == -1) {
					/*
					 * Regular case when seconds reach -1
					 */
					minute--;
					second = 59;
					ddSecond = dFormat.format(second);
					ddMinute = dFormat.format(minute);
					lblTime.setText(ddMinute + ":" + ddSecond);
					System.out.println("Second == -1");

				}
			}
		});

	}

	public void countDownTimerShort() {

		lblTime = lblTimeShort;
		executedShort = true;

		timerS = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				/*
				 * Update the timer every second; seconds -1 and minute -1 when seconds reach
				 * zero
				 */
				second--;
				ddSecond = dFormat.format(second);
				ddMinute = dFormat.format(minute);
				lblTime.setText(ddMinute + ":" + ddSecond);
				/*
				 * Particular case when the second reach zero and also minutes are at zero. In
				 * this case we reached the end of the countdown.
				 */
				if (second == 0 && minute == 0) {

					ddSecond = dFormat.format(second);
					ddMinute = dFormat.format(minute);
					lblTime.setText(ddMinute + ":" + ddSecond);
					timerS.stop();
					/*
					 * Play a sound at the end of the countdown
					 */
					Clip clip = null;
					try {
						clip = AudioSystem.getClip();
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					File fileAudio = new File("Glass.aiff");
					AudioInputStream inputStream = null;
					try {
						inputStream = AudioSystem.getAudioInputStream(fileAudio);
					} catch (UnsupportedAudioFileException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					try {
						clip.open(inputStream);
					} catch (LineUnavailableException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					clip.start();

				} else if (second == -1) {
					/*
					 * Regular case when seconds reach -1
					 */
					minute--;
					second = 59;
					ddSecond = dFormat.format(second);
					ddMinute = dFormat.format(minute);
					lblTime.setText(ddMinute + ":" + ddSecond);
					System.out.println("Second == -1");

				}
			}
		});

	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}

	public JLabel getLblTime() {
		return lblTime;
	}

	public void setLblTime(JLabel lblTime) {
		this.lblTime = lblTime;
	}

	public JLabel getLblPomodoro() {
		return lblPomodoro;
	}

	public void setLblPomodoro(JLabel lblPomodoro) {
		this.lblPomodoro = lblPomodoro;
	}

}
