/*
 * Copyright (C) eMad, 2015.
 */
package ch.emad.business.schuetu;

import ch.emad.business.common.properties.Prop;
import ch.emad.model.common.model.DBAuthUser;
import ch.emad.model.common.model.properties.PropModel;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Satz von Properties fuer das Framework, kann in einer Prop Impl mit einem tieferen order Wert ueberschrieben werden
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.0.1
 */

@Controller
public class PropAppImpl implements Prop {

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public Map<String, PropModel> getMap() {
        List<PropModel> list = new ArrayList<PropModel>();

        list.add(new PropModel("dropbox.root", "/shared/test", "Root Verzeichnis in der Dropbox"));
        list.add(new PropModel("dropbox.localroot", "D:/temp", "Root der lokalen Repoablage fuer Test"));
        list.add(new PropModel("dropbox.rmi", "rmi://87.230.15.247:4199/DropboxConnectorRemote", "RMI RL der Dropbox"));

        list.add(new PropModel("db.backup", Boolean.FALSE, "Einschalten des Dropox DB Backups"));
        list.add(new PropModel("db.backup.aenderungszahl", 300, "Alle x Aenderugen wird ein Backup durchgefuehrt"));
        list.add(new PropModel("db.backup.localfolder", "D:/temp/db", "Lokales DB File"));
        list.add(new PropModel("db.backup.remotefolder", "/db2/", "Backup Ordner in der Dropbox"));
        list.add(new PropModel("db.backup.dbname", "change.me", "Name der DB"));

        list.add(new PropModel("security.allerollen", "root,admin", "Alle moeglichen Rollen der App"));
        list.add(new PropModel("security.dbauthuser", "ch.emad.model.common.model.DBAuthUser", "DBAuthUser fuerdie Factory"));


        list.add(new PropModel("layout.show.buildtime", Boolean.TRUE, "Buildtime anzeigen im Layout"));
        list.add(new PropModel("layout.show.registration", Boolean.FALSE, "Zeigt an ob der Menüpunkt zum registrieren beim Login angezeigt werden soll oder nicht"));

        list.add(new PropModel("email.hostname", "smtp.gmail.com", "Host zum Mails senden"));
        list.add(new PropModel("email.password", "springwebflow", "Lustiges Wort des Mailkontos"));

        list.add(new PropModel("app.show.jahreswechsel", Boolean.FALSE, "Zeigt an ob der Menüpunkt zum Jahreswechsel erscheinen soll oder nicht"));

        list.add(new PropModel("appname", "schuetu", "Name der Applikation"));

        list.add(new PropModel("h2webserveron", Boolean.FALSE, "Startet den H2 Webserver"));
        list.add(new PropModel("h2webserverport", 8888, "Port des H2 Webservers"));

        list.add(new PropModel("defaultstring", "defaultstring", "Default String Propertie"));
        list.add(new PropModel("defaultboolean", Boolean.TRUE, "Default Boolean Propertie"));
        list.add(new PropModel("defaultinteger", 1, "Default Integer Propertie"));
        list.add(new PropModel("dbauthuser", DBAuthUser.class, "Default DBAuthUser, welcher in der App erstellt werden soll"));

        Map<String, PropModel> map = new HashMap<String, PropModel>();
        for (PropModel temp : list) {
            map.put(temp.getKey(), temp);
        }
        return map;
    }

}
