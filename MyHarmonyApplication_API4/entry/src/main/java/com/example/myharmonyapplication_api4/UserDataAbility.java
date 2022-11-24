package com.example.myharmonyapplication_api4;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.data.DatabaseHelper;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.dataability.DataAbilityUtils;
import ohos.data.rdb.*;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;

public class UserDataAbility extends Ability {
    //定义Data Ability数据库相关常量
    private static final String DB_NAME = "userdata.db";

    private static final String DB_TAB_NAME = "user";

    private static final String DB_COLUMN_USER_ID = "userid";

    private static final String DB_COLUMN_USERNAME = "username";

    private static final String DB_COLUMN_PASSWORD = "state";

    private static final int DB_VERSION = 1;
    //定义RdbStore变量，并通过RdbOpenCallback创建数据库数据表
    private StoreConfig config = StoreConfig.newDefaultConfig(DB_NAME);

    private RdbStore rdbStore;

    private RdbOpenCallback rdbOpenCallback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore store) {
            //创建数据表
            store.executeSql("create table if not exists "
                    + DB_TAB_NAME + " ("
                    + DB_COLUMN_USER_ID + " integer primary key autoincrement, "
                    + DB_COLUMN_USERNAME + " text not null, "
                    + DB_COLUMN_PASSWORD + " text not null)");
        }

        @Override
        public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        System.out.println("UserDataAbility onStart");
        //初始化与数据库的连接
        DatabaseHelper helper = new DatabaseHelper(this);
        rdbStore = helper.getRdbStore(config,DB_VERSION,rdbOpenCallback,null);
    }

    @Override
    public ResultSet query(Uri uri, String[] columns, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, DB_TAB_NAME);
        ResultSet resultSet = rdbStore.query(rdbPredicates, columns);
        if (resultSet == null) {
            System.out.println("resultSet is null");
        }
        return resultSet;
    }

    @Override
    public int insert(Uri uri, ValuesBucket value) {
        System.out.println("UserDataAbility insert");
        String path = uri.getLastPath();
        if (!"user".equals(path)) {
            System.out.println("DataAbility insert path is not matched");
            return -1;
        }
        ValuesBucket values = new ValuesBucket();
        values.putString(DB_COLUMN_USERNAME, value.getString(DB_COLUMN_USERNAME));
        values.putString(DB_COLUMN_PASSWORD, value.getString(DB_COLUMN_PASSWORD));
        int index = (int) rdbStore.insert(DB_TAB_NAME, values);
        return index;
    }

    @Override
    public int delete(Uri uri, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, DB_TAB_NAME);
        int index = rdbStore.delete(rdbPredicates);
        System.out.println("delete: " + index);
        return index;
    }

    @Override
    public int update(Uri uri, ValuesBucket value, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, DB_TAB_NAME);
        int index = rdbStore.update(value, rdbPredicates);
        System.out.println("update: " + index);
        return index;
    }
}