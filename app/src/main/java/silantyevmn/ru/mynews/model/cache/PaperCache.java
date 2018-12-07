package silantyevmn.ru.mynews.model.cache;


import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.entity.News;


public class PaperCache implements Cache{
    private final String BASE_KEY="paper_key";

    @Override
    public void putAll(News news) {
        Paper.book().write(BASE_KEY,news);
    }

    @Override
    public Observable<News> getNews() {
        return Observable.create(e->{
            News news= Paper.book().read(BASE_KEY);
            if(news==null){
                news=new News("","",new ArrayList<>());
            }
            e.onNext(news);
            e.onComplete();
        });
    }
}
