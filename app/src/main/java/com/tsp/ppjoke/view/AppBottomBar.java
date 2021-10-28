package com.tsp.ppjoke.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.tsp.main.R;
import com.tsp.ppjoke.model.BottomBar;
import com.tsp.ppjoke.model.Destination;
import com.tsp.ppjoke.utils.AppConfig;

import java.util.List;

/**
 * author : shengping.tian
 * time   : 2021/10/28
 * desc   : 自定义底部导航栏 继承自 BottomNavigationView
 * version: 1.0
 */
public class AppBottomBar extends BottomNavigationView {

    /**
     * 底部导航栏图标
     */
    private static int[] sIcons = new int[]{
            R.mipmap.icon_tab_home,
            R.mipmap.icon_tab_sofa,
            R.mipmap.icon_tab_publish,
            R.mipmap.icon_tab_find,
            R.mipmap.icon_tab_mine
    };

    private BottomBar mConfig;


    public AppBottomBar(@NonNull Context context) {
        this(context, null);
    }

    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @SuppressLint({"WrongConstant", "RestrictedApi"})
    private void initView() {
        mConfig = AppConfig.getBottomBarConfig();
        //构建 item 点击的状态选择器，类似于 xm 中定义的 selector
        //定义二维数组颜色，和状态
        int[][] state = new int[2][];
        state[0] = new int[]{android.R.attr.state_selected};
        state[1] = new int[]{};
        int[] colors = new int[]{Color.parseColor(mConfig.activeColor), Color.parseColor(mConfig.inActiveColor)};
        ColorStateList stateList = new ColorStateList(state, colors);
        setItemTextColor(stateList);
        setItemIconTintList(stateList);

        //LABEL_VISIBILITY_LABELED: 设置按钮的文本为一直显示模式
        //LABEL_VISIBILITY_AUTO: 当按钮个数小于三个时一直显示，或者当按钮个数大于3个且小于5个时，被选中的那个按钮文本才会显示
        //LABEL_VISIBILITY_SELECTED：只有被选中的那个按钮的文本才会显示
        //LABEL_VISIBILITY_UNLABELED:所有的按钮文本都不显示
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        List<BottomBar.Tab> tabs = mConfig.tabs;
        for (BottomBar.Tab tab : tabs) {
            if (!tab.enable) {
                continue;
            }
            int itemId = getItemId(tab.pageUrl);
            if (itemId < 0) {
                continue;
            }
            MenuItem menuItem = getMenu().add(0, itemId, tab.index, tab.title);
            menuItem.setIcon(sIcons[tab.index]);
        }

        //此处给按钮icon设置大小
        int index = 0;
        for (BottomBar.Tab tab : mConfig.tabs) {
            if (!tab.enable) {
                continue;
            }
            int itemId = getItemId(tab.pageUrl);
            if (itemId < 0) {
                continue;
            }
            int iconSize = dp2Px(tab.size);
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(index);
            itemView.setIconSize(iconSize);
            if (TextUtils.isEmpty(tab.title)) {
                int tintColor = TextUtils.isEmpty(tab.tintColor) ? Color.parseColor("#ff678f") : Color.parseColor(tab.tintColor);
                itemView.setIconTintList(ColorStateList.valueOf(tintColor));
                //禁止掉点按时 上下浮动的效果
                itemView.setShifting(false);
                /**
                 * 如果想要禁止掉所有按钮的点击浮动效果。
                 * 那么还需要给选中和未选中的按钮配置一样大小的字号。
                 *  在MainActivity布局的AppBottomBar标签增加如下配置，
                 *  @style/active，@style/inActive 在style.xml中
                 *  app:itemTextAppearanceActive="@style/active"
                 *  app:itemTextAppearanceInactive="@style/inActive"
                 */
            }
            index++;
        }

        //底部导航栏默认选中项
        if (mConfig.selectTab != 0) {
            BottomBar.Tab selectTab = mConfig.tabs.get(mConfig.selectTab);
            if (selectTab.enable) {
                int itemId = getItemId(selectTab.pageUrl);
                //这里需要延迟一下 再定位到默认选中的tab
                //因为 咱们需要等待内容区域,也就NavGraphBuilder解析数据并初始化完成，
                //否则会出现 底部按钮切换过去了，但内容区域还没切换过去
                post(() -> setSelectedItemId(itemId));
            }
        }
    }

    private int dp2Px(int dpValue) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return (int) (metrics.density * dpValue + 0.5f);
    }

    /**
     * 构建 tab 的item条目的 id
     * 根据 pagerUri 地址
     */
    private int getItemId(String pageUrl) {
        Destination destination = AppConfig.getDestConfig().get(pageUrl);
        if (destination == null) return -1;
        return destination.id;
    }
}
