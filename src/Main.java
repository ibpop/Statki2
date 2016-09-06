import view.MainFrame;

import java.awt.*;

public class Main {

    private static MainFrame mMainFrame;

    public static void main(String [] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    mMainFrame = MainFrame.getInstance();

                } catch(Exception e){

                }
            }
        });
    }
}
