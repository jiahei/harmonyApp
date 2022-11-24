package com.example.myharmonyapplication_api4.slice;

import com.example.myharmonyapplication_api4.ResourceTable;
import com.example.myharmonyapplication_api4.utils.HttpRequestUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;

import java.util.Objects;

public class RegisterSlice extends AbilitySlice implements Component.ClickedListener{

    //定义两个输入框变量
    private TextField username;
    private TextField password;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_register);
        //注册账号
        Button register = (Button)findComponentById(ResourceTable.Id_register);
        //获取文本框输入的用户名和密码
        username = (TextField)findComponentById(ResourceTable.Id_username);
        password = (TextField)findComponentById(ResourceTable.Id_password);
        //为按钮绑定点击事件监听函数
        register.setClickedListener(this);
    }

    @Override
    public void onClick(Component component) {
        //获取用户输入的用户名密码等信息
        String userName = username.getText();
        String passWord = password.getText();
        //定义线程任务分发器
        TaskDispatcher globalTaskDispatcher = getGlobalTaskDispatcher(TaskPriority.DEFAULT);
        System.out.println(userName+"--------------------------------");
        System.out.println(passWord+"--------------------------------");
        //创建异步线程，派发异步任务发送网络请求
        globalTaskDispatcher.asyncDispatch(()->{
            //定义要请求的url
            String urlString = "此处填写自己的需要请求的url";
            //使用网络请求帮助类发送GET请求上传数据
            String result = HttpRequestUtils.sendGetRequest(this,urlString);
            System.out.println(result+"------------------------------------");
            //如果注册成功了，执行以下操作
            if (Objects.equals("OK", result)){
                //创建UI线程，在屏幕下方显示“注册成功”弹窗
                getUITaskDispatcher().asyncDispatch(()->{
                    ToastDialog toastDialog = new ToastDialog(this);
                    toastDialog.setAlignment(LayoutAlignment.BOTTOM);
                    toastDialog.setOffset(0,200);
                    toastDialog.setText("注册成功");
                    toastDialog.show();
                });
                System.out.println("注册成功");
                //跳转到登录页面
                present(new EntranceSlice(),new Intent());
            }else{
                //如果该用户名已经被注册了，执行以下操作
                System.out.println("注册失败");
                //创建UI线程，在屏幕下方显示“该用户名已注册”弹窗
                getUITaskDispatcher().asyncDispatch(()->{
                    ToastDialog toastDialog = new ToastDialog(this);
                    toastDialog.setAlignment(LayoutAlignment.BOTTOM);
                    toastDialog.setOffset(0,200);
                    toastDialog.setText("该用户名已注册");
                    toastDialog.show();
                });
            }
        });
    }
}
