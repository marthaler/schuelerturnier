/**
 * Copyright (C) eMad, 2014.
 */
package ch.emad.business.schuetu.dropbox;

import ch.emad.model.common.model.File;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger LOG = Logger.getLogger(DropboxFileAspect.class);

    @Autowired
    private DropboxFileAsyncBean dropbox;

    @Before("execution(* ch.emad.persistence.common.FileRepository.save(..))")
    public void saveFile(JoinPoint joinPoint) {
        try {
            for (Object obj : joinPoint.getArgs()) {
                if (obj instanceof File) {
                    File f = (File) obj;

                    if (f.getContent() == null || f.getContent().length < 1) {
                        byte[] content = dropbox.loadFromDropbox(f);
                        if (content != null && content.length > 0) {
                            f.setContent(content);
                        }
                    } else {
                        dropbox.saveToDropbox(f);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @After("execution(* ch.emad.persistence.common.FileRepository.delete(..))")
    public void deleteFile(JoinPoint joinPoint) {
        try {
            for (Object obj : joinPoint.getArgs()) {
                if (obj instanceof File) {
                    File f = (File) obj;
                    if (f.getContent() != null && f.getContent().length > 0) {
                        dropbox.deleteFromDropbox(f);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}