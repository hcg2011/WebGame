package com.prize.webgame.db;

import android.os.Environment;
import android.util.Log;

import com.prize.webgame.bean.GameBean;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.List;


public class DBMediaHelper
{

    private String TAG = "DBMediaHelper";

    private DbManager db = null;
    private String dbDir = Environment.getExternalStorageDirectory() + "/prize/db/";
    private String dbName = "web_game.db";

    private static DBMediaHelper INSTANCE = null;

    public static DBMediaHelper getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new DBMediaHelper();
        return INSTANCE;
    }

    public DBMediaHelper()
    {
        File f = new File(dbDir);
        if(!f.exists())
            f.mkdirs();

        initDb();
    }

    public void initDb()
    {
        //本地数据的初始化
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName(dbName) //设置数据库名
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbDir(new File(dbDir)) // 数据库存储路径
                .setDbVersion(3) //设置数据库版本,每次启动应用时将会检查该版本号,
                // 发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener
                .setAllowTransaction(true) //设置是否开启事务,默认为false关闭事务
                .setTableCreateListener(new DbManager.TableCreateListener()
                {
                    @Override
                    public void onTableCreated(DbManager dbManager, TableEntity<?> tableEntity)
                    {
                        Log.d(TAG, "onTableCreated: ");
                    }
                })
                .setDbOpenListener(new DbManager.DbOpenListener()
                {
                    @Override
                    public void onDbOpened(DbManager db)
                    {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                // 设置数据库创建时的Listener
                .setDbUpgradeListener(new DbManager.DbUpgradeListener()
                {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion)
                    {
                        Log.d(TAG, "onUpgrade: ");
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                        // or
                        // db.dropDb();
                    }
                }); //设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等
        // .setDbDir(null);//设置数据库.db文件存放的目录,默认为包名下databases目录下

        db = x.getDb(daoConfig);
    }

    public void deleteDbFile()
    {
        File dbFile = new File(dbDir + dbName);
        if(dbFile.exists())
            dbFile.delete();
    }


    public void addRecords(List<GameBean> gameBeans) throws DbException {
        //加载数据
        //db.save(list); // 保存实体类或者实体类的List到数据库
        db.saveOrUpdate(gameBeans); // 保存或更新实体类或者实体类的List到数据库，根据id对应的数据是否存在
        //db.saveBindingId(list); // 保存实体类或实体类的List到数据库，如果该类型的id是自动生成的，则保存完后会给id赋值
    }

    public void addRecord(GameBean gameBean)
    {
        try {

            gameBean.setLast_modify_time(System.currentTimeMillis());
            //db.save(list); // 保存实体类或者实体类的List到数据库
            db.saveOrUpdate(gameBean); // 保存或更新实体类或者实体类的List到数据库，根据id对应的数据是否存在
            //db.saveBindingId(list); // 保存实体类或实体类的List到数据库，如果该类型的id是自动生成的，则保存完后会给id赋值
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteAllRecords() throws DbException {
        db.delete(GameBean.class);//该方法是删除表中的全部数据
        //db.deleteById(Person.class, 12);//该方法主要是根据表的主键(id)进行单条记录的删除
        //db.delete(Person.class, WhereBuilder.b("age", ">", "20"));//根据where语句的条件进行删除操作
        //List<Person> findAll = db.selector(Person.class).expr("age > 20").findAll();
        //db.delete(findAll);//根据实体bean进行对表里面的一条或多条数据进行删除
    }


    public List<GameBean> getAllRecordsByPage(int size)
    {
        try
        {
            if(db == null)
                initDb();

            Selector<GameBean> selector = db.selector(GameBean.class);
            selector.orderBy("last_modify_time", true);

            selector.limit(size);
            List<GameBean> gameBeans = selector.findAll();

            return gameBeans;
        }
        catch (DbException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
