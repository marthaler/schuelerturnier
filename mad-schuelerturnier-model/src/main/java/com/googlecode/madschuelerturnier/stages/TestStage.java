/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */
package com.googlecode.madschuelerturnier.stages;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

/**
 * Zeigt an, dass sich die Applikation in einer Testumgebung befindet
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.0.1
 */
@Controller
@Profile("test")
public class TestStage extends Stage{

    public StageEnum getStage(){
        return StageEnum.TEST;
    }

}
