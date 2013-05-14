package com.googlecode.mad_schuelerturnier.web;

import com.googlecode.mad_schuelerturnier.business.dataloader.CVSMannschaftParser;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import java.io.IOException;
import java.util.List;

@Component
public class FileUploadAction implements Action {

    private static final Logger LOG = Logger.getLogger(FileUploadAction.class);

    @Autowired
    private CVSMannschaftParser man;

    @Autowired
    private MannschaftRepository repo;

    public Event execute(RequestContext context) throws IOException {
        LOG.info("reest");
        AttributeMap map = context.getRequestParameters().asAttributeMap();
        String text = map.getString("form1:text");
        List<Mannschaft> manList = man.parseFileContent(text);
        repo.save(manList);
        LOG.info("text: " + text);
        return new Event(this, "mannschaftslisteImport");


        //HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getNativeRequest();
        //request.getServletPath()

    }
}