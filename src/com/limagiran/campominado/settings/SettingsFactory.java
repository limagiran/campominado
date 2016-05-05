package com.limagiran.campominado.settings;

import com.limagiran.util.RWObj;
import java.io.File;
import static java.io.File.separator;

/**
 *
 * @author Vinicius
 */
public class SettingsFactory {

    private static final File FILE;
    private static final String PASSWORD = "&p4ssS3cm";

    static {
        FILE = new File(System.getProperty("user.home") + separator
                + "Campo Minado" + separator + "set.dat");
        if (FILE.getParentFile() != null) {
            FILE.getParentFile().mkdirs();
        }
    }

    /**
     * Criar a instância Settings
     *
     * @return Settings salvo no computador ou as configurações padrões
     */
    public static Settings getInstance() {
        Settings _return = RWObj.lerObjeto(FILE, Settings.class, PASSWORD);
        return ((_return != null) ? _return : new Settings());
    }

    /**
     * Salvar as configurações atuais
     *
     * @param s Settings com as configurações
     */
    public static void salvar(Settings s) {
        RWObj.gravarObjeto(s, FILE, PASSWORD);
    }
}
