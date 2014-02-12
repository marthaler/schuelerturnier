/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.dropbox;

import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public interface DropboxConnector {

    Boolean isConnected();

    String getLoginURL();

    void insertToken(String token);

    List<String> getFilesInFolder();

    List<String> getFilesInAltFolder();

    byte[] loadFile(String file);

    void saveFile(String file, byte[] content);

    byte[] selectGame(String folder);

    List<String> getAllGames();

    String getSelectedGame();

    byte[] loadGameAttachemt(String file);

    void saveGameAttachemt(String file, byte[] content);

    void saveGame(byte[] content);

}
