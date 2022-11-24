package com.example.myharmonyapplication_api4.slice;

import com.example.myharmonyapplication_api4.ResourceTable;
import com.example.myharmonyapplication_api4.utils.DataBaseUtils;
import com.example.myharmonyapplication_api4.utils.HttpRequestUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;

public class EntranceSlice extends AbilitySlice implements Component.ClickedListener {

    //定义需要用到的一些变量
    private TextField username;
    private TextField password;
    private Button login;
    private Button register;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_login);
        //初始化私有变量
        username = (TextField)findComponentById(ResourceTable.Id_username);
        password = (TextField)findComponentById(ResourceTable.Id_password);
        login = (Button)findComponentById(ResourceTable.Id_login);
        register = (Button)findComponentById(ResourceTable.Id_register);
        //为登录注册按钮绑定点击事件函数
        login.setClickedListener(this);
        register.setClickedListener(this);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    //按钮点击事件监听函数
    @Override
    public void onClick(Component component) {
        //当点击了注册按钮之后跳转到注册页面
        if (component == register){
            //跳转到注册页面，设置两个按钮可以点击
            present(new RegisterSlice(),new Intent());
            register.setClickable(true);
            login.setClickable(true);
        }else {//当点击了登录按钮，执行下面的函数
            //获取在用户名和密码输入框中输入的内容
            String userName = username.getText();
            String passWord = password.getText();
            //定义线程任务分发器
            TaskDispatcher globalTaskDispatcher = getGlobalTaskDispatcher(TaskPriority.DEFAULT);
            System.out.println(userName+"--------------------------------");
            System.out.println(passWord+"--------------------------------");
            //创建异步线程，派发异步任务发送网络请求
            globalTaskDispatcher.asyncDispatch(()->{
                //定义要访问的url
                String urlString = "此处填写自己的需要请求的url"+userName;
                //使用网络请求帮助类发送GET请求访问数据
                String result = HttpRequestUtils.sendGetRequest(this,urlString);
                System.out.println(result+"------------------------------------");
                //如果用户名和密码不为空，且根据用户名访问到的密码数据与输入的密码一致，执行下面的函数
                if (passWord.equals(result) && !passWord.equals("") && !userName.equals("")){
                    System.out.println("登录成功");
                    //使用数据库帮助类向鸿蒙自带的本地数据库插入用户的用户名、登陆状态等信息
                    int i = DataBaseUtils.setUser(userName,this);
                    System.out.println(i+"---------------------------------------------");
                    //创建UI线程，在屏幕下方显示“登录成功”弹窗
                    getUITaskDispatcher().asyncDispatch(()->{
                        ToastDialog toastDialog = new ToastDialog(this);
                        toastDialog.setAlignment(LayoutAlignment.BOTTOM);
                        toastDialog.setOffset(0,200);
                        toastDialog.setText("登录成功");
                        toastDialog.show();
                    });
                    //跳转到应用的主页面
                    present(new MainAbilitySlice(),new Intent());
                }else{
                    //如果密码不匹配或者用户名或密码为空，执行下面的操作
                    System.out.println("登陆失败");
                    //创建UI线程，在屏幕下方显示“登录失败”弹窗
                    getUITaskDispatcher().asyncDispatch(()->{
                        ToastDialog toastDialog = new ToastDialog(this);
                        toastDialog.setAlignment(LayoutAlignment.BOTTOM);
                        toastDialog.setOffset(0,200);
                        toastDialog.setText("用户名或密码错误");
                        toastDialog.show();
                    });
                }
            });

        }
    }
}