package com.example.myharmonyapplication_api4.utils;

import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.app.Context;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;

public class DataBaseUtils {

    //定义本地数据库的uri
    private static Uri uri = Uri.parse("dataability:///com.example.myharmonyapplication_api4.UserDataAbility/user");

    //根据传入的列在鸿蒙本地数据库中查询数据
    public static String getUser(String column,Context context){
        //定义返回数据为null
        String state = null;
        //创建DataAbilityHelper变量
        DataAbilityHelper dataAbilityHelper = DataAbilityHelper.creator(context);
        //定义要查询的列和查询条件
        String[] columns = {column};
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.equalTo("state","exist");
        //使用dataAbilityHelper进行查询操作，并返回查询结果
        try {
            ResultSet query = dataAbilityHelper.query(uri, columns, predicates);
            if (query.getRowCount()>0){
                query.goToFirstRow();
                state = query.getString(0);
            }
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }
        return state;
    }

    //向鸿蒙本地数据库插入数据
    public static int setUser(String uname, Context context){
        //定义返回变量，0表示插入失败，1表示插入成功
        int i = 0;
        //构造要插入的数据
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString("username",uname);
        valuesBucket.putString("state","exist");
        //创建DataAbilityHelper变量
        DataAbilityHelper dataAbilityHelper = DataAbilityHelper.creator(context);
        //使用dataAbilityHelper进行插入操作，并返回插入结果
        try {
            i = dataAbilityHelper.insert(uri,valuesBucket);
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }
        return i;
    }

    //向鸿蒙本地数据库删除数据
    public static int deleteUser(String username,Context context){
        //定义返回变量，0表示插入失败，1表示插入成功
        int i = 0;
        //创建DataAbilityHelper变量
        DataAbilityHelper dataAbilityHelper = DataAbilityHelper.creator(context);
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        //定义删除条件
        predicates.equalTo("username",username);
        //使用dataAbilityHelper进行删除操作，并返回删除结果
        try {
            i = dataAbilityHelper.delete(uri, predicates);
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }
        return i;
    }

}
