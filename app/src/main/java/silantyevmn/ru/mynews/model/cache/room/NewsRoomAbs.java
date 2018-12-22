package silantyevmn.ru.mynews.model.cache.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import silantyevmn.ru.mynews.model.entity.Articles;


@Database(entities = {Articles.class}, version = 1, exportSchema = false)
public abstract class NewsRoomAbs extends RoomDatabase {
    public abstract NewsRoomDao photoDao();
}
