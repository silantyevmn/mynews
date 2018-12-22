package silantyevmn.ru.mynews.model.cache.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import silantyevmn.ru.mynews.model.entity.Articles;


@Dao
public interface NewsRoomDao {
    @Query("SELECT * FROM bookmarks")
    List<Articles> getList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Articles articles);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Articles> list);

    @Update
    void update(Articles articles);

    @Delete
    void delete(Articles articles);

    @Query("SELECT * FROM bookmarks WHERE id = :id")
    Articles getById(int id);

}
