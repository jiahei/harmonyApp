package com.example.myharmonyapplication_api4.slice;

import com.example.myharmonyapplication_api4.ResourceTable;
import com.example.myharmonyapplication_api4.beans.InfoResult;
import com.example.myharmonyapplication_api4.beans.Item;
import com.example.myharmonyapplication_api4.provider.TabPageSliderProvider;
import com.example.myharmonyapplication_api4.utils.DataBaseUtils;
import com.example.myharmonyapplication_api4.utils.HttpRequestUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;

import java.util.ArrayList;
import java.util.List;

public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener {

    //定义两个按钮变量
    private Button postInfo;
    private Button exit;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        //应用启动之后首先加载主页面，使用数据库帮助类查询鸿蒙本地数据库，看是否已登录
        String state = DataBaseUtils.getUser("state",this);
        //若登录状态不为空，即已经登录过了，执行下面函数
        if (state!=null){
            //使用数据库帮助类，从鸿蒙本地数据库中取出用户名信息
            String username = DataBaseUtils.getUser("username",this);
            //初始化tabList
            TabList tabList = (TabList)findComponentById(ResourceTable.Id_tablist);
            //定义tab的文本内容，并将其插入到tabList中
            String[] tabTexts = {"寻找组队","发布信息","我的"};
            for (String tabText : tabTexts) {
                TabList.Tab tab = tabList.new Tab(this);
                tab.setText(tabText);
                if (tabList.getTabCount()!=3){
                    tabList.addTab(tab);
                }
            }
            //将三个页面数据添加到layoutFileIds中
            List<Integer> layoutFileIds = new ArrayList<>();
            layoutFileIds.add(ResourceTable.Layout_viewinfo_list);
            layoutFileIds.add(ResourceTable.Layout_post_info);
            layoutFileIds.add(ResourceTable.Layout_my_info);
            //定义pageSlider对象
            PageSlider pageSlider = (PageSlider)findComponentById(ResourceTable.Id_page_slider);
            //使用TabPageSliderProvider初始化pageSlider
            pageSlider.setProvider(new TabPageSliderProvider(layoutFileIds,this));
            //TabList与PageSlider联动
            tabList.addTabSelectedListener(new TabList.TabSelectedListener() {
                @Override
                public void onSelected(TabList.Tab tab) {
                    //获取点击的TabList的索引
                    int position = tab.getPosition();
                    //设置pageSlider的索引与TabList的索引一致
                    pageSlider.setCurrentPage(position);
                    //如果选中第一个页面，初始话第一个页面
                    if (tabList.getSelectedTabIndex()==0){
                        initTableList(pageSlider);
                    }else if (tabList.getSelectedTabIndex()==1){
                        //如果选中了第二个页面，分配按钮变量，并绑定按钮点击事件监听函数
                        postInfo = (Button) findComponentById(ResourceTable.Id_postInfo);
                        postInfo.setClickedListener(MainAbilitySlice.this);
                    }else if (tabList.getSelectedTabIndex()==2){
                        //如果选中了第三个页面，分配按钮变量，并绑定按钮点击事件监听函数，并将用户名传给Text组件
                        exit = (Button) findComponentById(ResourceTable.Id_exit);
                        exit.setClickedListener(MainAbilitySlice.this);
                        Text UserName = (Text) findComponentById(ResourceTable.Id_theUserName);
                        UserName.setText(username);
                    }
                }
                @Override
                public void onUnselected(TabList.Tab tab) {}
                @Override
                public void onReselected(TabList.Tab tab) {}
            });
            //PageSlider与TabList联动
            pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
                    @Override
                public void onPageSliding(int i, float v, int i1) {}
                @Override
                public void onPageSlideStateChanged(int i) {}
                @Override
                public void onPageChosen(int i) {
                    //参数i表示当前索引，设置TabList的索引与PageSlider的索引一致
                    if (tabList.getSelectedTabIndex()!=i){
                        tabList.selectTabAt(i);
                    }
                }
            });
            //默认选中第一个页面
            tabList.selectTabAt(0);
        }else {
            //若登录状态为空，即没有登录，跳转到登录页面
            present(new EntranceSlice(),new Intent());
        }

    }

    //初始化第一个页面
    private void initTableList(PageSlider pageSlider){
        //定义线程任务分发器
        TaskDispatcher globalTaskDispatcher = getGlobalTaskDispatcher(TaskPriority.DEFAULT);
        TaskDispatcher uiTaskDispatcher = getUITaskDispatcher();
        //定义Context变量，获取上下文
        Context context = this;
        //创建异步线程，派发异步任务发送网络请求
        globalTaskDispatcher.asyncDispatch(()->{
            //定义要访问的url
            String urlString = "此处填写自己的需要请求的url";
            //使用网络请求帮助类发送GET请求访问数据
            String result = HttpRequestUtils.sendGetRequest(this,urlString);
            System.out.println(result+"------------------------------------");
            //将json数据转化为我们可以使用的数据类型
            Gson gson = new Gson();
            InfoResult infoResult = gson.fromJson(result,InfoResult.class);
            //如果请求结果的代码为10000，也就是请求到了数据，执行下面的函数
            if (infoResult.getCode()==10000){
                //获取请求得到的数据中的结果数据
                String str =gson.toJson(infoResult.getResult());
                List<Item> infoList = gson.fromJson(str,new TypeToken<List<Item>>(){}.getType());
                System.out.println("请求数据成功");
                //创建UI线程进行UI相关操作
                uiTaskDispatcher.asyncDispatch(()->{
                    //定义一个TableLayout变量container
                    TableLayout container = findComponentById(ResourceTable.Id_list_table);
                    //遍历请求到的数据，将其赋给每一个Item
                    for (Item info : infoList) {
                        System.out.println(info);
                        //获取Item布局模板
                        Component itemTemplate = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_viewinfo_list_item, null, false);
                        //获取item布局中的text组件
                        Text sportTypeText = itemTemplate.findComponentById(ResourceTable.Id_sportType);
                        Text timeText = itemTemplate.findComponentById(ResourceTable.Id_time);
                        Text levelText = itemTemplate.findComponentById(ResourceTable.Id_level);
                        Text numText = itemTemplate.findComponentById(ResourceTable.Id_numNeeded);
                        //绑定数据到text组件上
                        sportTypeText.setText("类型:" + info.getSportType());
                        timeText.setText("时间地点:" + info.getTime() + info.getLocation());
                        levelText.setText("水平:" + info.getLevel()+"  需人:");
                        numText.setText(info.getNumNeeded()+"");
                        //获取item布局中的button组件
                        Button add = itemTemplate.findComponentById(ResourceTable.Id_add);
                        Button del = itemTemplate.findComponentById(ResourceTable.Id_del);
                        //定义两个按钮的颜色属性，灰色为不可点击，蓝色为可以点击
                        ShapeElement shapeElement1 = new ShapeElement();
                        ShapeElement shapeElement2 = new ShapeElement();
                        shapeElement1.setRgbColor(new RgbColor(0x99, 0x99, 0x99));
                        shapeElement2.setRgbColor(new RgbColor(0x21, 0xa8, 0xfd));
                        //将取消按钮颜色设置为灰色
                        del.setBackground(shapeElement1);
                        Component.ClickedListener listener = component -> {
                            //获取点击前的“需要人数”的数目
                            int oldNum = Integer.parseInt(numText.getText());
                            //定义更新之后的数据
                            int newNum = oldNum;
                            //定义一个update，判断是否进行数据更新，更新了为true，没更新为false
                            boolean update = false;
                            System.out.println(oldNum);
                            //如果点击了取消按钮，执行下面的函数
                            if (component == del) {
                                //将原数据+1赋给新数据
                                newNum = oldNum + 1;
                                System.out.println("增加人数----------------" + info.getUid());
                                //将update变量设置为true，表示已经更新数据
                                update = true;
                                //设置取消按钮不可点击，颜色为灰色；设置组队按钮可以点击，颜色为蓝色
                                add.setClickable(true);
                                del.setClickable(false);
                                add.setBackground(shapeElement2);
                                del.setBackground(shapeElement1);
                            } else if (component == add) {
                                //点击了组队按钮
                                //判断需求人数是否大于0，也就是是否还需要人，还需要人就执行下面操作
                                if (oldNum > 0){
                                    //将人数减一
                                    newNum = oldNum - 1;
                                    System.out.println("减少人数----------------" + info.getUid());
                                    //将update变量设置为true，表示已经更新数据
                                    update = true;
                                    //设置组队按钮不可点击，颜色为灰色；设置取消按钮可以点击，颜色为蓝色
                                    add.setClickable(false);
                                    del.setClickable(true);
                                    add.setBackground(shapeElement1);
                                    del.setBackground(shapeElement2);
                                }else{
                                    //创建UI线程，在屏幕下方显示“人数已满”弹窗
                                    getUITaskDispatcher().asyncDispatch(()->{
                                        ToastDialog toastDialog = new ToastDialog(this);
                                        toastDialog.setAlignment(LayoutAlignment.BOTTOM);
                                        toastDialog.setOffset(0,200);
                                        toastDialog.setText("人数已满");
                                        toastDialog.show();
                                    });
                                }
                            }
                            //将更新后的数据改为final类型
                            final int num = newNum;
                            System.out.println(num);
                            //定义两个操作通用的baseUrl
                            String baseUrl =  "此处填写自己的需要请求的url";
                            //如果更新了数据，执行下面的操作
                            if (update){
                                //如果更新后的数据比原来的大，执行下面的操作
                                if (newNum>oldNum){
                                    //创建异步线程，派发异步任务发送网络请求
                                    globalTaskDispatcher.asyncDispatch(()->{
                                        //定义要请求的url
                                        String addString = baseUrl+info.getUid()+"&method=add";
                                        //使用网络请求帮助类发送POST请求访问数据
                                        String addResult = HttpRequestUtils.sendPostRequest(this,addString);
                                        //如果修改数据成功了，在UI线程中修改显示内容
                                        if (addResult.equals("OK")){
                                            uiTaskDispatcher.asyncDispatch(()->{
                                                numText.setText(num+"");
                                            });
                                        }
                                    });
                                }else {
                                    //如果更新后的数据比原来的小，执行下面的操作
                                    //创建异步线程，派发异步任务发送网络请求
                                    globalTaskDispatcher.asyncDispatch(()->{
                                        //定义要请求的url
                                        String delString = baseUrl+info.getUid()+"&method=del";
                                        //使用网络请求帮助类发送POST请求访问数据
                                        String delResult = HttpRequestUtils.sendPostRequest(this,delString);
                                        //如果修改数据成功了，在UI线程中修改显示内容
                                        if (delResult.equals("OK")){
                                            uiTaskDispatcher.asyncDispatch(()->{
                                                numText.setText(num+"");
                                            });
                                        }
                                    });
                                }
                                //修改update的值为false
                                update = false;
                            }
                        };
                        //为两个按钮绑定点击事件监听函数
                        add.setClickedListener(listener);
                        del.setClickedListener(listener);
                        //最开始设置组队按钮可点击，取消按钮不可点击
                        add.setClickable(true);
                        del.setClickable(false);
                        //将一个item数据添加到tablelayout中
                        container.addComponent(itemTemplate);
                    }
                });
            }else {
                System.out.println("请求数据失败");
            }
        });
    }

    @Override
    public void onClick(Component component) {
        //在发布信息页面的发布信息按钮点击事件监听函数
        if (component == postInfo){
            //定义五个输入框变量
            TextField sportType = (TextField) findComponentById(ResourceTable.Id_sportType);
            TextField time = (TextField) findComponentById(ResourceTable.Id_time);
            TextField location = (TextField) findComponentById(ResourceTable.Id_location);
            TextField level = (TextField) findComponentById(ResourceTable.Id_level);
            TextField numNeeded = (TextField) findComponentById(ResourceTable.Id_numNeeded);
            //获取输入框输入的内容
            String sportTypeText = sportType.getText();
            String timeText = time.getText();
            String locationText = location.getText();
            String levelText = level.getText();
            String numNeededText = numNeeded.getText();
            //定义线程任务分发器
            TaskDispatcher globalTaskDispatcher = getGlobalTaskDispatcher(TaskPriority.DEFAULT);
            //若五个输入框均不为空，执行下面的操作
            if (!sportTypeText.equals("")&&!timeText.equals("")&&!locationText.equals("")&&!levelText.equals("")&&!numNeededText.equals("")){
                //创建异步线程，派发异步任务发送网络请求
                globalTaskDispatcher.asyncDispatch(()->{
                    //定义要请求的url
                    String urlString = "此处填写自己的需要请求的url"+sportTypeText+"&time="+timeText+"&location="+locationText+"&level="+levelText+"&numNeeded="+numNeededText+""; // 开发者根据实际情况自定义EXAMPLE_URL
                    System.out.println(urlString);
                    //使用网络请求帮助类发送POST请求上传数据
                    String result = HttpRequestUtils.sendPostRequest(this,urlString);
                    System.out.println(result+"------------------------------------");
                    //如果上传数据成功了，执行下面操作
                    if (result.equals("OK")){
                        System.out.println("发布信息成功！");
                        //创建UI线程，在屏幕下方显示“发布信息成功”弹窗
                        getUITaskDispatcher().asyncDispatch(()->{
                            ToastDialog toastDialog = new ToastDialog(this);
                            toastDialog.setAlignment(LayoutAlignment.BOTTOM);
                            toastDialog.setOffset(0,200);
                            toastDialog.setText("发布信息成功！");
                            toastDialog.show();
                        });
                    }else {
                        //如果上传数据失败了，执行下面操作
                        System.out.println("发布信息失败");
                        //创建UI线程，在屏幕下方显示“发布信息失败”弹窗
                        getUITaskDispatcher().asyncDispatch(()->{
                            ToastDialog toastDialog = new ToastDialog(this);
                            toastDialog.setAlignment(LayoutAlignment.BOTTOM);
                            toastDialog.setOffset(0,200);
                            toastDialog.setText("发布信息失败");
                            toastDialog.show();
                        });
                    }
                });
            }else {
                //如果有输入框输入内容为空，在屏幕下方显示“请检查信息是否完整”弹窗
                ToastDialog toastDialog = new ToastDialog(this);
                toastDialog.setAlignment(LayoutAlignment.BOTTOM);
                toastDialog.setOffset(0,200);
                toastDialog.setText("请检查信息是否完整");
                toastDialog.show();
            }
        }else if (component == exit){
            //如果在我的页面点击了退出登录按钮，执行下面操作
            //使用数据库帮助类访问鸿蒙自带的本地数据库获取用户的用户名、登陆状态等信息
            String username = DataBaseUtils.getUser("username",this);
            //如果用户名不为空，即用户已经登录过，执行下面的操作
            if (username!=null){
                //使用数据库帮助类访问鸿蒙自带的本地数据库，删除用户的用户名、登陆状态等信息
                int i = DataBaseUtils.deleteUser(username,this);
                System.out.println(i+"++++++++++++++++++++++++++");
                //跳转到登录页面
                present(new EntranceSlice(),new Intent());
            }
        }
    }
}
