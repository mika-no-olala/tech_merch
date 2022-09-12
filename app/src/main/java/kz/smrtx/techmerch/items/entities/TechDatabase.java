package kz.smrtx.techmerch.items.entities;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import kz.smrtx.techmerch.items.dao.ChoosePointsDao;
import kz.smrtx.techmerch.items.dao.ElementDao;
import kz.smrtx.techmerch.items.dao.RequestDao;
import kz.smrtx.techmerch.items.dao.SalePointDao;
import kz.smrtx.techmerch.items.dao.UserDao;
import kz.smrtx.techmerch.items.dao.VisitDao;

@Database(entities = {Visit.class,
        SalePoint.class,
        SalePointFts.class,
        User.class,
        Element.class,
        Request.class}, version = 1)
public abstract class TechDatabase extends RoomDatabase {

    private static TechDatabase instance;

    public abstract VisitDao visitDao();
    public abstract SalePointDao salePointDao();
    public abstract ChoosePointsDao choosePointsDao();
    public abstract UserDao userDao();
    public abstract ElementDao elementDao();
    public abstract RequestDao requestDao();

    public static synchronized TechDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TechDatabase.class, "tech_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
