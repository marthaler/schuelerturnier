/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.business.utils.MavenVersionUtil;
import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.web.security.DoLoginController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Bereitet das Model fuer die Login Seite auf
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
public class LoginController {

    @Autowired
    MavenVersionUtil version;

    @Autowired
    DoLoginController controler;

    @RequestMapping("/login")
    public String loginPrepare(Model uimodel) {

        uimodel.addAttribute("mavenVersionUtil", version);
        uimodel.addAttribute("loginController2", controler);
        DBAuthUser user;

        return "login";
    }

}
