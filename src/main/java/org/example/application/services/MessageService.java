package org.example.application.services;

import org.example.application.entity.Message;
import org.example.application.repositories.MessageRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.*;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    private int minCount = Integer.MAX_VALUE;
    private int maxCount = 0;
    private int tempCount = 0;

    public List<Message> getAllRef(String mainUrl){
        List<Message> messages = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(mainUrl.trim()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.select("a:not(.mw-jump-link)");

        for (Element element : elements) {
            if (element.ownText().equals(""))
                continue;
            String absUrl = element.absUrl("href");
            tempCount = getCountSymbols(absUrl);
            if (tempCount < 1000)
                continue;
            maxCount = maxCount < tempCount ? tempCount : maxCount;
            minCount = minCount > tempCount ? tempCount : minCount;
            Message message = new Message();
            message.setCount(tempCount);
            message.setUrl(absUrl);
            messageRepository.saveAndFlush(message);
            messages.add(message);
        }
        return messages;
    }

    public int getCountSymbols(String url){
        Document tempDoc = null;
        try {
            tempDoc = Jsoup.connect(url.trim()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements allElements = tempDoc.getAllElements();
        StringBuilder s = new StringBuilder("");

        for (Element allElement : allElements) {
            s.append(allElement.ownText().replaceAll("[^a-zA-Z]",""));
        }
        return s.length();
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
