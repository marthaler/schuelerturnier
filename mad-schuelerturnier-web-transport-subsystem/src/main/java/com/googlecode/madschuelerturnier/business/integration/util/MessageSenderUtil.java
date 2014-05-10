package com.googlecode.madschuelerturnier.business.integration.util;

import com.googlecode.madschuelerturnier.model.integration.MessageWrapperToSend;
import com.googlecode.madschuelerturnier.model.util.XstreamUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MessageSenderUtil {

    private static final Logger LOG = Logger.getLogger(MessageSenderUtil.class);

    private static int failurecounter = 0;
    public static MessageWrapperToSend send(String adresse, MessageWrapperToSend obj) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(adresse);
        StringBuffer buff = new StringBuffer();
        BufferedReader rd;
        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("payload", XstreamUtil.serializeToString(obj)));

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                buff.append(line);
            }

            return (MessageWrapperToSend) XstreamUtil.deserializeFromString(buff.toString());
        } catch (IOException e) {
            failurecounter++;
            if(failurecounter % 100 == 0 ){
                LOG.error(e.getMessage(),e);
            }
        }
        return null;
    }
}