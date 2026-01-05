import javax.swing.*;
import util.SimpleHttpServer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::createAndShowGUI);
    }

    private static void createAndShowGUI() {

        JFrame frame = new JFrame("Click to Ten");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        AtomicInteger clicks = new AtomicInteger(0);

        // GIF 物件
        JLabel gifLabel = new JLabel();
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gifLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        gifLabel.setVisible(false);
        ImageIcon gifIcon = new ImageIcon("vid/badApple.gif");
        gifLabel.setIcon(gifIcon);

        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(new BorderLayout());
        frame.setContentPane(bgPanel);

        // Declare imgLabel
        final JLabel[] imgLabel = { null };
        // Declare timerLabel
        final JLabel[] timerLabel = { null };
        // Declare timer
        final javax.swing.Timer[] timer = { null };

        // initial background
        bgPanel.setBackgroundImage("img/00.png");
        bgPanel.add(gifLabel, BorderLayout.CENTER);

        // Declare clip
        final javax.sound.sampled.Clip[] clip = new javax.sound.sampled.Clip[1];

        // 預先加載音樂檔案
        try {
            javax.sound.sampled.AudioInputStream audioIn = javax.sound.sampled.AudioSystem
                    .getAudioInputStream(new java.io.File("music/nggyu.wav"));
            clip[0] = javax.sound.sampled.AudioSystem.getClip();
            clip[0].open(audioIn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Ctrl+C 複製網址
        KeyAdapter ctrlCListener = new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if ((e.getKeyCode() == java.awt.event.KeyEvent.VK_C)
                        && ((e.getModifiersEx() & java.awt.event.InputEvent.CTRL_DOWN_MASK) != 0)) {
                    String url = "https://ilikepopcorn.qzz.io/";
                    java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(url);
                    java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                    JOptionPane.showMessageDialog(null, "已複製網址到剪貼簿！", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };

        // N 鍵開新視窗
        KeyAdapter nKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_N) {
                    JFrame newFrame = new JFrame("New Window");
                    newFrame.setSize(400, 300);
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    int maxX = screenSize.width - newFrame.getWidth();
                    int maxY = screenSize.height - newFrame.getHeight();
                    int randX = (int) (Math.random() * maxX);
                    int randY = (int) (Math.random() * maxY);
                    newFrame.setLocation(randX, randY);
                    newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    newFrame.setVisible(true);
                    // 新增視窗後自動將焦點設回原視窗
                    frame.toFront();
                    frame.requestFocus();
                }
            }
        };

        bgPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int c = clicks.incrementAndGet();
                switch (c) {
                    case 1:
                        bgPanel.setBackgroundImage("img/01.png");
                        gifLabel.setVisible(false);
                        // nothing to do for case 1
                        break;
                    case 2:
                        bgPanel.setBackgroundImage("img/02.png");
                        // 顯示 GIF
                        gifLabel.setVisible(true);
                        break;
                    case 3:
                        bgPanel.setBackgroundImage("img/03.png");
                        gifLabel.setVisible(false);
                        // 播放預先加載的音樂
                        try {
                            if (clip[0] != null) {
                                clip[0].setFramePosition(0); // 從頭播放
                                clip[0].start();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case 4:
                        bgPanel.setBackgroundImage("img/04.png");
                        if (clip[0] != null && clip[0].isRunning()) {
                            clip[0].stop();
                        }
                        // 滾動滑鼠滾輪改變視窗大小
                        bgPanel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
                            @Override
                            public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
                                int notches = e.getWheelRotation();
                                Dimension size = frame.getSize();
                                int newWidth = Math.max(200, size.width - notches * 40);
                                int newHeight = Math.max(150, size.height - notches * 30);
                                frame.setSize(newWidth, newHeight);
                            }
                        });
                        break;
                    case 5:
                        bgPanel.setBackgroundImage("img/05.png");

                        bgPanel.removeMouseWheelListener(bgPanel.getMouseWheelListeners()[0]);
                        // 按下 N 鍵開新視窗
                        frame.addKeyListener(nKeyListener);
                        frame.setFocusable(true);
                        frame.requestFocusInWindow();
                        break;
                    case 6:
                        bgPanel.setBackgroundImage("img/06.png");
                        frame.removeKeyListener(nKeyListener);
                        frame.addKeyListener(ctrlCListener);
                        frame.setFocusable(true);
                        frame.requestFocusInWindow();
                        break;
                    case 7:
                        bgPanel.setBackgroundImage("img/07.png");
                        frame.removeKeyListener(ctrlCListener);

                        timerLabel[0] = new JLabel("Timer: 0 s", SwingConstants.CENTER);
                        timerLabel[0].setFont(timerLabel[0].getFont().deriveFont(22f));
                        timerLabel[0].setForeground(Color.YELLOW);
                        bgPanel.add(timerLabel[0], BorderLayout.CENTER);

                        final int[] seconds = { 0 };
                        timer[0] = new javax.swing.Timer(1000, evt -> {
                            seconds[0]++;
                            timerLabel[0].setText("Timer: " + seconds[0] + " s");
                            if (seconds[0] == 10) {
                                timer[0].stop();
                                imgLabel[0] = new JLabel(new ImageIcon("img/1000.jpg"));
                                imgLabel[0].setHorizontalAlignment(SwingConstants.CENTER);
                                imgLabel[0].setVerticalAlignment(SwingConstants.BOTTOM);
                                bgPanel.add(imgLabel[0], BorderLayout.SOUTH);
                                bgPanel.revalidate();
                                bgPanel.repaint();
                            }
                        });
                        timer[0].start();
                        break;
                    case 8:
                        bgPanel.setBackgroundImage("img/08.png");
                        // 強制結束 case 7 timer event
                        if (timer[0] != null && timer[0].isRunning()) {
                            timer[0].stop();
                        }
                        if (imgLabel[0] != null) {
                            imgLabel[0].setVisible(false);
                        }
                        if (timerLabel[0] != null) {
                            timerLabel[0].setVisible(false);
                        }

                        try {
                            // 取得目前類別的 .class 檔案位置
                            URL location = App.class.getProtectionDomain()
                                    .getCodeSource()
                                    .getLocation();

                            // 轉成 File 並取得父目錄（這就是程式根目錄）
                            File classPath = new File(location.toURI());
                            File rootDir = classPath.getParentFile();

                            // 要生成的檔案路徑
                            File txtFile = new File(rootDir, "something.txt");

                            // 寫入內容
                            try (FileWriter writer = new FileWriter(txtFile)) {
                                writer.write("我其實蠻愛喝激浪汽水的\n"); // edit here
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case 9:
                        bgPanel.setBackgroundImage("img/09.png");
                        // 啟動 HTTP 伺服器，服務 html 資料夾內容
                        try {
                            if (System.getProperty("http.server.started") == null) {
                                String htmlDir = new java.io.File("html").getAbsolutePath();
                                SimpleHttpServer server = new SimpleHttpServer(8080, htmlDir);
                                server.start();
                                System.setProperty("http.server.started", "true");
                            } else {
                                JOptionPane.showMessageDialog(frame, "HTTP 伺服器已經啟動。", "HTTP Server",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, "HTTP 伺服器啟動失敗: " + ex.getMessage(), "錯誤",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case 10:
                        bgPanel.setBackgroundImage("img/10.png");

                        break;
                    case 11:
                        System.exit(0);
                        break;
                    default:
                        int idx = Math.min(c, 10);
                        bgPanel.setBackgroundImage(String.format("img/%02d.png", idx));
                        gifLabel.setVisible(false);
                        break;
                }
            }
        });

        frame.setVisible(true);
        // 當視窗關閉時移除 KeyListener，避免重複註冊
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                frame.removeKeyListener(nKeyListener);
                frame.removeKeyListener(ctrlCListener);
            }
        });
    }

    private static class BackgroundPanel extends JPanel {
        private Image background;

        public void setBackgroundImage(String path) {
            ImageIcon icon = new ImageIcon(path);
            if (icon.getIconWidth() > 0) {
                background = icon.getImage();
            } else {
                background = null;
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (background != null) {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
