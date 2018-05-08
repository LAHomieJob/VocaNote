package com.hfad.vocanoteapp.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;


@Database(entities = {VocaNote.class, GroupVc.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    private static RoomDatabase.Callback sAppDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    public synchronized static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database")
                            .addCallback(sAppDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract VocaNoteDao vocaNoteDao();

    public abstract GroupVcDao groupVcDao();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final GroupVcDao mGroupVcDao;
        private final VocaNoteDao mVocaNoteDao;

        PopulateDbAsync(AppDatabase db) {

            mGroupVcDao = db.groupVcDao();
            mVocaNoteDao = db.vocaNoteDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //Initialization of groups for 5 languages
            GroupVc groupVc = new GroupVc("English", "Hello");
            mGroupVcDao.insert(groupVc);
            groupVc = new GroupVc("Spanish", "Hola");
            mGroupVcDao.insert(groupVc);
            groupVc = new GroupVc("French", "Bonjour");
            mGroupVcDao.insert(groupVc);
            groupVc = new GroupVc("Italian", "Buon giorno");
            mGroupVcDao.insert(groupVc);
            groupVc = new GroupVc("German", "Guten tag");
            mGroupVcDao.insert(groupVc);
            //Initialization of VocaNotes for Hello group
            VocaNote wordEng = new VocaNote("Car", "Машина", "Hello");
            mVocaNoteDao.insert(wordEng);
            wordEng = new VocaNote("Human", "Человек", "Hello");
            mVocaNoteDao.insert(wordEng);
            wordEng = new VocaNote("Stupid", "Глупый", "Hello");
            mVocaNoteDao.insert(wordEng);
            wordEng = new VocaNote("To invoke", "Вызывать", "Hello");
            mVocaNoteDao.insert(wordEng);
            wordEng = new VocaNote("To bring", "Приносить", "Hello");
            mVocaNoteDao.insert(wordEng);
            //Initialization of VocaNotes for Hola group
            VocaNote wordEsp = new VocaNote("Mujer", "Женщина", "Hola");
            mVocaNoteDao.insert(wordEsp);
            wordEsp = new VocaNote("Haber", "Иметь", "Hola");
            mVocaNoteDao.insert(wordEsp);
            wordEsp = new VocaNote("Estupendo", "Великолепный", "Hola");
            mVocaNoteDao.insert(wordEsp);
            wordEsp = new VocaNote("Dejar", "Покидать", "Hola");
            mVocaNoteDao.insert(wordEsp);
            wordEsp = new VocaNote("Llevar", "Приносить", "Hola");
            mVocaNoteDao.insert(wordEsp);
            //Initialization of VocaNotes for Bonjour group
            VocaNote wordFr = new VocaNote("Merci", "Спасибо", "Bonjour");
            mVocaNoteDao.insert(wordFr);
            wordFr = new VocaNote("De rien", "Не за что", "Bonjour");
            mVocaNoteDao.insert(wordFr);
            wordFr = new VocaNote("Place", "Площадь", "Bonjour");
            mVocaNoteDao.insert(wordFr);
            wordFr = new VocaNote("A droite", "Направо", "Bonjour");
            mVocaNoteDao.insert(wordFr);
            wordFr = new VocaNote("Bus", "Автобус", "Bonjour");
            mVocaNoteDao.insert(wordFr);
            //Initialization of VocaNotes for Buon giorno group
            VocaNote wordIt = new VocaNote("Mediocre", "Посредственный", "Buon giorno");
            mVocaNoteDao.insert(wordIt);
            wordIt = new VocaNote("Candela", "Свеча", "Buon giorno");
            mVocaNoteDao.insert(wordIt);
            wordIt = new VocaNote("Risata", "Смех", "Buon giorno");
            mVocaNoteDao.insert(wordIt);
            wordIt = new VocaNote("Sigillo", "Печать", "Buon giorno");
            mVocaNoteDao.insert(wordIt);
            wordIt = new VocaNote("Cassa", "Ящик", "Buon giorno");
            mVocaNoteDao.insert(wordIt);
            //Initialization of VocaNotes for Guten tag group
            VocaNote wordGer = new VocaNote("Anfrage", "Запрос", "Guten tag");
            mVocaNoteDao.insert(wordGer);
            wordGer = new VocaNote("Haufen", "Куча", "Guten tag");
            mVocaNoteDao.insert(wordGer);
            wordGer = new VocaNote("Geistig", "Духовный", "Guten tag");
            mVocaNoteDao.insert(wordGer);
            wordGer = new VocaNote("Festlegen", "Устанавливать", "Guten tag");
            mVocaNoteDao.insert(wordGer);
            wordGer = new VocaNote("Senden", "Посылать", "Guten tag");
            mVocaNoteDao.insert(wordGer);
            return null;
        }
    }


}
