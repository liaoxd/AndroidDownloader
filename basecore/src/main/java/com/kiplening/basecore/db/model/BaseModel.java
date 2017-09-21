package com.kiplening.basecore.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by hongchao5 on 2017/9/19.
 */
@DatabaseTable
public class BaseModel implements Serializable,Cloneable{
    private static final long serialVersionUID = 660247134904241826L;
    @DatabaseField(id = true)
    private String id;// id
    @DatabaseField
    private String currentUser;// 当前用户

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    @SuppressWarnings("finally")
    @Override
    public Object clone() {
        // 注意此处要把protected改为public
        // 序列化
        Object obj = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(this);

            // 反序列化
            ByteArrayInputStream bis = new ByteArrayInputStream(
                    bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);

            obj = ois.readObject();
        } catch (Exception e) {
            // TODO Auto-generated catch block
        } finally {
            return obj;
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        try {
            Class<? extends BaseModel> t = this.getClass();
            Field[] fields = t.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                sb.append("{");
                sb.append(field.getName());
                sb.append(":");
                if (field.getType() == Integer.class) {
                    sb.append(field.getInt(this));
                } else if (field.getType() == Long.class) {
                    sb.append(field.getLong(this));
                } else if (field.getType() == Boolean.class) {
                    sb.append(field.getBoolean(this));
                } else if (field.getType() == char.class) {
                    sb.append(field.getChar(this));
                } else if (field.getType() == Double.class) {
                    sb.append(field.getDouble(this));
                } else if (field.getType() == Float.class) {
                    sb.append(field.getFloat(this));
                } else
                    sb.append(field.get(this));
                sb.append("}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
