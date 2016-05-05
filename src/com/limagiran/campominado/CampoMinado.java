package com.limagiran.campominado;

import com.limagiran.campominado.view.GamePlay;
import javax.swing.UIManager.LookAndFeelInfo;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Vinicius Silva
 */
public class CampoMinado {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        applyNimbus();
        GamePlay.main();
    }

    public static void applyNimbus() {
        try {
            for (LookAndFeelInfo info : getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException |
                InstantiationException | IllegalAccessException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

}
