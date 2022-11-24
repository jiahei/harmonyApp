package com.example.myharmonyapplication_api4.provider;

import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.PageSliderProvider;

import java.util.List;

public class TabPageSliderProvider extends PageSliderProvider {

    //定义需要的两个变量
    private List<Integer> layoutFileIds;
    private AbilitySlice abilitySlice;

    //构造函数
    public TabPageSliderProvider(List<Integer> layoutFileIds, AbilitySlice abilitySlice) {
        this.layoutFileIds = layoutFileIds;
        this.abilitySlice = abilitySlice;
    }

    //返回共有多少条数据
    @Override
    public int getCount() {
        return layoutFileIds.size();
    }

    //传入页面数据
    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        Integer id = layoutFileIds.get(i);
        Component component = LayoutScatter.getInstance(abilitySlice).parse(id, null, false);
        componentContainer.addComponent(component);
        return component;
    }

    //销毁数据
    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        componentContainer.removeComponent((Component) o);
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return true;
    }
}
