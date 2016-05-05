package com.limagiran.campominado.control;

import static com.limagiran.campominado.util.Images.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Vinicius Silva
 */
public class Tile extends JLabel {

    private final Tiles tiles;
    private ImageIcon tileShowing = TILE_DEFAULT;
    private final List<ImageIcon> tilesClickR = new ArrayList<>(
            Arrays.asList(TILE_DEFAULT, TILE_FLAG, TILE_QUESTION));
    private ImageIcon tile = TILE_EMPTY;
    private boolean blocked = false;
    private int bombsAmount = 0;

    /**
     * Cria um novo quadrado para o campo minado
     *
     * @param tiles campo minado
     */
    protected Tile(Tiles tiles) {
        super(TILE_DEFAULT);
        this.tiles = tiles;
    }

    /**
     * Executa o código para um clique com botão esquerdo
     */
    private void clickLeft() {
        if (!isFlag() && !isClicked()) {
            tiles.clicked(this);
            if (isBomb()) {
                tiles.gameOver(false);
            } else {
                tileShowing = tile;
                setIcon(tileShowing);
                if (tile.equals(TILE_EMPTY)) {
                    tiles.getAround(this).stream().forEach(t -> t.clickLeft());
                }
                tiles.verifyWin();
            }
        }
    }

    /**
     * Executa o código para um clique com botão direito
     */
    private void clickRight() {
        int index = tilesClickR.indexOf(tileShowing);
        if (index != -1) {
            tileShowing = tilesClickR.get((++index % tilesClickR.size()));
        }
    }

    /**
     * Verifica se o campo é uma bomba
     *
     * @return <i>true</i> ou <i>false</i>
     */
    protected boolean isBomb() {
        return tile.equals(TILE_BOMB);
    }

    /**
     * Transforma o quadrado em uma bomba
     */
    protected void toBomb() {
        tile = TILE_BOMB;
    }

    /**
     * Altera a imagem exibida no quadrado
     *
     * @param tileShowing nova imagem a ser exibida
     */
    protected void setTileShowing(ImageIcon tileShowing) {
        this.tileShowing = tileShowing;
    }

    /**
     * Calcula a quantidade de bombas ao redor do quadrado e atualiza a imagem
     * com a imagem correspondente à quantidade de bombas.
     */
    protected void calculateAmountBombs() {
        if (!isBomb()) {
            bombsAmount = (int) tiles.getAround(this).stream()
                    .filter((t2) -> (t2.isBomb())).count();
            tile = getTileAmountBombs(bombsAmount);
        }
    }

    /**
     * Encerra a partida
     *
     * @param win <i>true</i> se houve vitória. <i>false</i> se uma bomba foi
     * clicada.
     */
    protected void gameOver(boolean win) {
        if (win) {
            if (isBomb()) {
                tileShowing = TILE_FLAG;
                setIcon(tileShowing);
            }
        } else {
            tileShowing = tile;
            setIcon(tile);
        }
        blocked = true;
    }

    /**
     * Verifica se o quadrado tem uma bandeira de marcação de bomba
     *
     * @return <i>true</i> ou <i>false</i>
     */
    protected boolean isFlag() {
        return tileShowing.equals(TILE_FLAG);
    }

    /**
     * Verifica se o quadrado já foi clicado
     *
     * @return <i>true</i> ou <i>false</i>
     */
    protected boolean isClicked() {
        return !tilesClickR.contains(tileShowing);
    }

    /**
     * Gera um evento de clique para o quadrado
     *
     * @param e MouseEvent
     */
    protected void click(MouseEvent e) {
        if (!blocked) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                clickLeft();
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                clickRight();
            }
            setIcon(tileShowing);
        }
    }

    /**
     * Altera a imagem do quadrado para empty (imagem exibida quando o quadrado
     * está sendo clicado)
     *
     * @param flag <i>true</i> para ativar. <i>false</i> o contrário.
     */
    protected void clicking(boolean flag) {
        if (!blocked && !isClicked() && !isFlag()) {
            setIcon(flag ? TILE_EMPTY : tileShowing);
        }
    }

    /**
     * Executa o evento ao clicar no quadrado com os dois botões do mouse ao
     * mesmo tempo.<br>
     * Clica em todos os quadrados ao redor deste, exceto os que estão marcados
     * com bandeira.<br>
     * O evento só será executado caso a quantidade de quadrados ao redor
     * marcados com bandeira seja igual à quantidade de bombas ao redor.
     */
    protected void clickAround() {
        if (isClicked()) {
            List<Tile> arounds = tiles.getAround(this);
            int flagsAround = tiles.getAround(this).stream()
                    .filter((t2) -> (t2.isFlag()))
                    .map((_item) -> 1).reduce(0, Integer::sum);
            if (flagsAround == bombsAmount) {
                arounds.stream().filter((t) -> (!t.blocked)).forEach((t) -> {
                    t.clickLeft();
                });
            }
        }
    }
}
