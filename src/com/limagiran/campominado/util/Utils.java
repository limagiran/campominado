package com.limagiran.campominado.util;

import java.awt.event.MouseEvent;

/**
 *
 * @author Vinicius Silva
 */
public class Utils {

    private static long x1 = -1, y1 = -1, z1 = 0, x2 = -1, y2 = -1, z2 = 0;

    /**
     * Verifica se houve um clique com os dois botões do mouse ao mesmo tempo
     *
     * @param e MouseEvent
     * @return <i>true</i> ou <i>false</i>
     */
    public static boolean isClickTwoButtons(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1: {
                return (((x1 = e.getXOnScreen()) == x2) 
                        & ((y1 = e.getYOnScreen()) == y2) 
                        & ((z1 = System.currentTimeMillis()) - z2) <= 150);
            }
            case MouseEvent.BUTTON3: {
                return (((x2 = e.getXOnScreen()) == x1) 
                        & ((y2 = e.getYOnScreen()) == y1) 
                        & ((z2 = System.currentTimeMillis()) - z1) <= 150);
            }
        }
        return false;
    }

}
