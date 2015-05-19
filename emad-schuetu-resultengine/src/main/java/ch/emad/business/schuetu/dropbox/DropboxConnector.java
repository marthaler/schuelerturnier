

/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.dropbox;

import java.util.List;
import java.util.Map;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public interface DropboxConnector {

    Boolean isConnected();

    String getLoginURL();

    void insertToken(String token);

    @Deprecated
    List<String> getFilesInFolder();

    List<String> getFilesInFolder(String folder);

    List<String> getFilesInAltFolder();

    byte[] loadFile(String file);

    void saveFile(String file, byte[] content);

    byte[] selectGame(String folder);

    List<String> getAllGames();

    String getSelectedGame();

    byte[] loadGameAttachemt(String file,String suffix);

    void saveGameAttachemt(String file, String suffix, byte[] content);

    void deleteGameAttachemt(String file,String suffix);

    void saveGame(byte[] content);

    void saveOldGame(String jahr,String content);

    Map<String,String> loadOldGames();

    void deleteFile(String file);

    String getDescription();

}
