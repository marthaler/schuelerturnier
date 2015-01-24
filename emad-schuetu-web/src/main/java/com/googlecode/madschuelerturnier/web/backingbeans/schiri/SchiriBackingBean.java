/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.backingbeans.schiri;

import com.googlecode.madschuelerturnier.business.spieldurchfuehrung.SpielDurchfuehrung;
import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Schiri;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.repository.DBAuthUserRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import com.googlecode.madschuelerturnier.util.Colors;
import com.googlecode.madschuelerturnier.web.security.DoLoginController;
import com.googlecode.madschuelerturnier.web.security.LoginBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Session basiertes Backing Bean für die mobile Schiri-Sicht
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Component
@Scope("session")
public class SchiriBackingBean implements Serializable {

    private boolean wechsel = false;

    Colors colors = new Colors();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count;

    private int c = 0;

    @Autowired
    SpielDurchfuehrung durchfuehrung;

    @Autowired
    private DoLoginController loginC;

    @Autowired
    private DBAuthUserRepository repo;

    @Autowired
    private SpielRepository spielRepo;

    public Spiel getGame() {
        return game;
    }

    private Spiel game;

    public SchiriBackingBean() {

    }

    @PostConstruct
    public void init() {
        DBAuthUser schiri = repo.findByUsername("schiri");
        if (schiri == null) {
            schiri = new Schiri();
        }

        schiri.setUsername("schiri");
        schiri.changeUsersPassword("schiri");
        Schiri schir = (Schiri) schiri;
        schir.setAktiviert(true);
        repo.save(schir);
    }

    public void coun(){
        count ++;
    }

    public int getC(){
        c++;
                return c;
    }

    private String user;
    private String password;

    private LoginBean login = new LoginBean();

    private Spiel spiel = null;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() {

        login.setUser(this.user);
        login.setPassword(this.password);
        loginC.login(login);

        if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

            if(user == null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anmeldung nicht erfolgreich Benutzer nicht gefunden", ""));
                return;
            }

            if(!user.isAuthenticated()){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Benutzer ist nicht angemeldet", ""));
            }
        }
    }

    public boolean isSchiriAktivated() {

        if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {

            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            if (user != null && user.getPrincipal() instanceof Schiri) {
                Schiri schir = (Schiri) user.getPrincipal();
                if (schir.isAktiviert()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Schiri getLoggedInSchiri() {

        if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {

            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            if (user != null && user.getPrincipal() instanceof Schiri) {
                Schiri schiri = (Schiri) user.getPrincipal();
                return schiri;
            }
        }
        return null;
    }

    public List<Spiel> getNext() {
        List<Spiel> ret = new ArrayList<Spiel>();
        List<SpielZeile> vorb = new ArrayList<SpielZeile>();
        vorb.addAll(durchfuehrung.getList3Vorbereitet());
        vorb.addAll(durchfuehrung.getList2ZumVorbereiten());
        for (SpielZeile zeile : vorb) {
            ret.add(zeile.getA());
            ret.add(zeile.getB());
            ret.add(zeile.getC());
        }
        return ret;
    }

    public boolean showLogin() {
        if (getLoggedInSchiri() == null) {
            return true;
        }
        return false;
    }

    public boolean showSchiriNotActivated() {
        if (getLoggedInSchiri() != null) {
            if (!getLoggedInSchiri().isAktiviert()) {
                return true;
            }
        }
        return false;
    }

    public boolean showSpielWahl() {
        if (spiel == null && !showSchiriNotActivated() && !showLogin()) {
            return true;
        }
        return false;
    }

    public boolean showSpiel() {
        if (spiel != null && !showSchiriNotActivated() && !showLogin()) {
            return true;
        }
        return false;
    }

    public String getTitel() {
        List<Spiel> sp = getNext();
        if (sp.isEmpty()) {
            return "Im Moment keine Spiele zur Auswahl";
        }
        SimpleDateFormat fm = new SimpleDateFormat("HH:mm");
        return "Spiele für " + fm.format(sp.get(0).getStart());
    }

    public void selectGame(String id){
        game = spielRepo.findOne(Long.parseLong(id));
    }

    public void colorchange(String id){
        if(id.equals("eins")){
            getMannschaftEins().setColor(this.colors.getNextFarbe(getMannschaftEins().getColor()));
        } else {
            getMannschaftZwei().setColor(this.colors.getNextFarbe(getMannschaftZwei().getColor()));
        }
    }

    public Mannschaft getMannschaftEins(){

        if(wechsel){
            return game.getMannschaftB();
        }
        return game.getMannschaftA();
    }

    public Mannschaft getMannschaftZwei(){
        if(wechsel){
            return game.getMannschaftA();
        }
        return game.getMannschaftB();
    }

    public void wechseln(){
        this.wechsel = !wechsel ;
    }

    public String getColorEins(){
        return colors.getColor(getMannschaftEins().getColor());
    }

    public String getColorZwei(){
        return colors.getColor(getMannschaftZwei().getColor());
    }

    public String getBColorEins(){
        return colors.getBackground(getMannschaftEins().getColor());
    }

    public String getBColorZwei(){
        return colors.getBackground(getMannschaftZwei().getColor());
    }

}