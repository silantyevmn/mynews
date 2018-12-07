package silantyevmn.ru.mynews.ui.adapter;


import java.util.List;

import silantyevmn.ru.mynews.model.entity.Articles;

public interface IAdapter {
    List<Articles> getArticlesList();

    void startWebView(Articles articles);
}
