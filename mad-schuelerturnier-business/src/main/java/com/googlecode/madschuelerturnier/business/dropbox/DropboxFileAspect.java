/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.dropbox;

import com.googlecode.madschuelerturnier.model.support.File;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Aspekt zum sichern der File Attachements in der Dropbox
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Aspect
@Component
public class DropboxFileAspect {

    @Autowired
    private DropboxConnector dropbox;

    @After("execution(* com.googlecode.madschuelerturnier.persistence.repository.FileRepository.save(..))")
    public void logAfter(JoinPoint joinPoint) {

        for (Object obj : joinPoint.getArgs()) {
            if (obj instanceof File) {
                File f = (File) obj;
                if(f.getContent() != null && f.getContent().length > 0){
                    saveToDropbox(f);
                }
            }

        }
    }

    @Before("execution(* com.googlecode.madschuelerturnier.persistence.repository.FileRepository.save(..))")
    public void logBefore(JoinPoint joinPoint) {

        for (Object obj : joinPoint.getArgs()) {
            if (obj instanceof File) {
                File f = (File) obj;

                if(f.getContent() == null || f.getContent().length <1){
                    byte[] content =loadFromDropbox(f);
                    if(content != null && content.length >0 ){
                    f.setContent(content);
                    }
                }
            }
        }
    }

    private void saveToDropbox(File f) {
        dropbox.saveGameAttachemt(f.getTyp() + "_" + f.getPearID() + "."+f.getSuffix(), f.getContent());
    }

    private byte[] loadFromDropbox(File f) {
        return dropbox.loadGameAttachemt(f.getTyp() + "_" + f.getPearID() + "."+f.getSuffix());
    }

}