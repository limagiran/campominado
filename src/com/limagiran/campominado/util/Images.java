package com.limagiran.campominado.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import static java.awt.RenderingHints.*;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Vinicius Silva
 */
public class Images {

    private static final int SCALE = 20;
    private static final String RES = "/com/limagiran/campominado/resources/img/";
    public static final ImageIcon SMILE = getImgRes("smile.png");
    public static final ImageIcon SMILE_GLASSES = getImgRes("smileGlass.png");
    public static final ImageIcon SMILE_DEAD = getImgRes("smileDead.png");
    public static final BufferedImage TILES = getTiles();
    public static final ImageIcon TILE_DEFAULT = getTile(0, 0);
    public static final ImageIcon TILE_FLAG = getTile(0, 1);
    public static final ImageIcon TILE_BOMB = getTile(0, 2);
    public static final ImageIcon TILE_EMPTY = getTile(0, 3);
    public static final ImageIcon TILE_QUESTION = getTileQuestion();
    public static final ImageIcon TILE_ONE = getTile(1, 0);
    public static final ImageIcon TILE_TWO = getTile(1, 1);
    public static final ImageIcon TILE_THREE = getTile(1, 2);
    public static final ImageIcon TILE_FOUR = getTile(1, 3);
    public static final ImageIcon TILE_FIVE = getTile(2, 0);
    public static final ImageIcon TILE_SIX = getTile(2, 1);
    public static final ImageIcon TILE_SEVEN = getTile(2, 2);
    public static final ImageIcon TILE_EIGHT = getTile(2, 3);

    /**
     * Carrega uma imagem do pacote "/com/limagiran/campominado/resources/img/"
     *
     * @param res nome da imagem no pacote, incluído a extensão
     * @return ImageIcon da imagem
     */
    private static ImageIcon getImgRes(String res) {
        return new ImageIcon(Images.class.getResource(RES + res));
    }

    /**
     * Carrega a imagem Sprite original com todas as imagens utilizadas no campo
     * de jogo. (bombas, campo vazio, quantidade de bombas ao redor, etc...)
     *
     * @return BufferedImage com a imagem carregada
     */
    private static BufferedImage getTiles() {
        try {
            return ImageIO.read(Images.class.getResourceAsStream(RES + "tiles.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao carregar imagens.");
            System.exit(0);
            return null;
        }
    }

    /**
     * Retorna a imagem Sprite na posição indicada nos parâmetros
     *
     * @param y linha onde a imagem se encontra
     * @param x coluna onde a imagem se encontra
     * @return ImageIcon com a imagem retirada do Sprite
     */
    private static ImageIcon getTile(int y, int x) {
        BufferedImage img = new BufferedImage(SCALE, SCALE, TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        setupGraphics(g);
        g.drawImage(TILES.getSubimage((x * 128), (y * 128), 128, 128), 0, 0, SCALE, SCALE, null);
        g.dispose();
        return new ImageIcon(img);
    }

    /**
     * Retorna a imagem referente à quantidade de bombas ao redor
     *
     * @param amount quantidade de bombas
     * @return ImageIcon com a imagem
     */
    public static ImageIcon getTileAmountBombs(int amount) {
        switch (amount) {
            case 0:
                return TILE_EMPTY;
            case 1:
                return TILE_ONE;
            case 2:
                return TILE_TWO;
            case 3:
                return TILE_THREE;
            case 4:
                return TILE_FOUR;
            case 5:
                return TILE_FIVE;
            case 6:
                return TILE_SIX;
            case 7:
                return TILE_SEVEN;
            case 8:
                return TILE_EIGHT;
            default:
                return TILE_EMPTY;
        }
    }

    /**
     * Configura a renderização padrão para o contexto gráfico
     *
     * @param g2 contexto gráfico
     */
    private static void setupGraphics(Graphics2D g) {
        g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
        g.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
    }

    /**
     * Cria uma imagem para ser utilizada como ponto de interrogação.
     *
     * @return ImageIcon com a imagem criada
     */
    private static ImageIcon getTileQuestion() {
        BufferedImage img = new BufferedImage(20, 20, TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        setupGraphics(g);
        g.drawImage(TILES.getSubimage(0, 0, 128, 128), 0, 0, 20, 20, null);
        g.setFont(new Font("Tahoma", Font.BOLD, 14));
        g.setColor(Color.GRAY.darker());
        g.drawString("?", 6, 15);
        g.dispose();
        return new ImageIcon(img);
    }
}
