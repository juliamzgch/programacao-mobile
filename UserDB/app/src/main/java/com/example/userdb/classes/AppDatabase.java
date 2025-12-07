package com.example.userdb.classes;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Playlist.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase databaseInstance = null;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static AppDatabase getInstance(Context context){
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(
                    context.getApplicationContext(), AppDatabase.class, "UsersDB")
            .addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    executorService.execute(() -> {
                        getInstance(context).userDao().insert(
                                new User("Pedro", "pedro@email.com", "12345"),
                                new User("Rui", "rui@email.com", "54321")
                        );
                        for(User user : getInstance(context).userDao().getAll()) {
                            getInstance(context).playlistDao().insert(
                                    new Playlist(user.getId(), "Playlist A:"+user.getId()),
                                    new Playlist(user.getId(), "Playlist B:"+user.getId()),
                                    new Playlist(user.getId(), "Playlist C:"+user.getId())
                            );
                        }
                    });
                }
                @Override
                public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                    super.onDestructiveMigration(db);
                    onCreate(db);
                }
            }).fallbackToDestructiveMigration().build();

        }
        return databaseInstance;
    }

    public abstract UserDao userDao();
    public abstract PlaylistDao playlistDao();
}
