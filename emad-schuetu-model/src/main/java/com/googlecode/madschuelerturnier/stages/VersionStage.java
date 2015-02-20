/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */
package com.googlecode.madschuelerturnier.stages;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

/**
 * Zeigt an, dass sich die Applikation in einer Integrationsumgebung befindet
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.0.1
 */
@Controller
@Profile("vers")
public class VersionStage extends Stage{

    public StageEnum getStage(){
        return StageEnum.VERSION;
    }

}
