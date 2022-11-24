package com.example.myharmonyapplication_api4;

import com.example.myharmonyapplication_api4.slice.EntranceSlice;
import com.example.myharmonyapplication_api4.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
