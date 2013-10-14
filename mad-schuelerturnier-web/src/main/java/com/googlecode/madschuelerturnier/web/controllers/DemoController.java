package com.googlecode.madschuelerturnier.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.googlecode.madschuelerturnier.web.controllers.util.*;

import java.util.ArrayList;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Controller
public class DemoController  {

    @RequestMapping("/halloc")
    public String helloWorld(HalloBean bean,Model uimodel){
    List<HalloBean> beans = new ArrayList<HalloBean>();
    beans.add(bean);

    uimodel.addAttribute("msg","Meine Nachricht en");
        uimodel.addAttribute("beans",beans);

        return "controllers/hallo";
    }

}
