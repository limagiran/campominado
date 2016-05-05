package com.limagiran.campominado.control;

import com.limagiran.campominado.settings.Settings.EnumMode;
import com.limagiran.campominado.util.*;
import static com.limagiran.campominado.util.Images.*;
import java.awt.GridLayout;
import static java.awt.event.InputEvent.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Vinicius Silva
 */
public class Tiles {

    private static final int[][] arounds = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0},
        {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
    private List<Tile> tileClicking = new ArrayList<>();
    private JPanel panel;
    private ImageIcon smile = SMILE;
    public final Tile[][] tiles;
    public final List<Tile> tilesList = new ArrayList<>();
    private long timeIni = 0;
    private long timeEnd = 0;
    private EnumMode mode;
    private boolean win = false;

    /**
     * Novo jogo Campo Minado
     *
     * @param mode modo de jogo EnumMode
     * @param panel JPanel onde os quadrados (minas) serão adicionados
     * @see EnumMode
     */
    public Tiles(EnumMode mode, JPanel panel) {
        this.panel = panel;
        this.panel.removeAll();
        this.panel.setLayout(new GridLayout(mode.height, mode.width, 0, 0));
        tiles = new Tile[mode.height][mode.width];
        this.mode = mode;
        init();
    }

    /**
     * Retorna um número aleatório de 0 a (x - 1)
     *
     * @param x limite
     * @return número aleatório
     */
    private static int random(int x) {
        return (((int) (Math.random() * 999999)) % x);
    }

    /**
     * Retorna os quadrados ao redor do quadrado passado por parâmetro
     *
     * @param tile quadrado
     * @return quadrados ao redor
     */
    public List<Tile> getAround(Tile tile) {
        List<Tile> _return = new ArrayList<>();
        int[] coord = getCoordenate(tile);
        if (coord != null) {
            for (int[] ar : arounds) {
                try {
                    _return.add(tiles[coord[1] + ar[1]][coord[0] + ar[0]]);
                } catch (Exception e) {
                    //ignore
                }
            }
        }
        return _return;
    }

    /**
     * Retorna a coordenada (x, y) do quadrado no campo minado
     *
     * @param tile quadrado
     * @return coordenada (x, y) = <i>new int[]{x, y}</i> ou <i>null</i> para
     * quadrado não encontrado
     */
    private int[] getCoordenate(Tile tile) {
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                if (tiles[y][x].equals(tile)) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    /**
     * Configurações e procedimentos iniciais
     */
    private void init() {
        instanceTiles();
        tilesList.stream().forEach(t -> panel.add(t));
        for (MouseListener ml : panel.getMouseListeners()) {
            panel.removeMouseListener(ml);
        }
        for (MouseMotionListener ml : panel.getMouseMotionListeners()) {
            panel.removeMouseMotionListener(ml);
        }
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                clicking(e);
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clicking(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                executeClick(e);
            }
        });
    }

    /**
     * Inicia todos os quadrados do campo
     */
    private void instanceTiles() {
        for (Tile[] ts : tiles) {
            for (int i = 0; i < ts.length; i++) {
                ts[i] = new Tile(this);
                tilesList.add(ts[i]);
            }
        }
    }

    /**
     * Insere as bombas no campo minado.
     *
     * @param tileExcept excluir o primeiro quadrado clicado
     */
    private void insertBombs(Tile tileExcept) {
        int flag = mode.getBombs();
        while (flag > 0) {
            int w = random(mode.width);
            int h = random(mode.height);
            if (!tiles[h][w].isBomb() && !tiles[h][w].equals(tileExcept)) {
                tiles[h][w].toBomb();
                flag--;
            }
        }
    }

    /**
     * Encerra a partida
     *
     * @param win <i>true</i> se houve vitória. <i>false</i> se uma bomba foi
     * clicada.
     */
    protected void gameOver(boolean win) {
        if (timeEnd == 0) {
            timeEnd = System.currentTimeMillis();
            smile = (win ? SMILE_GLASSES : SMILE_DEAD);
            tilesList.stream().forEach(t -> t.gameOver(win));
        }
        this.win = win;
    }

    /**
     * Bombas restantes a serem marcadas
     *
     * @return a quantidade de bombas restantes a serem marcadas com bandeira
     */
    public int getBombsRest() {
        long _return = tilesList.stream().filter((t) -> (t.isFlag())).count();
        _return = mode.getBombs() - _return;
        return (int) ((_return < 0) ? 0 : _return);
    }

    /**
     * Executa os eventos para um quadrado clicado
     *
     * @param tile quadrado clicado
     */
    protected void clicked(Tile tile) {
        //verifica se é o primeiro quadrado clicado da partida
        if (timeIni == 0) {
            start(tile);
        }
    }

    /**
     * Inicia a partida a partir do primeiro clique em um quadrado
     *
     * @param tile primeiro quadrado clicado
     */
    protected void start(Tile tile) {
        timeIni = System.currentTimeMillis();
        insertBombs(tile);
        //Adiciona em cada quadrado a imagem correspondente ao número de 
        //bombas ao redor dele.
        tilesList.stream().forEach(t -> t.calculateAmountBombs());
    }

    /**
     * Retorna o tempo decorrido desde o início da partida até o momento atual
     * (caso a partida esteja em andamento) ou até o término dela.
     *
     * @return tempo decorrido em segundos, retorna o valor máximo de 999.
     */
    public long getTime() {
        if (timeIni != 0) {
            long end = ((timeEnd != 0) ? timeEnd : System.currentTimeMillis());
            long _return = ((end - timeIni) / 1000L);
            return ((_return <= 999L) ? _return : 999L);
        }
        return 0;
    }

    /**
     * Verificar se houve vitória (todos os quadrados sem bomba descobertos). Se
     * sim, executa o fim do jogo com vitória.
     */
    protected void verifyWin() {
        if (tilesList.stream().noneMatch((t) -> (!t.isClicked() && !t.isBomb()))) {
            gameOver(true);
        }
    }

    /**
     * Retorna a imagem Smile atual. (SMILE, SMILE_GLASSES, SMILE_DEAD)
     *
     * @return imagem smile atual.
     */
    public Icon getSmile() {
        return smile;
    }

    /**
     * Altera o quadrado sendo clicado atualmente. Altera a imagem do quadrado
     * para Empty. Altera o status de "sendo clicado" para os quadrados ao
     * redor, caso o clique seja com o botão direito e esquerdo ao mesmo tempo.
     *
     * @param e MouseEvent
     * @see Tile#clicking(boolean)
     */
    private void clicking(MouseEvent e) {
        tileClicking.removeIf((Tile t) -> (t == null));
        while (!tileClicking.isEmpty()) {
            tileClicking.remove(0).clicking(false);
        }
        Object obj = panel.getComponentAt(e.getPoint());
        if ((obj != null) && (obj instanceof Tile)) {
            Tile t = (Tile) obj;
            if (e.getModifiersEx() == BUTTON1_DOWN_MASK) {
                tileClicking.add(t);
                t.clicking(true);
            } else if (e.getModifiersEx() == (BUTTON1_DOWN_MASK 
                    + BUTTON3_DOWN_MASK)) {
                tileClicking.addAll(getAround(t));
                tileClicking.add(t);
                tileClicking.stream().forEach((t2) -> {
                    t2.clicking(true);
                });
            }
        }
    }

    /**
     * Verifica se a partida foi vencida
     *
     * @return <i>true</i> para vitória. <i>false</i> para derrota ou partida em
     * andamento.
     */
    public boolean isWin() {
        return win;
    }

    /**
     * Executa o procedimento para um evento de clique do mouse no campo minado.
     *
     * @param e MouseEvent
     * @see Tile#click(java.awt.event.MouseEvent)
     */
    private void executeClick(MouseEvent e) {
        Object obj = panel.getComponentAt(e.getPoint());
        if ((obj != null) && (obj instanceof Tile)) {
            if (Utils.isClickTwoButtons(e)) {
                ((Tile) obj).clickAround();
                clicking(e);
            } else if ((e.getModifiersEx() == 256) || (e.getModifiersEx() == 0)) {
                ((Tile) obj).click(e);
                clicking(e);
            }
        }
    }
}
