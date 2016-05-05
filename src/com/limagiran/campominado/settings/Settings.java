package com.limagiran.campominado.settings;

import static com.limagiran.campominado.settings.Settings.EnumMode.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Vinicius
 */
public class Settings implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 0;
    private static final int HEIGHT = 1;
    private static final int BOMBS = 2;

    public static final Settings set;

    private EnumMode mode;
    private Map<EnumMode, String> recordName;
    private Map<EnumMode, Integer> record;
    private Map<Integer, Integer> lastCustom;

    protected Settings() {
        mode = EnumMode.MEDIUM;
        record = new HashMap<>();
        recordName = new HashMap<>();
        lastCustom = new HashMap<>();
        lastCustom.put(WIDTH, 10);
        lastCustom.put(HEIGHT, 10);
        lastCustom.put(BOMBS, 10);
        save();
    }

    static {
        set = SettingsFactory.getInstance();
        if (set.getMode().equals(CUSTOM)) {
            CUSTOM.setWidth(set.lastCustom.getOrDefault(WIDTH, 10));
            CUSTOM.setHeight(set.lastCustom.getOrDefault(HEIGHT, 10));
            CUSTOM.setBombs(set.lastCustom.getOrDefault(BOMBS, 10));
        }
    }

    public static void save() {
        SettingsFactory.salvar(set);
    }

    /**
     * Modo de jogo
     *
     * @return Modo de jogo
     */
    public EnumMode getMode() {
        return mode;
    }

    /**
     * Altera o modo de jogo
     *
     * @param mode novo modo de jogo
     */
    public void setMode(EnumMode mode) {
        this.mode = mode;
        if (this.mode.equals(CUSTOM)) {
            lastCustom.put(WIDTH, this.mode.width);
            lastCustom.put(HEIGHT, this.mode.height);
            lastCustom.put(BOMBS, this.mode.getBombs());
        }
        save();
    }

    /**
     * Retorna o recorde (menor tempo)
     *
     * @param mode modo de jogo
     * @return recorde no formato String ou "..." para recorde não registrado
     */
    public String getRecord(EnumMode mode) {
        return ((record.get(mode) == null) ? "..." : String.valueOf(record.get(mode)));
    }

    /**
     * Retorna o nome do dono do recorde (menor tempo)
     *
     * @param mode modo de jogo
     * @return nome do dono ou "..." para recorde não registrado
     */
    public String getRecordName(EnumMode mode) {
        return recordName.getOrDefault(mode, "...");
    }

    /**
     * Verifica se a pontuação é um recorde. Se sim, pede para inserir o nome.
     *
     * @param points pontuação feita na partida atual.
     */
    public void checkRecord(int points) {
        Integer actualPoints = record.get(mode);
        if ((actualPoints == null) || (points < actualPoints)) {
            recordName.put(mode, newRecordGetName());
            record.put(mode, points);
        }
        save();
    }

    /**
     * Abre uma caixa de diálogo para capturar o nome de quem fez o recorde
     *
     * @return nome do jogador.
     */
    private String newRecordGetName() {
        String name = null;
        while((name == null) || (name.isEmpty())){
            name = JOptionPane.showInputDialog("Parabéns! Novo Recorde!\nInsira seu nome:");
        }
        return name;
    }

    /**
     * Modos de jogo, EASY, MEDIUM, HARD e CUSTOM
     */
    public static enum EnumMode {
        EASY(10, 10, 10), MEDIUM(16, 16, 40), HARD(30, 16, 99), CUSTOM(10, 10, 10);
        
        public final int width;
        public final int height;
        private final int bombs;

        /**
         * Nova instância do modo de jogo
         *
         * @param width largura
         * @param height altura
         * @param bombs quantidade de bombas
         */
        private EnumMode(int width, int height, int bombs) {
            this.width = width;
            this.height = height;
            this.bombs = bombs;
        }

        /**
         * Quantidade de bombas
         *
         * @return quantidade de bombas no limite de 90% do total de casas
         */
        public int getBombs() {
            int lim = (int) ((height * width) * 0.9);
            return ((bombs <= lim) ? bombs : lim);
        }

        /**
         * Alterar uma variável do modo CUSTOM através de reflection
         *
         * @param value novo valor
         * @param fieldName nome da variável (width, height, bombs)
         */
        private void set(Integer value, String fieldName) {
            if (equals(CUSTOM) && (value != null) && (value > 0)) {
                try {
                    Field f = getClass().getDeclaredField(fieldName);
                    f.setAccessible(true);
                    f.set(this, value);
                    save();
                } catch (Exception ignore) {
                }
            }
        }

        /**
         * Alterar a largura do modo CUSTOM
         *
         * @param width nova largura
         */
        public void setWidth(Integer width) {
            set(width, "width");
        }

        /**
         * Alterar a altura do modo CUSTOM
         *
         * @param height nova largura
         */
        public void setHeight(Integer height) {
            set(height, "height");
        }

        /**
         * Alterar a quantidade de bombas do modo CUSTOM
         *
         * @param bombs nova quantidade de bombas
         */
        public void setBombs(Integer bombs) {
            set(bombs, "bombs");
        }
    }
}
