package com.kiplening.basecore.db.manager;

import android.content.Context;
import android.text.TextUtils;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.kiplening.basecore.db.model.BaseModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongchao5 on 2017/9/19.
 */

public class BaseModelManager {
    public static String currentUser;
    private static BaseModelManager baseModelManager = null;

    protected BaseModelManager() {
    }

    public static BaseModelManager getInstance() {
        if (baseModelManager == null) {
            baseModelManager = new BaseModelManager();
        }
        return baseModelManager;
    }

    /**
     *
     * @param clazz
     * @param context
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T extends BaseModel> Dao<T, String> getDao(Class<T> clazz,
                                                          Context context, Class dbHelperClass) {
        Dao<T, String> baseModelDao = null;
        OrmLiteSqliteOpenHelper dbOpenHelper = OpenHelperManager.getHelper(
                context, dbHelperClass);
        try {
            baseModelDao = dbOpenHelper.getDao(clazz);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return baseModelDao;
    }

    /**
     *
     * 保存 or 更新BaseModel
     *
     * @param clazz <T>
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param <T>
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> void saveOrUpdateModel(Class<T> clazz,
                                                        Context context, T model, Class dbHelperClass) {
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context, dbHelperClass);
            model.setCurrentUser(currentUser);
            if (baseModelDao.idExists(model.getId())) {
                baseModelDao.update(model);
            } else {
                baseModelDao.create(model);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param clazz <T>
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param nPage
     *            第几页
     * @param nModelCountOfPerPage
     *            每页多少条数
     * @param attributeName
     *            属性名称
     * @param attribute
     *            属性
     * @param columnName
     *            排序的字段名称
     * @param ascending
     *            是否升序
     * @return
     */
    @SuppressWarnings({ "rawtypes", "deprecation" })
    public <T extends BaseModel> List<T> getPageModelsByAttribute(Class<T> clazz,
                                                                  Class dbHelperClass, Context context, int nPage,
                                                                  int nModelCountOfPerPage, String attributeName, String attribute,
                                                                  String columnName, boolean ascending) {
        List<T> list = new ArrayList<T>();
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            QueryBuilder<T, String> builder = baseModelDao.queryBuilder();
            long count = baseModelDao.countOf() - nPage * nModelCountOfPerPage;
            if (count > 0) {
                count = count > nModelCountOfPerPage ? nModelCountOfPerPage
                        : count;
                builder.orderBy(columnName, ascending)
                        .offset((long) (nPage * nModelCountOfPerPage)).limit(count)
                        .where().eq(attributeName, attribute).and()
                        .eq("currentUser", currentUser);
                list = baseModelDao.query(builder.prepare());
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     *
     * @param clazz <T>
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param nPage
     *            第几页
     * @param nModelCountOfPerPage
     *            每页多少条数
     * @param columnName
     *            排序的字段名称
     * @param ascending
     *            是否升序
     * @return
     */
    @SuppressWarnings({ "rawtypes", "deprecation" })
    public <T extends BaseModel> List<T> getPageModels(Class<T> clazz,
                                                       Class dbHelperClass, Context context, int nPage,
                                                       int nModelCountOfPerPage,String columnName, boolean ascending) {
        List<T> list = new ArrayList<T>();
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            QueryBuilder<T, String> builder = baseModelDao.queryBuilder();
            long count = baseModelDao.countOf() - nPage * nModelCountOfPerPage;
            if (count > 0) {
                count = count > nModelCountOfPerPage ? nModelCountOfPerPage
                        : count;
                builder.orderBy(columnName, ascending)
                        .offset((long) (nPage * nModelCountOfPerPage)).limit(count)
                        .where().eq("currentUser", currentUser);
                list = baseModelDao.query(builder.prepare());
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 获取当前账户下所有对象
     *
     * @param clazz <T>
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> List<T> getAllModels(Class<T> clazz,Class dbHelperClass
            ,Context context) {
        List<T> list = new ArrayList<T>();
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            QueryBuilder<T, String> builder = baseModelDao.queryBuilder();
            builder.where().eq("currentUser", currentUser);
            list = baseModelDao.query(builder.prepare());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查找model
     *
     * @param clazz <T>
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param id
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> T getModelById(Class<T> clazz,Class dbHelperClass,
                                                Context context, String id) {
        if (TextUtils.isEmpty(id)) {
            return null;
        }
        T model = null;
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            model = baseModelDao.queryForId(id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     *
     * @param clazz
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param columnName
     * @param attribute
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> List<T> getModelsByAttribute(Class<T> clazz,
                                                              Class dbHelperClass,Context context, String columnName, String attribute) {
        List<T> models = new ArrayList<T>();
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            QueryBuilder<T, String> builder = baseModelDao.queryBuilder();
            builder.where().eq(columnName, attribute).and()
                    .eq("currentUser", currentUser);
            models = baseModelDao.query(builder.prepare());
            System.out.println("ghj");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }

    /**
     *
     * @param clazz
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param columnName
     * @param attribute
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> List<T> getModelsByAttribute(Class<T> clazz,Class dbHelperClass,
                                                              Context context, String columnName, int attribute) {
        List<T> models = new ArrayList<T>();
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            QueryBuilder<T, String> builder = baseModelDao.queryBuilder();
            builder.where().eq(columnName, attribute).and()
                    .eq("currentUser", currentUser);
            models = baseModelDao.query(builder.prepare());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }

    /**
     *
     * @param clazz
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param columnName
     * @param attribute
     * @param orderColumn
     * @param ascending
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> List<T> getModelsByAttribute(Class<T> clazz,Class dbHelperClass,
                                                              Context context, String columnName, String attribute,
                                                              String orderColumn, boolean ascending) {
        List<T> models = new ArrayList<T>();
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            QueryBuilder<T, String> builder = baseModelDao.queryBuilder();
            builder.where().eq(columnName, attribute).and()
                    .eq("currentUser", currentUser);
            models = baseModelDao.query(builder.prepare());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }

    /**
     *
     * @param clazz
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param columnName
     * @param attribute
     * @param orderColumn
     * @param ascending
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> List<T> getOrderModelsByAttribute(
            Class<T> clazz,Class dbHelperClass, Context context, String columnName, int attribute,
            String orderColumn, boolean ascending) {
        List<T> models = new ArrayList<T>();
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            QueryBuilder<T, String> builder = baseModelDao.queryBuilder();
            builder.where().eq(columnName, attribute).and()
                    .eq("currentUser", currentUser);
            models = baseModelDao.query(builder.prepare());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }

    /**
     *
     * @param clazz
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param orderColumn
     * @param ascending
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> List<T> getOrderModels(
            Class<T> clazz,Class dbHelperClass, Context context,
            String orderColumn, boolean ascending) {
        List<T> models = new ArrayList<T>();
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            QueryBuilder<T, String> builder = baseModelDao.queryBuilder();
            builder.where().eq("currentUser", currentUser);
            models = baseModelDao.query(builder.prepare());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }

    /**
     *
     * @param clazz
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param columnName1
     * @param attribute1
     * @param columnName2
     * @param attribute2
     * @return
     */
    public <T extends BaseModel> List<T> getModelsByAttributes(Class<T> clazz,@SuppressWarnings("rawtypes") Class dbHelperClass,
                                                               Context context, String columnName1, String attribute1,
                                                               String columnName2, String attribute2) {
        List<T> models = new ArrayList<T>();
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            QueryBuilder<T, String> builder = baseModelDao.queryBuilder();
            builder.where().eq(columnName1, attribute1).and()
                    .eq(columnName2, attribute2).and()
                    .eq("currentUser", currentUser);
            models = baseModelDao.query(builder.prepare());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }

    /**
     *
     * @param clazz
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param columnName1
     * @param attribute1
     * @param columnName2
     * @param attribute2
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> List<T> getModelsByAttributes(Class<T> clazz,Class dbHelperClass,
                                                               Context context, String columnName1, String attribute1,
                                                               String columnName2, int attribute2) {
        List<T> models = new ArrayList<T>();
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            QueryBuilder<T, String> builder = baseModelDao.queryBuilder();
            builder.where().eq(columnName1, attribute1).and()
                    .eq(columnName2, attribute2).and()
                    .eq("currentUser", currentUser);
            models = baseModelDao.query(builder.prepare());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }

    /**
     * 删除baseMode
     *
     * @param clazz <T>
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param id
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> void delModel(Class<T> clazz,Class dbHelperClass, Context context,
                                               String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            baseModelDao.deleteById(id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除baseMode
     *
     * @param clazz <T>
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param columnName
     * @param attribute
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> void delModels(Class<T> clazz,Class dbHelperClass,
                                                Context context, String columnName, String attribute) {
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            DeleteBuilder<T, String> builder = baseModelDao.deleteBuilder();
            builder.where().eq(columnName, attribute).and()
                    .eq("currentUser", currentUser);
            baseModelDao.delete(builder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除baseMode
     * @param clazz <T>
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param columnName1
     * @param attribute1
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> void delModels(Class<T> clazz,Class dbHelperClass,
                                                Context context, String columnName1, String attribute1,
                                                String columnName2, String attribute2) {
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            DeleteBuilder<T, String> builder = baseModelDao.deleteBuilder();
            builder.where().eq(columnName1, attribute1).and()
                    .eq(columnName2, attribute2).and()
                    .eq("currentUser", currentUser);
            baseModelDao.delete(builder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param clazz
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param columnName1
     * @param attribute1
     * @param columnName2
     * @param attribute2
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> void delModels(Class<T> clazz,Class dbHelperClass,
                                                Context context, String columnName1, String attribute1,
                                                String columnName2, int attribute2) {
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            DeleteBuilder<T, String> builder = baseModelDao.deleteBuilder();
            builder.where().eq(columnName1, attribute1).and()
                    .eq(columnName2, attribute2).and()
                    .eq("currentUser", currentUser);
            baseModelDao.delete(builder.prepare());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param clazz
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> void delModels(Class<T> clazz,Class dbHelperClass, Context context) {
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            DeleteBuilder<T, String> builder = baseModelDao.deleteBuilder();
            builder.where().eq("currentUser", currentUser);
            baseModelDao.delete(builder.prepare());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/*
	 * 更新
	 */
    /**
     *
     * @param clazz
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param columnName
     * @param attribute
     * @param updateColumnName
     * @param updateAttribute
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> void updateModels(Class<T> clazz,Class dbHelperClass,
                                                   Context context, String columnName, String attribute,
                                                   String updateColumnName, String updateAttribute) {
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            UpdateBuilder<T, String> builder = baseModelDao.updateBuilder();
            builder.where().eq(columnName, attribute).and()
                    .eq("currentUser", currentUser);
            builder.updateColumnValue(updateColumnName, updateAttribute);
            baseModelDao.update(builder.prepare());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/*
	 * 更新
	 */
    /**
     *
     * @param clazz
     * @param dbHelperClass 数据帮助类，继承自OrmLiteSqliteOpenHelper
     * @param context
     * @param columnName
     * @param attribute
     * @param updateColumnName
     * @param updateAttribute
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseModel> void updateModels(Class<T> clazz, Class dbHelperClass,
                                                   Context context, String columnName, String attribute,
                                                   String updateColumnName, int updateAttribute) {
        try {
            Dao<T, String> baseModelDao = getDao(clazz, context,dbHelperClass);
            UpdateBuilder<T, String> builder = baseModelDao.updateBuilder();
            builder.where().eq(columnName, attribute).and()
                    .eq("currentUser", currentUser);
            builder.updateColumnValue(updateColumnName, updateAttribute);
            baseModelDao.update(builder.prepare());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
