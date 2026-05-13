import gui.MainFrame;
import gui.auth.LoginWindow;
import javax.swing.SwingUtilities;

public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                
                // 显示登录窗口
                LoginWindow loginWindow = new LoginWindow(frame);
                loginWindow.setVisible(true);
            }
        });
    }
}